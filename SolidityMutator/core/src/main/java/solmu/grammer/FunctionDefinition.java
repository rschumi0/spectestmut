package solmu.grammer;

import org.json.simple.JSONObject;
import solmu.common.BasicGrammar;

import java.util.ArrayList;
import java.util.Arrays;

public class FunctionDefinition extends Node {

    private String kind;
    private String stateMutability;
    private Boolean isConstructor;

    public FunctionDefinition(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
        kind = getAttributes().getOrDefault("kind","").toString();
        stateMutability = getAttributes().get("stateMutability").toString();
        isConstructor = (Boolean) getAttributes().get("isConstructor");
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getStateMutability() {
        if (stateMutability.equalsIgnoreCase("nonpayable"))
            return "";
        return stateMutability;
    }

    public void setStateMutability(String stateMutability) {
        this.stateMutability = stateMutability;
    }

    public Boolean getConstructor() {
        return isConstructor;
    }

    public void setConstructor(Boolean constructor) {
        isConstructor = constructor;
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code;
        if(isConstructor) {
            code = "constructor" + "(";
        } else {
            code = "function " + getName() + "(";
        }
        Node inputParam = findSubNodes(new ArrayList<>(Arrays.asList("ParameterList")),"FunctionDefinition").get(0);
        Node returnParam = findSubNodes(new ArrayList<>(Arrays.asList("ParameterList")),"FunctionDefinition").get(1);
        code = code + inputParam.getCode() + ") ";
        code = code + getVisibility() + BasicGrammar.space + getStateMutability() + BasicGrammar.space ;
        ArrayList<Node> modifiers = findSubNodes(new ArrayList<>(Arrays.asList("ModifierInvocation")));
        for (Node modifierNode : modifiers) {
            code = code + modifierNode.getCode();
        }
        if( returnParam != null){
            if(!returnParam.getCode().isEmpty()) {
                code = code + "returns (" + returnParam.getCode() + ") ";
            }
        }
        Node block = findSubNode("Block");
        if (!block.getCode().isEmpty())
            code = code + block.getCode();
        else
            code = code + ";\n";
        super.getCode();
        return code;
    }
}
