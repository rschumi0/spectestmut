package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class IndexAccess extends Node {

    public IndexAccess(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code;
        super.getCode();
        code = getSubNodes().get(0).getCode() + "[" + getSubNodes().get(1).getCode() + "]";
        return code;
    }
}
