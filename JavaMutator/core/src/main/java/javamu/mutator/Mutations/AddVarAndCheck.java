package javamu.mutator.Mutations;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import javamu.ASTparser.MyNode;
import javamu.common.BasicGrammar;
import javamu.gen.BaseGenerator;
import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class AddVarAndCheck extends Mutation {
    public AddVarAndCheck(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(MyNode AST) {
        if(super.start(AST)) {
            Random r = new Random();
            for(String t : getAddTo()) {
                ArrayList<MyNode> targetBlocks = getNodeType(t, AST);
                for (MyNode target : targetBlocks) {

                    String type = getParameters().getOrDefault("type","rand").toString();
                    if(type.equals("rand")){
                        String[] types= { "String", "Object", "String", "Object","boolean", "float", "double", "int", "short", "long","String", "Object"};
                        type = types[r.nextInt(types.length)];
                    }

                    //String value = getParameters().getOrDefault("value","").toString();
                    String name = getParameters().getOrDefault("name", BasicGrammar.randomName()).toString();
                    if(name.equals("rand")){
                        name = "v"+RandomStringUtils.randomAlphanumeric(5);
                    }



                    //Boolean isvariable= (Boolean) getParameters().getOrDefault("variable",false);
                    String value = BaseGenerator.randomElement(type,r);





                    Statement tempLastStmt = null;
                    com.github.javaparser.ast.type.Type retType = null;
                    if(((BlockStmt)target.getNode()).getStatements().size() != 0)
                    {
                        Statement s = ((BlockStmt)target.getNode()).getStatements().get(((BlockStmt)target.getNode()).getStatements().size()-1);
                        if(s instanceof ReturnStmt)
                        {
                            System.out.println(s.toString());
                            try {
                                tempLastStmt = s.asReturnStmt();
                                //MethodDeclaration method = tempLastStmt.findAncestor(MethodDeclaration.class).get();
                                //retType = method.getType();
                                ((BlockStmt)target.getNode()).getStatements().remove(((BlockStmt)target.getNode()).getStatements().size()-1);
                            }
                            catch(Exception e)
                            {
                                tempLastStmt = null;
                            }
                        }
                    }

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

                    JavaParser p = new JavaParser();
                    String access = "System.out.println(\"\"+"+name + ");";

                    System.out.println(access);
                    ParseResult<Statement> s = p.parseStatement(access);
                    ((BlockStmt) target.getNode()).addStatement(s.getResult().get());

                    if(type.equals("String") || type.equals("Object")){
                        String access1 = "";

                            String code = "{System.out.println("+name + ".getClass());\nSystem.out.println("+name + ".toString());\nSystem.out.println("+name + ".hashCode());}";
                            ParseResult<BlockStmt> sb = p.parseBlock(code);
                            for (Statement temps : sb.getResult().get().getStatements()) {
                                ((BlockStmt) target.getNode()).addStatement(temps);
                            }


                    }


                    //System.out.println("######mutation node" + target.getCode());
                    if(tempLastStmt != null){
                        ((BlockStmt)target.getNode()).getStatements().add(tempLastStmt);
                        tempLastStmt = null;
                    }
                }
            }
            return true;
        }
        return false;
    }


}
