package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class NewExpression extends Node {

    public NewExpression(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        String code = "new ";
        code += getSubNodes().get(0).getCode();

        return code;
    }
}
