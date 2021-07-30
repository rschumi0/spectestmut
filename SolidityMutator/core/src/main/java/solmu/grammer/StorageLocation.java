package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class StorageLocation extends Node {

    public StorageLocation(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        return getName();
    }
}
