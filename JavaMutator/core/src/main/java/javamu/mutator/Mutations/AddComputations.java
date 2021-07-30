package javamu.mutator.Mutations;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import javamu.ASTparser.MyNode;
import javamu.gen.MathExprGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;

import java.nio.charset.Charset;
import java.util.*;

public class AddComputations extends Mutation {
    public AddComputations(String type, String name, Properties parameters, List<Mutation> mutations) {
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



                 /*
                    Map<String,String> vars = new HashMap<>();
                    String[] types = { "float", "double", "int", "short", "long"};
                    for(int i = 0; i < r.nextInt(5)+2;i++) {

                        String type = getParameters().getOrDefault("type", "rand").toString();
                        if (type.equals("rand")) {
                            type = types[r.nextInt(types.length)];
                        }

                        //String value = getParameters().getOrDefault("value","").toString();
                        String name = getParameters().getOrDefault("name", BasicGrammar.randomName()).toString();
                        if (name.equals("rand")) {
                            name = "v" + RandomStringUtils.randomAlphanumeric(5);
                        }
                        //Boolean isvariable= (Boolean) getParameters().getOrDefault("variable",false);
                        String value = randomElement(type, r);
                        vars.put(name,type);

                        VariableDeclarator var = new VariableDeclarator();
                        var.setName(name);
                        var.setType(type);
                        var.setInitializer(value);


                        ExpressionStmt expressionStmt = new ExpressionStmt();
                        VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr();
                        variableDeclarationExpr.addVariable(var);
                        expressionStmt.setExpression(variableDeclarationExpr);

                        ((BlockStmt) target.getNode()).addStatement(expressionStmt);

                    }


                    String[] operators = new String[vars.size()];

                    for(int i = 0; i < vars.size()-1; i++)
                    {
                        String[] ops = { "+", "-", "*", "/", "%"};
                        operators[i] = ops[r.nextInt(ops.length)];
                    }

                    float asdfdf = 34;
                    double adfsadfsdaf = 0.8;
                    int asdf = (int) (asdfdf++/ adfsadfsdaf++ + 0.7);

                    String resName = "res" + RandomStringUtils.randomAlphanumeric(5);
                    String resName1 = "res" + RandomStringUtils.randomAlphanumeric(5);
                    String compString = "(";
                    int i =0;
                    for(String var : vars.keySet())
                    {
                        String prefix ="";
                        String postfix="";
                        if(r.nextInt(10) < 3){
                            var = randomElement(types[r.nextInt(types.length)],r);
                        }
                        else{
                            switch (r.nextInt(5)){
                                case 1: prefix = "++";break;
                                case 2: prefix = "--";break;
                                case 3: postfix = "++";break;
                                case 4: postfix = "--";break;
                                default: prefix= "";postfix="";
                            }
                        }
                        if(i == vars.size()-1){
                            compString += prefix+var+postfix + ");";
                        }
                        else{
                            compString += prefix+var+postfix + " " + operators[i] + " ";
                        }
                        i++;
                    }
                    JavaParser p = new JavaParser();
                    String access = "{int " +resName + "= (int) "+compString+ "\n";
                    System.out.println(access );
                    access += "double " +resName1 + "= (double) "+compString +"\n";
                    System.out.println(access);
                    access += "System.out.println(\"\"+"+resName + ");\n";
                    access += "System.out.println(\"\"+"+resName1 + ");}\n";

                    for (String var: vars.keySet()) {
                        access += "System.out.println(\""+var+" \" +("+var + "));\n";
                    }

                    System.out.println(access);
                    ParseResult<BlockStmt> s = p.parseBlock(access);
                    for (Statement temps : s.getResult().get().getStatements()) {
                        ((BlockStmt) target.getNode()).addStatement(temps);
                    }*/

                    boolean includeBitOps = false;
                    JavaParser p = new JavaParser();
                    String code ="";
                    Map<String, String> types;
                    Map<String, String> vals;
                    Map<String, Statement> vars;
                    ArrayList<String> varNames;
                    String resName;
                    do {
                        vals = new HashMap<>();
                        types = new HashMap<>();
                        vars = new MathExprGenerator(r).genVarsWithInitStatement(3, vals, types, includeBitOps);
                        varNames = new ArrayList<>(vars.keySet());
                        resName = varNames.get(r.nextInt(varNames.size()));
                    } while (!types.containsKey(resName) || types.get(resName).equals("short"));

                    for(String k : vars.keySet()){
                        ((BlockStmt)target.getNode()).getStatements().add(vars.get(k));
                    }


                    String compString = new MathExprGenerator(r).generateCompString(varNames,3,2,includeBitOps);
                    //String compString = new MiscExprGenerator(r).generateConditionalOperatorString(varNames,3,2,includeBitOps);

                    //String resName = "res" + RandomStringUtils.randomAlphanumeric(5);
                    //String resName1 = "res" + RandomStringUtils.randomAlphanumeric(5);

                    String[] assignOps = new String[]{"=", "+=", "-=", "*=", "/="};//, "%="};//, "<<=", ">>=", "&=", "^=", "|="};

                    if(includeBitOps) {
                        assignOps = new String[]{"=", "+=", "-=", "*=", "/=", "<<=", ">>=", "&=", "^=", "|="};//"%=",
                    }
                    //code +="{int " +resName + "= (int) "+compString+ ";\n";

                    code += "{ " +resName +" " +assignOps[r.nextInt(assignOps.length)]+ " "+compString +";\n";

                    //code += "{int " +resName1 + "=" + BaseGenerator.intGen(r) + ";\n" +resName1 +" " +assignOps[r.nextInt(assignOps.length)]+ " (int) "+compString +";\n";
                    code += "System.out.println(\"\"+"+resName + ");\n";
                    //code += "System.out.println(\"\"+"+resName1 + ");\n";



                    for (String var: varNames) {
                        code += "System.out.println(\""+var+" \" +("+var +" )"+ "+\" oV: \"+"+vals.get(var)+");";
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

//    private String varToString(String var, String type){
//        if(type == null){
//            return var;
//        }
//        switch (type){
//            case "int": return "new Integer("+var+").toString()";
//            case "boolean": return "new Boolean("+var+").toString()";
//            case "float": return "new Float("+var+").toString()";
//            case "double": return "new Double("+var+").toString()";
//            case "long": return "new Long("+var+").toString()";
//            case "short": return "new Short("+var+").toString()";
//            default: return var;
//        }
//    }


    private String randomElement(String type,Random rand){
        switch (type){
            //case "int": return ""+rand.nextInt();
            case "String": return stringGen(rand);
            case "boolean": return ""+rand.nextBoolean();
            case "float": return floatGen(rand);
            case "double": return doubleGen(rand);
            case "long": return longGen(rand);
            case "short": return shortGen(rand);
            default: return intGen(rand);
        }
    }

    private String intGen(Random r){
        switch(r.nextInt(4)){
            case 1: return "0b"+Integer.toBinaryString(r.nextInt());
            case 2: return "0x"+Integer.toHexString(r.nextInt());
            case 3: return "0"+Integer.toOctalString(r.nextInt());
            default: return ""+r.nextInt();
        }
    }
    private String longGen(Random r){
        switch(r.nextInt(4)){
            case 1: return "0b"+Long.toBinaryString(r.nextLong()) + "L";
            case 2: return "0x"+Long.toHexString(r.nextLong())+ "L";
            case 3: return "0"+Long.toOctalString(r.nextLong())+ "L";
            default: return ""+r.nextLong()+ "L";
        }
    }
    private String shortGen(Random r){
        switch(r.nextInt(4)){
            case 1: return "0b"+Integer.toBinaryString(r.nextInt(Short.MAX_VALUE));
            case 2: return "0x"+Integer.toHexString(r.nextInt(Short.MAX_VALUE));
            case 3: return "0"+Integer.toOctalString(r.nextInt(Short.MAX_VALUE));
            default: return ""+r.nextInt(Short.MAX_VALUE);
        }
    }
    private String doubleGen(Random r){
        switch(r.nextInt(4)){
            case 1: return "0b"+Integer.toBinaryString(r.nextInt());
            case 2: return Double.toHexString(r.nextInt());
            case 3: return "0"+Integer.toOctalString(r.nextInt());
            default: return ""+r.nextDouble();
        }
    }
    private String floatGen(Random r){
        switch(r.nextInt(4)){
            case 1: return "0b"+Integer.toBinaryString(r.nextInt());
            case 2: return Float.toHexString(r.nextFloat())+"f";
            case 3: return "0"+Integer.toOctalString(r.nextInt())+"f";
            default: return ""+r.nextFloat()+"f";
        }
    }

    private String stringGen(Random r){
        switch(r.nextInt(2)){
            case 1:  byte[] array = new byte[10]; // length is bounded by 7
            new Random().nextBytes(array);
            String generatedString = new String(array, Charset.forName("UTF-8"));
            return "\""+ StringEscapeUtils.escapeJava(generatedString)+"\"";
            default:  return "\""+RandomStringUtils.randomAlphanumeric(r.nextInt(10)+1)+"\"";
        }
    }

}
