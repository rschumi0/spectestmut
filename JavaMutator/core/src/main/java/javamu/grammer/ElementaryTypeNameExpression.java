package javamu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ElementaryTypeNameExpression extends OldNode {

    public ElementaryTypeNameExpression(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
        if(getName().isEmpty()){
            setName(getAttributes().get("value").toString());
        }
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        return getName();
    }

}
