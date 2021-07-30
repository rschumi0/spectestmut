package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ForStatement extends Node{
    private String loopBreak = " ; ";
    private Boolean condition = true;
    private Boolean initializationExpression = true;
    private Boolean loopExpression = true;

    public ForStatement(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);

        if (getAttributes() != null) {
            this.condition = !getAttributes().keySet().contains("condition");
            this.initializationExpression = !getAttributes().keySet().contains("initializationExpression");
            this.loopExpression =  !getAttributes().keySet().contains("loopExpression");
        }
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = "for (";
        int i = 0;
        if (initializationExpression) {
            getSubNodes().get(i).setEndStament(false);
            code = code + getSubNodes().get(i).getCode();
            i++;
        }
        code += loopBreak;
        if (condition) {
            getSubNodes().get(i).setEndStament(false);
            code = code + getSubNodes().get(i).getCode();
            i++;
        }
        code += loopBreak;
        if (loopExpression) {
            getSubNodes().get(i).setEndStament(false);
            code = code + getSubNodes().get(i).getCode();
            i++;
        }
        code = code + ") ";
        code = code + getSubNodes().get(i).getCode();
        super.getCode();
        return code;
    }
}
