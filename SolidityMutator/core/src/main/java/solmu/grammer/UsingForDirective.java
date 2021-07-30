package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class UsingForDirective extends Node {

    public UsingForDirective(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String usingFor;
        if (getSubNodes().size() == 1){
            usingFor = "*";
        } else
            usingFor = getSubNodes().get(1).getCode();

        String code = "using " + getSubNodes().get(0).getCode() + " for " + usingFor + getStatementEnd();
        super.getCode();
        return code;
    }
}
