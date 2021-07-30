package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ExpressionStatement extends Node {

    public ExpressionStatement(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
        getSubNodes().get(0).setEndStament(false);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = getSubNodes().get(0).getCode() + getStatementEnd();
        super.getCode();
        return code;
    }
}
