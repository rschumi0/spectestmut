package javamu.mutator.Mutations;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import javamu.ASTparser.MyNode;
import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class AddLabelledBlock extends Mutation {
    Random rand;

    public AddLabelledBlock(String type, String name, Properties parameters, List<Mutation> mutations) {
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
                    BlockStmt bs = new BlockStmt();
                    LabeledStmt ls = new LabeledStmt("b"+RandomStringUtils.randomAlphanumeric(10),bs);

                    //int nested = (int) getParameters().getOrDefault("nested",1);
                    int nested = rand.nextInt(11);
                    System.out.println("nested: " + nested);


                    if(getParameters().getProperty("move") != null){
                        ls.getStatement().asBlockStmt().getStatements().addAll(((BlockStmt)target.getNode()).getStatements());
                        ((BlockStmt)target.getNode()).getStatements().clear();
                    }

                    if(nested > 1)
                    {
                        List<String> outerBlocks = new ArrayList<>();
                        outerBlocks.add(ls.getLabel().toString());
                        addNested(ls, nested, (boolean)getParameters().getOrDefault("randomNestedBreak",false), outerBlocks, rand);
                    }

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
    private void addNested(LabeledStmt ls, int count, boolean randomBreakInsert, List<String> outerBlocks, Random r){
        BlockStmt bs = new BlockStmt();
        LabeledStmt ls1 = new LabeledStmt("b"+RandomStringUtils.randomAlphanumeric(10),bs);
        ls1.getStatement().asBlockStmt().getStatements().addAll(ls.getStatement().asBlockStmt().getStatements());
        ls.getStatement().asBlockStmt().getStatements().clear();
        ls.getStatement().asBlockStmt().getStatements().add(ls1);
        outerBlocks.add(ls.getLabel().toString());

        if(randomBreakInsert && r.nextInt(10) < 2){
            String label = outerBlocks.get(r.nextInt(outerBlocks.size()));
            JavaParser p = new JavaParser();
            ParseResult<Statement> s = p.parseStatement("break " + label +";");
            ls1.getStatement().asBlockStmt().addStatement(s.getResult().get());
            count =0;
        }
        if(count > 0) {
            addNested(ls1, --count,randomBreakInsert, outerBlocks, r);
        }
    }



}
