package javamu.mutator.Mutations;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import javamu.ASTparser.MyNode;
import javamu.gen.MathExprGenerator;
import javamu.gen.BoolExprGenerator;

import java.util.*;

public class AddConditions extends Mutation {
    public AddConditions(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(MyNode AST) {
        if(super.start(AST)) {
            Random r = new Random();
            for(String t : getAddTo()) {
                ArrayList<MyNode> targetBlocks = getNodeType(t, AST);
                for (MyNode target : targetBlocks) {


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


                    JavaParser p = new JavaParser();
                    String code ="";

                    Map<String, Statement> vars = new MathExprGenerator(r).genVarsWithInitStatement(3, null, null, false);

                    for(String k : vars.keySet()){
                        ((BlockStmt)target.getNode()).getStatements().add(vars.get(k));
                    }
                    ArrayList<String> varNames = new ArrayList<>(vars.keySet());

                    String cond = new BoolExprGenerator(r).genBoolString(varNames,3,3,false);
                    code += "{System.out.println(\""+cond + "\" +( "+cond+") );";

                    for (String var: varNames) {
                        code += "System.out.println(\""+var+" \" +("+var + "));";
                    }

                    code += "System.out.println(\"done\");}";

                    System.out.println(code);
                    ParseResult<BlockStmt> s = p.parseBlock(code);
                    for (Statement temps : s.getResult().get().getStatements()) {
                        ((BlockStmt) target.getNode()).addStatement(temps);
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
