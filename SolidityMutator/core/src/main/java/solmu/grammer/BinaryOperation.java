package solmu.grammer;

import org.json.simple.JSONObject;
import solmu.common.BasicGrammar;

import java.util.ArrayList;

public class BinaryOperation extends Node {

    private String operation;
    public BinaryOperation(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
        this.operation = getAttributes().get("operator").toString();
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = getSubNodes().get(0).getCode() + BasicGrammar.space + getOperation() + BasicGrammar.space +
                getSubNodes().get(1).getCode();
        super.getCode();
        return code;
    }
}
