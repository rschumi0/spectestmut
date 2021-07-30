package solmu.grammer;

import org.json.simple.JSONObject;
import solmu.common.BasicGrammar;

import java.util.ArrayList;

public class Continue extends  Node{

    public Continue(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }

    public Continue() {
        super();
        setType("Continue");
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        return getType().toLowerCase() + BasicGrammar.StatementEnd;
    }
}
