package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ParameterList extends Node {

    public ParameterList(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = "";
        code += String.join(", ", getSubNodeCode());
        super.getCode();
        return code;
    }
}
