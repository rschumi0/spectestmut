package solmu.grammer;

import org.json.simple.JSONObject;
import solmu.common.BasicGrammar;

import java.util.ArrayList;
import java.util.Arrays;

public class Mapping extends Node {

    public Mapping(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String output = "mapping (";
        ArrayList<Node> variables = findSubNodes(new ArrayList<>(Arrays.asList(BasicGrammar.typeName)));
        output = output + variables.get(0).getCode() + " => " + variables.get(1).getCode() + ")";
        super.getCode();
        return output;
    }
}
