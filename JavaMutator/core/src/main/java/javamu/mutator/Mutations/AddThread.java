package javamu.mutator.Mutations;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.apache.commons.lang.RandomStringUtils;
import javamu.ASTparser.MyNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class AddThread extends Mutation {

    public AddThread(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(MyNode AST) {
        if(super.start(AST)) {
            Random r = new Random();


            for(String t : getAddTo()) {
                ArrayList<MyNode> targetBlocks = getNodeType(t, AST);
                for (MyNode target : targetBlocks) {

                    String type = "Thread";

                    String value = "new Thread()";
                    String name = "t"+RandomStringUtils.randomAlphanumeric(5);
                    Boolean isvariable= false;




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
                    String code ="";
                    if(r.nextBoolean() == true) {
                        code ="{" + name + ".start();\n"+name+".interrupt();}";
                    }
                    else
                    {
                        code ="{" + name + ".start();\n try{\n"+name+".wait();\n}catch(Exception e){System.out.println(e);}}";
                    }

                    System.out.println(code);
                    ParseResult<BlockStmt> s = p.parseBlock(code);
                    for (Statement temps : s.getResult().get().getStatements()) {
                        ((BlockStmt) target.getNode()).addStatement(temps);
                    }



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
