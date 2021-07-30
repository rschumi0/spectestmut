package javamu.mutator.Mutations;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import javamu.ASTparser.MyNode;
import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddSynchronized extends Mutation {

    public AddSynchronized(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(MyNode ast) {
        Boolean status = false;
        if(super.start(ast)){
            for(String type : getAddTo()) {
                Statement tempFirstStmt = null;
                //MyNode newNode = getMutauionNode();
                ArrayList<MyNode> targetBlocks = getNodeType(type, ast);
                for (MyNode target : targetBlocks) {
                    //newNode.getSubNodes().get(0).setName(getParameters().getProperty("condition"));
                    SynchronizedStmt synS = new SynchronizedStmt();

                    if(((BlockStmt)target.getNode()).getStatements().size() > 0 ){
                        Statement s =((BlockStmt)target.getNode()).getStatements().get(0);
                        if(s instanceof ExplicitConstructorInvocationStmt){
                            tempFirstStmt = s;
                            ((BlockStmt)target.getNode()).getStatements().remove(0);
                        }
                    }

                    ObjectCreationExpr expression = new ObjectCreationExpr();
                    expression.setType(new ClassOrInterfaceType("Object"));

                    VariableDeclarator var = new VariableDeclarator();
                    var.setName("insertedTestObj"+RandomStringUtils.randomAlphanumeric(10));
                    var.setType("Object");
                    var.setInitializer(expression);

                    ExpressionStmt expressionStmt = new ExpressionStmt();
                    VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr();
                    variableDeclarationExpr.addVariable(var);
                    expressionStmt.setExpression(variableDeclarationExpr);

                    synS.setExpression(var.getNameAsExpression());//new ThisExpr());
                    int nested = (int) getParameters().getOrDefault("nested",1);
                    System.out.println("nested: " + nested);


                    if(getParameters().getProperty("move") != null){

                        if(nested > 1)
                        {
                            addNested(synS, nested, ((BlockStmt)target.getNode()).getStatements(),false);
                        }
                        else{
                            synS.getBody().getStatements().addAll(((BlockStmt)target.getNode()).getStatements());
                        }
                        ((BlockStmt)target.getNode()).getStatements().clear();

                    }
                    else if(nested > 1)
                    {
                        addNested(synS, nested, null,true);
                    }


                    ((BlockStmt)target.getNode()).addStatement(expressionStmt);
                    ((BlockStmt)target.getNode()).addStatement(synS);

                    if(tempFirstStmt != null){
                        ((BlockStmt)target.getNode()).getStatements().add(0,tempFirstStmt);
                        tempFirstStmt = null;
                    }
                    status = true;
                }
            }
        }
        return status;
    }
    private void addNested(SynchronizedStmt synS, int count, NodeList stmts, boolean inserted){

        SynchronizedStmt synS1 = new SynchronizedStmt();


        ObjectCreationExpr expression = new ObjectCreationExpr();
        expression.setType(new ClassOrInterfaceType("Object"));

        VariableDeclarator var = new VariableDeclarator();
        var.setName("insertedTestObj"+count+ RandomStringUtils.randomAlphanumeric(10));
        var.setType("Object");
        var.setInitializer(expression);

        ExpressionStmt expressionStmt = new ExpressionStmt();
        VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr();
        variableDeclarationExpr.addVariable(var);
        expressionStmt.setExpression(variableDeclarationExpr);
        synS1.setExpression(var.getNameAsExpression());
        synS.getBody().getStatements().add(expressionStmt);
        synS.getBody().getStatements().add(synS1);

        if(!inserted && (Math.random() < 0.3 || count == 0)){
            inserted = true;
            synS.getBody().getStatements().addAll(stmts);
        }
        if(count > 0) {
            addNested(synS1, --count,stmts,inserted);
        }
    }

}
