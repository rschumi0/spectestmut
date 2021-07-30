package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class MemberAccess extends Node {

    private String memberName;

    public MemberAccess(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
        this.memberName = getAttributes().get("member_name").toString();
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        String code = getSubNodes().get(0).getCode() + "." + getMemberName();
        return code;
    }
}
