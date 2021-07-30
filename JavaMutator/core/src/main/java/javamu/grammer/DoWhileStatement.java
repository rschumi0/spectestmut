package javamu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class DoWhileStatement extends OldNode {

    public DoWhileStatement(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        String code = "do ";
        code += getSubNodes().get(1).getCode();
        code += "while (" + getSubNodes().get(0).getCode() + ");\n";
        return code;
    }
}
