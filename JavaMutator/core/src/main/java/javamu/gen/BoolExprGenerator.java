package javamu.gen;

import java.util.ArrayList;
import java.util.Random;

public class BoolExprGenerator extends MathExprGenerator {


    public BoolExprGenerator(Random r) {
        super(r);
    }

    public String genBoolString(boolean includeBitOps){
        return genBoolString(new ArrayList<>(), rand.nextInt(3)+2,rand.nextInt(3)+2,includeBitOps);
    }

    public String genBoolString(ArrayList<String> vars, int MaxNumberOfSubExpr, int MaxSubExpressionSize, boolean includeBitOps){

        String[] combinationOps = {"||","&&"};
        String ret = "";

        int subExpr = rand.nextInt(MaxNumberOfSubExpr)+1;
        for(int i = 0; i < subExpr;i++){
            int subSize = rand.nextInt(MaxSubExpressionSize)+1;
            String expr = "";
            if(subSize == 1){
                expr = ""+rand.nextBoolean();
            }
            else{
                expr = genComparison(vars,subSize, includeBitOps);
            }
            if(i < subExpr-1) {
                ret += expr + " " + combinationOps[rand.nextInt(combinationOps.length)];
            }
            else{
                ret += expr;
            }
        }
        return ret;
    }

    public String genComparison(ArrayList<String> vars, int maxSize, boolean includeBitOps){
        String ret = "";
        String[] compareOps = {"<",">","<=",">=", "!=", "=="};
        int left = rand.nextInt(maxSize)+1;
        int right = rand.nextInt(maxSize)+1;

        int leftVars = rand.nextInt(left)+1;
        int leftLit = left - leftVars;
        String leftStr = generateCompString(vars,leftVars,leftLit, includeBitOps);

        int rightVars = rand.nextInt(right)+1;
        int rightLit = right - rightVars;
        String rightStr = generateCompString(vars,rightVars,rightLit,includeBitOps);
        System.out.println(leftStr);
        System.out.println(rightStr);
        System.out.println(left + " "+ leftVars +" "+leftLit);
        System.out.println(right+ " " + rightVars + " "+ rightLit);
        String op = compareOps[rand.nextInt(compareOps.length)];
        return leftStr + " " + op + " "+ rightStr;
    }


}
