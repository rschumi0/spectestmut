package solmu.grammer;

import org.json.simple.JSONObject;
import solmu.common.BasicGrammar;

import java.util.ArrayList;

public class ModifierDefinition extends Node{

    public ModifierDefinition(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
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
