package solmu.gen;

import org.apache.commons.lang.RandomStringUtils;
import solmu.grammer.Node;
import solmu.grammer.VariableDeclaration;
import solmu.mutator.Mutations.Mutation;

import java.math.BigInteger;
import java.util.*;

public class MathExprGenerator extends BaseGenerator {

    protected BigInteger compRes = BigInteger.ZERO;

    public MathExprGenerator(Random r) {
        super(r);
        compRes = BigInteger.ZERO;
    }
    public void reset(Map<String,BigInteger> tmpVarValues){
        if(tmpVarValues != null){
            varValues = tmpVarValues;
        }
        //super.reset();
        compRes = BigInteger.ZERO;
    }

    public String generateCompString() {
        return generateCompString(new HashMap<String,String>(), rand.nextInt(3) + 2, rand.nextInt(3) + 2);
    }

    public String generateCompString(Map<String,String> vars, int numberOfVars1, int numberOfLits1) {
        String ret = "";
        String[] types = {"int", "uint"};
        while (true) {
            HashMap<String,BigInteger> tmpVarValues = new HashMap<>();
            for (String v: varValues.keySet()) {
                tmpVarValues.put(v,varValues.get(v));
            }
            ret = "";
            int numberOfLits = numberOfLits1;
            int numberOfVars = numberOfVars1;
            String typeprefix = types[rand.nextInt(types.length)];
            ArrayList<String> usedOps = new ArrayList<>();
            ArrayList<BigInteger> usedValues = new ArrayList<>();
            ArrayList<Integer> usedBits = new ArrayList<>();

            if (!vars.isEmpty()) {
                Map.Entry<String, String> entry = vars.entrySet().iterator().next();
                String value = entry.getValue();
                if (value.startsWith("int")) {
                    typeprefix = "int";
                } else {
                    typeprefix = "uint";
                }
            }

            ArrayList<String> ops = new ArrayList<>(Arrays.asList("+","-", "*", "/", "%", "&", "|","<<", ">>"));//"/", "%"));
            if (typeprefix.equals("uint")) {
                ops.add("**");// TODO: turn on again
            }


            if (vars.size() == 0) {
                numberOfLits = numberOfLits + numberOfVars;
                numberOfVars = 0;
            }

            ArrayList<String> varNames = new ArrayList<String>(vars.keySet());
            while (numberOfVars + numberOfLits > 0) {
                boolean addVar = false;
                if (numberOfVars > 0 && numberOfLits > 0) {
                    addVar = rand.nextBoolean();
                } else if (numberOfVars > 0) {
                    addVar = true;
                }
                String op = ops.get(rand.nextInt(ops.size()));


                if (addVar) {
                    String var = varNames.get(rand.nextInt(varNames.size()));
                    varNames.remove(var);//TODO remove once increment issue is fixed
                    String prefix = "";
                    String postfix = "";
                    switch (rand.nextInt(5)) {
//                    case 1:
//                        prefix = "++";
//                        incDec(typeprefix,var,prefix);
//                        usedValues.add(varValues.get(var));
//                        break;
//                    case 2:
//                        prefix = "--";
//                        incDec(typeprefix,var,prefix);
//                        usedValues.add(varValues.get(var));
//                        break;
//                    case 3:
//                        postfix = "++";
//                        usedValues.add(varValues.get(var));
//                        incDec(typeprefix,var,postfix);
//                        break;
//                    case 4:
//                        postfix = "--";
//                        usedValues.add(varValues.get(var));
//                        incDec(typeprefix,var,postfix);
//                        break;
                        default:
                            usedValues.add(varValues.get(var));
                            usedBits.add(varBits.get(var));
                            prefix = "";
                            postfix = "";
                    }
                    ret +=computeUnaryOps(usedValues,typeprefix)+prefix + var + postfix;//
                    //ret +=computeUnaryOps(usedValues,typeprefix)+typeprefix + "(" + prefix + var + postfix + ")";//prefix + var + postfix;//
                    numberOfVars--;
                } else {
//                if(op.equals("/")){
                    String lit = randomElement(intUintTypeGen(typeprefix));
                    //System.out.println(lit);
                    if (lit.contains("0x")) {
                        usedValues.add(new BigInteger(lit.replace("0x", ""), 16));
                    } else {
                        usedValues.add(new BigInteger(lit));
                    }
                    usedBits.add(0);
                    ret += computeUnaryOps(usedValues,typeprefix)+lit;//
                    //ret += computeUnaryOps(usedValues,typeprefix)+typeprefix + "(" + lit + ")";//+lit;//
//                }
//                else
//                {
//                    ret += randomElement(intUintTypeGen(typeprefix));
//                }

                    numberOfLits--;
                }
                if (numberOfVars + numberOfLits != 0) {
                    usedOps.add(op);
                    ret += " " + op + " ";
                }
            }
            System.out.println(ret);
            boolean success = calcCompRes(usedValues, usedBits, usedOps, typeprefix);
            if(!success){
                reset(tmpVarValues);
                continue;
            }
            else {
                break;
            }
        }
        return ret;
    }

    private String computeUnaryOps(ArrayList<BigInteger> usedValues,String typeprefix){
        String[] unaryOps = new String[] {"~","-"};
        int unaryOpNumb = rand.nextInt(4);
        String unaryPrefix = "";

//        for (int i = 0; i < unaryOpNumb; i++){
//            String unOp = "";
//            if(unaryPrefix.startsWith("-")){
//                unOp ="~";
//            }
//            else {
//                unOp = unaryOps[rand.nextInt(unaryOps.length)];
//            }
//            if(unOp.equals("~")){
//                usedValues.set(usedValues.size()-1,trimBits(usedValues.get(usedValues.size()-1).not(),256,typeprefix));
//            }
//            if(unOp.equals("-")){
//                usedValues.set(usedValues.size()-1,trimBits(usedValues.get(usedValues.size()-1).negate(),256,typeprefix));
//            }
//            unaryPrefix = unOp+unaryPrefix;
//        }
        return unaryPrefix;
    }

    private int getCurrentBits(ArrayList<Integer> bits, int i){
        return getCurrentBits(bits, i, false);
    }

    private int getCurrentBits(ArrayList<Integer> bits, int i, boolean useOnlyFirst){
        int current = bits.get(i);
        if(current == 0){
            if(i+1 >= bits.size()){
                bits.remove(i);
                return 256;
            }
            current = bits.get(i+1);
            bits.remove(i+1);
            if(current == 0){
                return 256;
            }
            return current;
        }
        else if(!useOnlyFirst && i+1 < bits.size()){
            int nextBit = bits.get(i+1);
            current = Math.max(current, nextBit);
            bits.set(i+1, current);
        }
        bits.remove(i);
        return current;
    }

    public boolean calcCompRes(ArrayList<BigInteger> vals, ArrayList<Integer> bits, ArrayList<String> ops, String type)
    {
        assert(vals.size() > ops.size());
        System.out.println(vals.size() + " " + ops.size());
        for(int i = 0; i < ops.size();i++){
            if(ops.get(i).equals("**")) {
                BigInteger tempRes = vals.get(i).modPow(vals.get(i+1), BigInteger.ONE.add(BigInteger.ONE).pow(256));
                ops.remove(i);
                vals.set(i,trimBits(tempRes,getCurrentBits(bits,i,true),type));
                vals.remove(i+1);
                i--;
            }
        }
        for(int i = 0; i < ops.size();i++){
            BigInteger tempRes = BigInteger.ZERO;
            if(ops.get(i).equals("*")) {
                tempRes = vals.get(i).multiply(vals.get(i+1));
            }
            else if(ops.get(i).equals("/")){
                if(vals.get(i+1).equals(BigInteger.ZERO)){
                    return false;
                }
                tempRes = vals.get(i).divide(vals.get(i+1));
            }
            else if(ops.get(i).equals("%")){
                //tempRes = vals.get(i).mod(vals.get(i+1));
                if(vals.get(i+1).signum() == -1){
                    BigInteger bi =vals.get(i+1).negate();
                    if(vals.get(i).signum() == -1) {
                        tempRes = vals.get(i).negate().mod(bi);
                        tempRes = tempRes.negate();
                    }
                    else
                    {
                        tempRes = vals.get(i).mod(bi);
                    }


//                    if(vals.get(i).signum() == -1) {
//                        tempRes = tempRes.negate();
//                    }
                    System.out.println(vals.get(i).toString() +" %  " +vals.get(i+1).toString() + " = "+ tempRes);
                }
                else if(vals.get(i+1).signum() == 1){
                    if(vals.get(i).signum() == -1) {
                        tempRes = vals.get(i).negate().mod(vals.get(i+1));
                        tempRes = tempRes.negate();
                    }
                    else
                    {
                        tempRes = vals.get(i).mod(vals.get(i+1));
                    }
                    System.out.println(vals.get(i).toString() +" %  " +vals.get(i+1).toString() + " = "+ tempRes);
                }
                else{
                    return false;
                }
            }
            else{
                continue;
            }
            ops.remove(i);
            vals.set(i,trimBits(tempRes,getCurrentBits(bits,i),type));
            vals.remove(i+1);
            i--;
        }
        for(int i = 0; i < ops.size();i++){
            BigInteger tempRes = BigInteger.ZERO;
            if(ops.get(i).equals("+")) {
                tempRes = vals.get(i).add(vals.get(i+1));
            }
            else if(ops.get(i).equals("-")) {
                tempRes = vals.get(i).subtract(vals.get(i+1));
            }
            else {
                continue;
            }
            ops.remove(i);
            vals.set(i,trimBits(tempRes,getCurrentBits(bits,i),type));
            vals.remove(i+1);
            i--;
        }

        for(int i = 0; i < ops.size();i++){
            BigInteger tempRes = BigInteger.ZERO;
            if(ops.get(i).equals("<<")) {
                if(vals.get(i+1).signum() == -1){
                    return false;
                }
                else if(vals.get(i+1).compareTo(new BigInteger("256"))==1){
                    tempRes = BigInteger.ZERO;
                }
                else {
                    System.out.println("shift" + vals.get(i+1));
                    tempRes = vals.get(i).shiftLeft(vals.get(i + 1).intValue());
                }
            }
            else if(ops.get(i).equals(">>")) {
                if(vals.get(i+1).signum() == -1){
                    return false;
                }
                else if(vals.get(i+1).compareTo(new BigInteger("256"))==1){
                    tempRes = BigInteger.ZERO;
                    if(vals.get(i).signum() == -1){
                        tempRes = BigInteger.ONE.negate();
                    }
                }
                else {
                    System.out.println("shift" + vals.get(i+1));
                    tempRes = vals.get(i).shiftRight(vals.get(i + 1).intValue());
                }
            }
            else {
                continue;
            }
            ops.remove(i);
            vals.set(i,trimBits(tempRes,getCurrentBits(bits,i,true),type));
            vals.remove(i+1);
            i--;
        }
        for(int i = 0; i < ops.size();i++){
            if(ops.get(i).equals("&")) {
                BigInteger tempRes = vals.get(i).and(vals.get(i+1));
                ops.remove(i);
                vals.set(i,trimBits(tempRes,getCurrentBits(bits,i),type));
                vals.remove(i+1);
                i--;
            }
        }
        for(int i = 0; i < ops.size();i++){
            if(ops.get(i).equals("^")) {
                BigInteger tempRes = vals.get(i).xor(vals.get(i+1));
                ops.remove(i);
                vals.set(i,trimBits(tempRes,getCurrentBits(bits,i),type));
                vals.remove(i+1);
                i--;
            }
        }
        for(int i = 0; i < ops.size();i++){
            if(ops.get(i).equals("|")) {
                BigInteger tempRes = vals.get(i).or(vals.get(i+1));
                ops.remove(i);
                vals.set(i,trimBits(tempRes,getCurrentBits(bits,i),type));
                vals.remove(i+1);
                i--;
            }
        }
        compRes = vals.get(0);
        return true;
    }

    public void incDec(String typePrefix, String var, String op){

        if(op.equals("++")){
            varValues.put(var,trimBits(varValues.get(var).add(BigInteger.ONE),varBits.get(var),typePrefix));
        }
        else if(op.equals("--")){
            //if(!(typePrefix.equals("uint") && varValues.get(var).compareTo(BigInteger.ZERO)!=1)){
                varValues.put(var,trimBits(varValues.get(var).subtract(BigInteger.ONE),varBits.get(var),typePrefix));
            //}
        }
    }


    public Map<String, String> genVarsWithInitStatement(int MaxNumber, Node addToBlock) {
        String[] types = {"int", "uint"};
        String typePrefix = types[rand.nextInt(types.length)];
        return genVarsWithInitStatement(MaxNumber,addToBlock,typePrefix);
    }

    public Map<String, String> genVarsWithInitStatement(int MinNumber, Node addToBlock, String typePrefix){
        Map<String,String> vars = new HashMap<>();
        for(int i = 0; i < rand.nextInt(3)+MinNumber;i++) {

            String type = intUintTypeGen(typePrefix);
            String name = "v" + RandomStringUtils.randomAlphanumeric(5);

            //Boolean isvariable= (Boolean) getParameters().getOrDefault("variable",false);
            String value = randomElement(name,type);

            Node newNode = Mutation.getMutauionNode("AddConstant");
            newNode.getSubNodes().get(0).setName(type);
            newNode.getSubNodes().get(1).setName(value);

            VariableDeclaration variableDeclaration = (VariableDeclaration) newNode;
            variableDeclaration.setConstant(false);
            variableDeclaration.setName(name);
            vars.put(name,type);
            addToBlock.addSubNode(variableDeclaration);
        }
        return vars;
    }


    public BigInteger getCompRes() {
        return compRes;
    }

    public void setCompRes(BigInteger compRes) {
        this.compRes = compRes;
    }
}
