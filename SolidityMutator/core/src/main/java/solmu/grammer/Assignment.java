package solmu.grammer;

import org.json.simple.JSONObject;
import solmu.common.BasicGrammar;

import java.util.ArrayList;

public class Assignment extends Node {
    private String operation;

    public Assignment(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
        this.operation = getAttributes().get("operator").toString();
    }

    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */

    @Override
    public String getCode() {
        String code = getSubNodes().get(0).getCode() + BasicGrammar.space + this.operation + BasicGrammar.space +
                getSubNodes().get(1).getCode();
        super.getCode();
        return code;
    }
}
