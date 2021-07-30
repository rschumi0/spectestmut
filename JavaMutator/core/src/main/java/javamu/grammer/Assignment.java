package javamu.grammer;

import org.json.simple.JSONObject;
import javamu.common.BasicGrammar;

import java.util.ArrayList;

public class Assignment extends OldNode {
    private String operation;

    public Assignment(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
        this.operation = getAttributes().get("operator").toString();
    }

    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */

    @Override
    public String getCode() {
        String code = getSubNodes().get(0).getCode() + BasicGrammar.space + this.operation + BasicGrammar.space +
                getSubNodes().get(1).getCode();
        super.getCode();
        return code;
    }
}
