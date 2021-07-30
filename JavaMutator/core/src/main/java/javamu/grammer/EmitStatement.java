package javamu.grammer;

import org.json.simple.JSONObject;
import javamu.common.BasicGrammar;

import java.util.ArrayList;

public class EmitStatement extends OldNode {

    public EmitStatement(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
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
