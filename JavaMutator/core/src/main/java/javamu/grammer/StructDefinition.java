package javamu.grammer;

import javamu.common.BasicGrammar;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class StructDefinition extends OldNode {

    public StructDefinition(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String output = "struct " + getName() + " {\n";
        ArrayList<OldNode> variables = findSubNodes(new ArrayList<>(
                Arrays.asList(BasicGrammar.NodeType.VariableDeclaration.toString())));

        for (OldNode node : variables){
            output = output + node.getCode();
        }
        output = output + "}\n";
        super.getCode();
        return output;
    }
}