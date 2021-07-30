package javamu.grammer;

import org.json.simple.JSONObject;
import javamu.common.BasicGrammar;

import java.util.ArrayList;


public class ContractDefinition extends OldNode {

    private String contractKind;
    public ContractDefinition(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
        this.contractKind = getAttributes().get("contractKind").toString();

    }

    public String getContractKind() {
        return contractKind;
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    public String getCode() {

        String contract = this.contractKind + " " + getName();
        ArrayList<String> inheritance = new ArrayList<>();
        for(OldNode inheritanceSpecifiers : findSubNodes(BasicGrammar.NodeType.InheritanceSpecifier.toString())) {
            inheritance.add(inheritanceSpecifiers.getCode());
        }
        if (!findSubNodes(BasicGrammar.NodeType.InheritanceSpecifier.toString()).isEmpty()) {
            contract += " is " + String.join(", ",inheritance);
        }

        contract += " {\n";
        for(OldNode node : findSubNodesNot(BasicGrammar.NodeType.InheritanceSpecifier.toString())) {
            contract = contract + node.getCode() ;
        }
        super.getCode();
        return contract + "\n}\n";
    }
}
