package solmu.grammer;

import org.json.simple.JSONObject;
import solmu.common.BasicGrammar;

import java.util.ArrayList;
import java.util.Arrays;

public class StructDefinition extends Node {

    public StructDefinition(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String output = "struct " + getName() + " {\n";
        ArrayList<Node> variables = findSubNodes(new ArrayList<>(
                Arrays.asList(BasicGrammar.NodeType.VariableDeclaration.toString())));

        for (Node node : variables){
            output = output + node.getCode();
        }
        output = output + "}\n";
        super.getCode();
        return output;
    }
}