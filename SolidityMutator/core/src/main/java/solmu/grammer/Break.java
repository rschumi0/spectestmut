package solmu.grammer;

import org.json.simple.JSONObject;
import solmu.common.BasicGrammar;

import java.util.ArrayList;

public class Break extends Node {
    public Break(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
    }

    public Break() {
        super();
        setType("Break");
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
