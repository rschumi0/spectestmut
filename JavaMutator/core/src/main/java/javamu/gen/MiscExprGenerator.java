package javamu.gen;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class MiscExprGenerator extends BoolExprGenerator {

    public MiscExprGenerator(Random r) {
        super(r);
    }

    public String generateConditionalOperatorString(ArrayList<String> vars, int numberOfVars, int numberOfLits, boolean includeBitOps) {

        int nesting = rand.nextInt(3) + 2;

        return generateConditionalOperatorStringNesting(vars,numberOfVars,numberOfLits,includeBitOps,nesting);
    }

    private String generateConditionalOperatorStringNesting(ArrayList<String> vars, int numberOfVars, int numberOfLits, boolean includeBitOps, int nesting) {
        String left = (nesting == 0 || rand.nextBoolean()) ? generateCompString(vars,numberOfVars,numberOfLits,includeBitOps) : generateConditionalOperatorStringNesting(vars,numberOfVars,numberOfLits,includeBitOps,nesting-1);
        String right = (nesting == 0 || rand.nextBoolean()) ? generateCompString(vars,numberOfVars,numberOfLits,includeBitOps) : generateConditionalOperatorStringNesting(vars,numberOfVars,numberOfLits,includeBitOps,nesting-1);
        return "(("+genBoolString(vars,2,2,includeBitOps)+") ? "+  left +" : "+ right+")";
    }

    public String generateCastString(ArrayList<String> vars, Map<String,String> types, String resVar){
        String castVar = "";
        for (String v: vars) {
            if(v != resVar){
                castVar = v;
                break;
            }
        }

        return "("+types.get(resVar)+") "+ castVar;
    }
}
