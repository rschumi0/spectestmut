package solmu.grammer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class PragmaDirective extends Node {
    private ArrayList<String> literals;
    public PragmaDirective(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
        this.literals = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) getAttributes().get("literals");
        for (int i = 0; i< jsonArray.size(); i++) {
            literals.add(jsonArray.get(i).toString());
        }
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        if(literals.contains("experimental")){
            return "pragma " + String.join(" ",literals) + getStatementEnd();
        }
        return "pragma solidity >=0.4.22 <0.6.0;\n";
    }
}
