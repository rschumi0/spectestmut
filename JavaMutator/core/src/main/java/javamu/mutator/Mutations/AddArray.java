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
import org.apache.commons.lang.RandomStringUtils;

import java.util.*;

public class AddArray extends Mutation {
    public AddArray(String type, String name, Properties parameters, List<Mutation> mutations) {
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
                        String[] types= {"String", "boolean", "float", "double", "int"};
                        type = types[r.nextInt(types.length)];
                    }

                    //String value = getParameters().getOrDefault("value","").toString();
                    String name = getParameters().getOrDefault("name", BasicGrammar.randomName()).toString();
                    if(name.equals("rand")){
                        name = "a"+RandomStringUtils.randomAlphanumeric(5);
                    }


                    int nested = (int) getParameters().getOrDefault("nested",0);
                    if(nested == 0)
                    {
                        nested = r.nextInt(10)+1;
                    }

                    Boolean isvariable= (Boolean) getParameters().getOrDefault("variable",false);
                    Map<Integer,Integer> nestedArraySizes = new HashMap<>();
                    String value = nestedArrayConstruction(type, nested,r,nestedArraySizes);


                    //String access = "System.out.println("+name;
                    for (int i=0; i < nested; i++){
                        type += "[]";
                       // access += "[0]";
                    }
                    //access += ");";

                    int randomAccessInserts = (int) getParameters().getOrDefault("randomAccessInserts",0);




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
                    for(int i = 0; i < randomAccessInserts; i++){
                        String access = "System.out.println("+name;

                        List<Integer> sizes = new ArrayList<>();
                        sizes.addAll(nestedArraySizes.keySet());
                        Collections.sort(sizes, Collections.reverseOrder());
                        for(int j = 0; j < sizes.size(); j++){
                            int index = nestedArraySizes.get(sizes.get(j));
                            access += "["+r.nextInt(index)+"]";
                        }
                        access += ");";

                        System.out.println(access);
                        ParseResult<Statement> s = p.parseStatement(access);
                        ((BlockStmt) target.getNode()).addStatement(s.getResult().get());
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

    String nestedArrayConstruction(String type, int nesting, Random rand, Map<Integer,Integer> nestedArraySizes){
        String ret = "";
        int elementCnt = rand.nextInt((3 - 1) + 1)  + 1;
        if(nestedArraySizes.containsKey(nesting)){
            nestedArraySizes.put(nesting,Math.min(nestedArraySizes.get(nesting),elementCnt));
        }
        else {
            nestedArraySizes.put(nesting, elementCnt);
        }
        nesting = nesting - 1;
        if(nesting == 0) {
            for(int i = 0; i < elementCnt;i++){
                ret +=  randomElement(type, rand) + ",";
            }
        }
        else if(nesting > 0){
            for(int i = 0; i < elementCnt;i++){
                ret +=  nestedArrayConstruction(type,nesting,rand, nestedArraySizes) + ",";
            }
        }
        else{
            assert false;
        }

        return "{"+ret.substring(0,ret.length()-1)+"}";
    }

    private String randomElement(String type,Random rand){
        switch (type){
            //case "int": return ""+rand.nextInt();
            case "String": return "\""+RandomStringUtils.randomAlphanumeric(rand.nextInt(3)+1)+"\"";
            case "boolean": return ""+rand.nextBoolean();
            case "float": return ""+rand.nextFloat()+"f";
            case "double": return ""+rand.nextDouble();
            default: return ""+rand.nextInt();
        }
    }
}
