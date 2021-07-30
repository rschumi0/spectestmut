package javamu.gen;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import org.apache.commons.lang.RandomStringUtils;

import java.util.*;

public class MathExprGenerator extends BaseGenerator {



    public MathExprGenerator(Random r) {
        super(r);
    }

    public String generateCompString(boolean includeBitOps) {
        return generateCompString(new ArrayList<>(), rand.nextInt(3) + 2, rand.nextInt(3) + 2, includeBitOps);
    }

    public String generateCompString(ArrayList<String> vars, int numberOfVars, int numberOfLits, boolean includeBitOps) {

        String[] ops = {"+", "-", "*", "/"};    //, "%"};
        //String[] bitOps = {"&", "|", "~", "^", "<<", ">>", ">>>"};
        String[] types = {"float", "double", "int", "short", "long"};
        if(includeBitOps){
            types = new String[]{"int", "short", "long"};
            ops = new String[]{"/","&", "|", "^", "<<", ">>", ">>>", "+", "-", "*", "/","&", "|", "^", "<<", ">>", ">>>"};//"~",
        }

        String ret = "";

        if(vars.size() == 0){
            numberOfLits = numberOfLits + numberOfVars;
            numberOfVars = 0;
        }

        while (numberOfVars + numberOfLits > 0) {
            boolean addVar = false;
            if (numberOfVars > 0 && numberOfLits > 0) {
                addVar = rand.nextBoolean();
            } else if (numberOfVars > 0) {
                addVar = true;
            }
            String op = ops[rand.nextInt(ops.length)];
            if (addVar) {
                String var = vars.get(rand.nextInt(vars.size()));
                String prefix = "";
                String postfix = "";
                switch (rand.nextInt(5)) {
                    case 1:
                        prefix = "++";
                        break;
                    case 2:
                        prefix = "--";
                        break;
                    case 3:
                        postfix = "++";
                        break;
                    case 4:
                        postfix = "--";
                        break;
                    default:
                        prefix = "";
                        postfix = "";
                }
                //ret +=  " (int) "+computeUnaryOps(prefix, includeBitOps)+ prefix + var + postfix;
                ret +=  " "+computeUnaryOps(prefix, includeBitOps)+ prefix + var + postfix;
                numberOfVars--;
            } else {
                String val =  randomElement(types[rand.nextInt(types.length)], rand);
               // ret += " (int) "+computeUnaryOps(val,includeBitOps)+ val;
                ret += " "+computeUnaryOps(val,includeBitOps)+ val;
                numberOfLits--;
            }
            if (numberOfVars + numberOfLits != 0) {
                ret += " " + op + " ";
            }
        }
System.out.println("ret: "+ret);
        return ret;
    }

    private String computeUnaryOps(String prefix, boolean includeBitOps){
        String[] unaryOps = new String[] {"-","+"};
        if(includeBitOps){
            unaryOps = new String[] {"~","-","+"};
        }
        int unaryOpNumb = rand.nextInt(6);
        String unaryPrefix = "";
//        for (int i = 0; i < unaryOpNumb; i++){
//            String unOp = "";
//            if((i==0 && prefix.startsWith("-")) || unaryPrefix.startsWith("-")){
//                unOp = includeBitOps ?  new String[] {"~","+"}[rand.nextInt(2)] : "+";
//            }
//            else if((i==0 && prefix.startsWith("+")) || unaryPrefix.startsWith("+")){
//                unOp = includeBitOps ? new String[] {"~","-"}[rand.nextInt(2)] : "-";
//            }
//            else {
//                unOp = unaryOps[rand.nextInt(unaryOps.length)];
//            }
//            unaryPrefix = unOp+unaryPrefix;
//        }
        return unaryPrefix;
    }

    public Map<String, Statement> genVarsWithInitStatement(int MaxNumber, Map<String, String> values,Map<String, String> varTypes, boolean includeBitOps){
        return genVarsWithInitStatement(MaxNumber,values,varTypes,includeBitOps,false);
    }
    public Map<String, Statement> genVarsWithInitStatement(int MaxNumber, Map<String, String> values,Map<String, String> varTypes, boolean includeBitOps, boolean includeByteChar){
        Map<String,Statement> vars = new HashMap<>();
        String[] types = { "double", "int", "short", "long"};//"float",
        if(includeBitOps) {
            types = new String[]{"int", "short", "long"};
        }
        if(includeByteChar){
            types = new String[]{ "double", "int", "short", "long", "float", "byte", "char"};
        }
        for(int i = 0; i < rand.nextInt(MaxNumber)+2;i++) {

            boolean isArray = false;//rand.nextBoolean(); //TODO Turn back on
            int nesting = rand.nextInt(2) + 1;
            String type = types[rand.nextInt(types.length)];
            String name = "v" + RandomStringUtils.randomAlphanumeric(5);

            //Boolean isvariable= (Boolean) getParameters().getOrDefault("variable",false);

            VariableDeclarator var = new VariableDeclarator();
            String value = randomElement(type, rand);
            String access = "";
            if(isArray && !type.equals("short")){
                Map<Integer,Integer> nestedArraySizes = new HashMap<>();
                value = nestedArrayConstruction(type,nesting,rand,nestedArraySizes);
                for(int j = 0; j < nesting; j++){
                    type+="[]";
                }
                int randomAccessInserts =  1;//rand.nextInt(1)+1;
                for(int k = 0; k < randomAccessInserts; k++){
                    access = name;//"System.out.println("+name;

                    List<Integer> sizes = new ArrayList<>();
                    sizes.addAll(nestedArraySizes.keySet());
                    Collections.sort(sizes, Collections.reverseOrder());
                    for(int j = 0; j < sizes.size(); j++){
                        int index = nestedArraySizes.get(sizes.get(j));
                        access += "["+rand.nextInt(index)+"]";
                    }
                    //access += ");";

                    System.out.println(access);
                }

            }
            var.setName(name);
            var.setType(type);

            var.setInitializer(value);

            if(values != null){
                values.put(name,value);
            }
            if(varTypes !=null)
            {
                varTypes.put(name,type);
            }

            ExpressionStmt expressionStmt = new ExpressionStmt();
            VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr();
            variableDeclarationExpr.addVariable(var);
            expressionStmt.setExpression(variableDeclarationExpr);

            //((BlockStmt) target.getNode()).addStatement(expressionStmt);
            if(isArray && !type.equals("short")){
                vars.put(access,expressionStmt);
            }
            else
            {
                vars.put(name,expressionStmt);
            }

        }



        return vars;
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
                String val;
                do {
                    val = randomElement(type, rand);
                } while(val.startsWith("0"));
                ret += val + ",";
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


}
