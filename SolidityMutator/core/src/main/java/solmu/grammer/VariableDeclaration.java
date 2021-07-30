package solmu.grammer;

import org.json.simple.JSONObject;
import solmu.common.BasicGrammar;

import java.util.ArrayList;
import java.util.Arrays;

public class VariableDeclaration extends Node {

    /*
    superTypes that don't need statement end
     */
    ArrayList<String> dontEnd = new ArrayList<>(Arrays.asList(
            BasicGrammar.NodeType.ParameterList.toString(),
            BasicGrammar.NodeType.VariableDeclarationStatement.toString()
    ));

    ArrayList<String> noVisibility = new ArrayList<>(Arrays.asList(
            BasicGrammar.NodeType.ParameterList.toString(),
            BasicGrammar.NodeType.StructDefinition.toString()
    ));

    private String storageLocation = "";
    private Boolean constant;
    private Boolean indexed;

    public Boolean getConstant() {
        return constant;
    }

    public void setConstant(Boolean constant) {
        this.constant = constant;
    }

    public Boolean getIndexed() {
        return indexed;
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
    }

    public VariableDeclaration(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
        this.constant = (Boolean) getAttributes().get("constant");
        this.indexed = (Boolean) getAttributes().getOrDefault("indexed", false);
        if (!getAttributes().get("storageLocation").toString().equalsIgnoreCase("default"))
            this.storageLocation = getAttributes().get("storageLocation").toString();
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    public String getCode() {
        String code = findSubNodes(new ArrayList<>(Arrays.asList(BasicGrammar.typeName))).get(0).getCode() + " " ;
        code = code + getStorageLocation() + " ";
        if (getVisibility() != "" && !getVisibility().equalsIgnoreCase("internal") &&
                !noVisibility.contains(getSuperType())) {
            code = code + getVisibility() + " ";
        }
        String constant = "";
        String indexed = "";
        if(this.constant){
            constant = "constant ";
        }
        if (this.indexed){
            indexed = "indexed ";
        }
        code += constant;
        code += indexed;
        code = code + getName();
        if(getSubNodes().size() > 1) {
            code += " = " + getSubNodes().get(1).getCode();
        }
        if (!dontEnd.contains(getSuperType())) {
            code = code + BasicGrammar.StatementEnd;
        }

        super.getCode();
        return code;
    }
}
