package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class UnaryOperation extends Node {
    private String operation;
    private Boolean prefix;

    public UnaryOperation(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
        this.operation = getAttributes().get("operator").toString();
        this.prefix = (Boolean) getAttributes().get("prefix");
    }

    public Boolean getPrefix() {
        return prefix;
    }

    public void setPrefix(Boolean prefix) {
        this.prefix = prefix;
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
        String code;
        if(getPrefix()){
            code = getOperation() + " " + getSubNodes().get(0).getCode();
        } else {
            code = getSubNodes().get(0).getCode() + " " + getOperation();
        }
        super.getCode();
        return code;
    }
}
