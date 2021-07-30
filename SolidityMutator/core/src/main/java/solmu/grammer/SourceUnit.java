package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class SourceUnit extends Node {
    public SourceUnit(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = "";
        for (Node node : getSubNodes()) {
        code += node.getCode() + "\n";
        }
        super.getCode();
        return code;
    }
}
