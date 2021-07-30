package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class InheritanceSpecifier extends Node {

    public InheritanceSpecifier(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = getSubNodes().get(0).getCode();
        if (getSubNodes().size() > 1){
            ArrayList<String> vars = getSubNodeCodeFrom(1);
            code += "(" + String.join(", ",vars) + ")";
        }
        super.getCode();
        return code;
    }
}
