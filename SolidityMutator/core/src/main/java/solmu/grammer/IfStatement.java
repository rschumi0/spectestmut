package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class IfStatement extends Node {

    public IfStatement(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
        getSubNodes().get(0).setEndStament(false);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = "if (";
        code = code + getSubNodes().get(0).getCode() + ") " + getSubNodes().get(1).getCode();
        if (getSubNodes().size() > 2) {
            code = code + " else " + getSubNodes().get(2).getCode();
        }
        super.getCode();
        return code;
    }
}
