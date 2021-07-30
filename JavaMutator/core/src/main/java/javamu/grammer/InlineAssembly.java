package javamu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class InlineAssembly extends OldNode {

    public InlineAssembly(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        String code;
        code = "assembly " + getAttributes().get("operations").toString() + "\n";
        return code;
    }
}
