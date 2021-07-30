package javamu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class WhileStatement extends OldNode {

    public WhileStatement(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
        getSubNodes().get(0).setEndStament(false);
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = "while (";
        code = code + getSubNodes().get(0).getCode() + ") ";
        code = code + getSubNodes().get(1).getCode();
        super.getCode();
        return code;
    }
}
