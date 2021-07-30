package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Conditional extends Node {
    public Conditional(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = "";
        code += getSubNodes().get(0).getCode() + " ? " + getSubNodes().get(1).getCode() + " : " +
                getSubNodes().get(2).getCode();
        super.getCode();
        return code;
    }
}
