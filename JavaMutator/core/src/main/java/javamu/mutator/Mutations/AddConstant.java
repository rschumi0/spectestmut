package javamu.mutator.Mutations;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import javamu.ASTparser.MyNode;
import javamu.common.BasicGrammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddConstant extends Mutation {
    public AddConstant(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(MyNode AST) {
        if(super.start(AST)) {
            String type = getParameters().getOrDefault("type","uint").toString();
            String value = getParameters().getOrDefault("value","32").toString();
            String name = getParameters().getOrDefault("name", BasicGrammar.randomName()).toString();
            Boolean isvariable= (Boolean) getParameters().getOrDefault("variable",false);
            for(String t : getAddTo()) {
                ArrayList<MyNode> targetBlocks = getNodeType(t, AST);
                for (MyNode target : targetBlocks) {

                    //MyNode newNode = getMutauionNode();
                    //Set constant type
                /*newNode.getSubNodes().get(0).setName(type);
                //Set constant value
                newNode.getSubNodes().get(1).setName(value);

                VariableDeclarator variableDeclaration = (VariableDeclarator) newNode;
                variableDeclaration.setConstant(!isvariable);
                variableDeclaration.setName(name);*/
                    VariableDeclarator var = new VariableDeclarator();
                    var.setName(name);
                    var.setType(type);
                    var.setInitializer(value);
                    ExpressionStmt expressionStmt = new ExpressionStmt();
                    VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr();
                    variableDeclarationExpr.addVariable(var);
                    expressionStmt.setExpression(variableDeclarationExpr);

                    ((BlockStmt) target.getNode()).addStatement(expressionStmt);


                    System.out.println("######mutation node" + target.getCode());
                }
            }
            return true;
        }
        return false;
    }
}
