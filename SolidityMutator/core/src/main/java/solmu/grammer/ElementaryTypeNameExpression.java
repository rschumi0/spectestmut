package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ElementaryTypeNameExpression extends Node{

    public ElementaryTypeNameExpression(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
        if(getName().isEmpty()){
            setName(getAttributes().get("value").toString());
        }
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        return getName();
    }

}
