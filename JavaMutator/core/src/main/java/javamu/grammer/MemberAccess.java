package javamu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class MemberAccess extends OldNode {

    private String memberName;

    public MemberAccess(JSONObject node, ArrayList<OldNode> subNodes) {
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
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        String code = getSubNodes().get(0).getCode() + "." + getMemberName();
        return code;
    }
}
