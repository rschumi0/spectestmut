package javamu.grammer;

import javamu.common.BasicGrammar;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ModifierDefinition extends OldNode {

    public ModifierDefinition(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = "modifier " + getName() + "(";
        code += findSubNode(BasicGrammar.NodeType.ParameterList.toString()).getCode() + ") ";

        code += findSubNode(BasicGrammar.NodeType.Block.toString()).getCode();
        super.getCode();
        return code;
    }
}
