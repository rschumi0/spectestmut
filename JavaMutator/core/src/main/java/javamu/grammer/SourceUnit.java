package javamu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class SourceUnit extends OldNode {
    public SourceUnit(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = "";
        for (OldNode node : getSubNodes()) {
        code += node.getCode() + "\n";
        }
        super.getCode();
        return code;
    }
}
