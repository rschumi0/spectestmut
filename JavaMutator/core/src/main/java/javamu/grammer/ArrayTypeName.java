package javamu.grammer;

import org.json.simple.JSONObject;
import javamu.common.BasicGrammar;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayTypeName extends OldNode {
    public ArrayTypeName(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String output;
        ArrayList<OldNode> typeName = findSubNodes(new ArrayList<>(Arrays.asList(BasicGrammar.typeName)));
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
