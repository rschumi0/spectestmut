package javamu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class EnumDefinition extends OldNode {
    public EnumDefinition(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code;
        super.getCode();
        code = "enum " + getName() + " {" + String.join(", ",getSubNodeCode()) + "}\n";
        return code;
    }
}
