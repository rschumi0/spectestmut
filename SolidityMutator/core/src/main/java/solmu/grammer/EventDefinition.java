package solmu.grammer;

import org.json.simple.JSONObject;
import solmu.common.BasicGrammar;

import java.util.ArrayList;

public class EventDefinition extends Node {

    public Boolean anonymous;
    public EventDefinition(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
        this.anonymous = (Boolean) getAttributes().get("anonymous");
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String anonymous = "";
        if (this.anonymous)
            anonymous = " anonymous";
        String code = "event " + getName() + "(";

        code += findSubNode(BasicGrammar.NodeType.ParameterList.toString()).getCode();
        code += ")" + anonymous +";\n";
        super.getCode();
        return code;
    }
}
