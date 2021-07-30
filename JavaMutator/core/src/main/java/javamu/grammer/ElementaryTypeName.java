package javamu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ElementaryTypeName extends OldNode {
    private String stateMutability;
    public ElementaryTypeName(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
        stateMutability = (String) getAttributes().getOrDefault("stateMutability","");
        if(!stateMutability.equalsIgnoreCase("payable")){
            stateMutability = "";
        }
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    public String getCode() {
        super.getCode();
        return getName() + " " + stateMutability;
    }
}
