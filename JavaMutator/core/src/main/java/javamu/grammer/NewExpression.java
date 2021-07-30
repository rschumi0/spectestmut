package javamu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class NewExpression extends OldNode {

    public NewExpression(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        String code = "new ";
        code += getSubNodes().get(0).getCode();

        return code;
    }
}
