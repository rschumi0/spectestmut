package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class InlineAssembly extends Node {

    public InlineAssembly(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
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
