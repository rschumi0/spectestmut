package javamu.grammer;

import org.json.simple.JSONObject;
import javamu.common.BasicGrammar;

import java.util.ArrayList;

public class Continue extends OldNode {

    public Continue(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
    }

    public Continue() {
        super();
        setType("Continue");
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        return getType().toLowerCase() + BasicGrammar.StatementEnd;
    }
}
