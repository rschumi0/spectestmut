package javamu.grammer;

import javamu.common.BasicGrammar;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Throw extends OldNode {

    public Throw(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        return getType() + BasicGrammar.StatementEnd;
    }
}
