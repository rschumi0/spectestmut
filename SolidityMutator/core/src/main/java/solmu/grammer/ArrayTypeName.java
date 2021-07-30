package solmu.grammer;

import org.json.simple.JSONObject;
import solmu.common.BasicGrammar;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayTypeName extends Node {
    public ArrayTypeName(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String output;
        ArrayList<Node> typeName = findSubNodes(new ArrayList<>(Arrays.asList(BasicGrammar.typeName)));
        output = typeName.get(0).getCode() + " [";
        if (getSubNodes().size()>1){
            getSubNodes().get(1).setEndStament(false);
            output += getSubNodes().get(1).getCode();
        }
        output += "]";
        super.getCode();
        return output;
    }
}
