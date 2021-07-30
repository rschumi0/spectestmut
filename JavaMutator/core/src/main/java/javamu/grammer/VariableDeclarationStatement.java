package javamu.grammer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class VariableDeclarationStatement extends OldNode {
    private ArrayList<Long> assignments;
    private Boolean noInitialValue;
    public VariableDeclarationStatement(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
        this.noInitialValue = getAttributes().keySet().contains("initialValue");
        JSONArray jsonAssignments = (JSONArray) getAttributes().get("assignments");
        Iterator<Long> iterator = jsonAssignments.iterator();
        this.assignments = new ArrayList();
        while (iterator.hasNext()){
            Long temp = iterator.next();
            this.assignments.add(temp);
        }
        for (OldNode sub : getSubNodes()) {
            sub.setEndStament(false);
        }
    }

    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode(){
        String code;
        if (noInitialValue) {
            if (assignments.size() == 1){
                code = findSubNode("VariableDeclaration").getCode() + getStatementEnd();
            } else {
                code = getVarDeclarations() + getStatementEnd();
            }
        }else {
            if (assignments.size() == 1){
                code = findSubNode("VariableDeclaration").getCode() + " = " + getSubNodes().get(1).getCode() +
                        getStatementEnd();
            } else {
                code = getVarDeclarations() + " = " + getSubNodes().get(getSubNodes().size()-1).getCode() +
                        getStatementEnd();
            }
        }

        super.getCode();
        return code;
    }

    private String getCodeById(int id) {
        for(OldNode node : getSubNodes()) {
            if (node.getId() == id){
                return node.getCode();
            }
        }
        return "";
    }

    private String getVarDeclarations() {
        ArrayList<String> varList = new ArrayList<>();
        for(Long i : assignments) {
            if(i == null){
                varList.add("");
            } else {
                varList.add(getCodeById(i.intValue()));
            }
        }
        String code = "(" + String.join(", ",varList) + ")";
        return code;
    }


}
