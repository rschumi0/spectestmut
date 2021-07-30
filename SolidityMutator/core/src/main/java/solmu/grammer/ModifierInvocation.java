package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ModifierInvocation extends Node {

    public ModifierInvocation(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        Node identifire = findSubNode("Identifier");

        String code = identifire.getCode();

        if(getSubNodes().size() > 1) {
            code += "(" + String.join(", ",getSubNodeCodeFrom(1)) + ") ";

        } else {
            code += "()";
        }
        super.getCode();
        return code;
    }
}
