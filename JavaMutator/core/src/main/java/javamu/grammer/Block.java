package javamu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Block extends OldNode {

    public Block(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
    }

    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = "{\n";

        for (OldNode subNode : getSubNodes()){
            code = code + subNode.getCode();
        }
        super.getCode();
        return code + "}\n";
    }
}
