package javamu.mutator.Mutations;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.*;
import javamu.ASTparser.MyNode;
import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class AddWhile extends Mutation {
    Random rand;

    public AddWhile(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
        rand = new Random();
    }

    @Override
    public Boolean start(MyNode ast) {
        Boolean status = false;
        if(super.start(ast)){
            for(String type : getAddTo()) {
                //MyNode newNode = getMutauionNode();
                Statement tempFirstStmt = null;
                Statement tempLastStmt = null;
                com.github.javaparser.ast.type.Type retType = null;
                ArrayList<MyNode> targetBlocks = getNodeType(type, ast);
                for (MyNode target : targetBlocks) {
                    //newNode.getSubNodes().get(0).setName(getParameters().getProperty("condition"));
                    if(((BlockStmt)target.getNode()).getStatements().size() > 0 ){
                        Statement s =((BlockStmt)target.getNode()).getStatements().get(0);
                        if(s instanceof ExplicitConstructorInvocationStmt){
                            tempFirstStmt = s;
                            ((BlockStmt)target.getNode()).getStatements().remove(0);
                        }
                    }
                    if(((BlockStmt)target.getNode()).getStatements().size() != 0)
                    {
                        Statement s = ((BlockStmt)target.getNode()).getStatements().get(((BlockStmt)target.getNode()).getStatements().size()-1);
                        if(s instanceof ReturnStmt)
                        {
                            System.out.println(s.toString());
                            try {
                                tempLastStmt = s.asReturnStmt();
                                MethodDeclaration method = tempLastStmt.findAncestor(MethodDeclaration.class).get();
                                retType = method.getType();
                                ((BlockStmt)target.getNode()).getStatements().remove(((BlockStmt)target.getNode()).getStatements().size()-1);
                            }
                            catch(Exception e)
                            {
                                tempLastStmt = null;
                            }
                        }
                    }
                    //WhileStmt ws = new WhileStmt(null, new BooleanLiteralExpr(), new ReturnStmt());
                    JavaParser p = new JavaParser();
                    String looplabel = "b"+RandomStringUtils.randomAlphanumeric(10);
                    ParseResult<Statement> varInit = p.parseStatement("int " + looplabel + " = " + (rand.nextInt(3)+1) + ";");

                    ParseResult<Expression> ble = p.parseExpression(looplabel+"-- >0");
                    BlockStmt bs = new BlockStmt();
                    WhileStmt ws = new WhileStmt(null, ble.getResult().get(),bs);
                    LabeledStmt ls = new LabeledStmt(looplabel,ws);

                    //int nested = (int) getParameters().getOrDefault("nested",1);
                    int nested = rand.nextInt(3);
                    System.out.println("nested: " + nested);

                    System.out.println("move" +getParameters().getProperty("move"));
                    if(getParameters().getProperty("move") != null){
                        ((WhileStmt)ls.getStatement()).getBody().asBlockStmt().getStatements().addAll(((BlockStmt)target.getNode()).getStatements());
                        ((BlockStmt)target.getNode()).getStatements().clear();
                    }

                    List<String> outerBlocks = new ArrayList<>();
                    outerBlocks.add(ls.getLabel().toString());
                    addNested(ls, nested, (boolean)getParameters().getOrDefault("randomNestedBreak",true),(boolean)getParameters().getOrDefault("randomNestedContinue",true), outerBlocks, rand);

                    ((BlockStmt)target.getNode()).addStatement(varInit.getResult().get());
                    ((BlockStmt)target.getNode()).addStatement(ls);

                    if(tempFirstStmt != null){
                        ((BlockStmt)target.getNode()).getStatements().add(0,tempFirstStmt);
                        tempFirstStmt = null;
                    }
                    if(getParameters().getProperty("move") != null && tempLastStmt != null){
                        System.out.println(tempLastStmt.toString());

                        System.out.println(retType.toString());
                        String retString = "null";
                        if(retType.toString().equals("int")){
                            retString = "0";
                        }
                        else if(retType.toString().equals("boolean")){
                            retString = "true";
                        }
                        else if(retType.toString().equals("String")){
                            retString = "\"\"";
                        }
                        System.out.println(retString);


                        ((BlockStmt)target.getNode()).getStatements().add(new ReturnStmt(retString));
                        //((BlockStmt)target.getNode()).getStatements().add(tempLastStmt);
                        tempLastStmt = null;
                    }

                    status = true;
                }
            }
        }
        return status;
    }
    private void addNested(LabeledStmt ls, int count, boolean randomBreakInsert, boolean randomContinueInsert, List<String> outerBlocks, Random r){
        JavaParser p = new JavaParser();
        String looplabel = "b"+RandomStringUtils.randomAlphanumeric(10);
        LabeledStmt ls1 = null;
        if(count >= 0) {
        ParseResult<Statement> varInit = p.parseStatement("int " + looplabel + " = " + (r.nextInt(3)+1) + ";");

        BlockStmt bs = new BlockStmt();
        ParseResult<Expression> ble = p.parseExpression(looplabel+"-- >0");
        WhileStmt ws = new WhileStmt(null, ble.getResult().get(),bs);
        ls1 = new LabeledStmt(looplabel,ws);
        //System.out.println(((WhileStmt)ls1.getStatement()).getBody().toString() + " "+ ((WhileStmt)ls1.getStatement()).getBody().getClass().toString());
        ((WhileStmt)ls1.getStatement()).getBody().asBlockStmt().getStatements().addAll(((WhileStmt)ls.getStatement()).getBody().asBlockStmt().getStatements());
        ((WhileStmt)ls.getStatement()).getBody().asBlockStmt().getStatements().clear();

        ((WhileStmt)ls.getStatement()).getBody().asBlockStmt().getStatements().add(varInit.getResult().get());
        ((WhileStmt)ls.getStatement()).getBody().asBlockStmt().getStatements().add(ls1);
        }

        if(randomBreakInsert && r.nextInt(10) < 4){
            String label = outerBlocks.get(r.nextInt(outerBlocks.size()));
            ParseResult<Statement> s = p.parseStatement("break " + label +";");
            ((WhileStmt)ls.getStatement()).getBody().asBlockStmt().asBlockStmt().addStatement(s.getResult().get());
            //count =0;
        }
        else if(randomContinueInsert && r.nextInt(10) < 3){
            String label = outerBlocks.get(r.nextInt(outerBlocks.size()));
            ParseResult<Statement> s = p.parseStatement("continue " + label +";");
            ((WhileStmt)ls.getStatement()).getBody().asBlockStmt().asBlockStmt().addStatement(s.getResult().get());
        }

        if(count >= 0) {
            outerBlocks.add(looplabel);
            addNested(ls1, --count,randomBreakInsert,randomContinueInsert, outerBlocks, r);
        }
    }



}