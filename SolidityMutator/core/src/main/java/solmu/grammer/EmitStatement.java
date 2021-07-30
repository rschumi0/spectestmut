package solmu.grammer;

import org.json.simple.JSONObject;
import solmu.common.BasicGrammar;

import java.util.ArrayList;

public class EmitStatement extends Node{

    public EmitStatement(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = "emit " + findSubNode(BasicGrammar.NodeType.FunctionCall.toString()).getCode() +
                BasicGrammar.StatementEnd;
        super.getCode();
        return code;
    }
}
