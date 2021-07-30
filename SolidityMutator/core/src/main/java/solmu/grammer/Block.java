package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Block extends Node {

    public Block(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }

    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = "{\n";

        for (Node subNode : getSubNodes()){
            code = code + subNode.getCode();
        }
        super.getCode();
        return code + "}\n";
    }
}
