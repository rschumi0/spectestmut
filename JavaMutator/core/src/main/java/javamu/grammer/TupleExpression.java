package javamu.grammer;

import javamu.ASTparser.NodeFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TupleExpression extends OldNode {
    private String type;
    private Boolean isInlineArray;
    private ArrayList<String> format;
    ArrayList<String> subCode = new ArrayList<>();
    public TupleExpression(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
        this.isInlineArray = (Boolean) getAttributes().get("isInlineArray");
        if( getAttributes().get("type") != null)
            this.type = getAttributes().get("type").toString().replace("tuple(","").replace(")","");
        format = new ArrayList<>(Arrays.asList(this.type.split(",")));
        if (type.endsWith(","))
            format.add("");
        for(OldNode sub : getSubNodes()) {
            sub.setEndStament(false);
        }
        if(getSubNodes().size()==0) {
            JSONArray components = (JSONArray) getAttributes().get("components");
            for(int i =0; i < components.size(); i ++) {
                JSONObject jsonObject = (JSONObject) components.get(i);
                if(components.get(i) != null){
                    addSubNode(findNodes(jsonObject,this.getSuperType()));
                }
            }
            Collections.sort(getSubNodes());
        }
    }
    public OldNode findNodes(JSONObject jsonObject, String superType){
        OldNode node;
        ArrayList<OldNode> innerNodes = new ArrayList<>();
        if (jsonObject.get("children") != null) {
            JSONArray children = (JSONArray) jsonObject.get("children");
            if (children != null){
                for(int x = 0; x < children.size(); x ++){
                    JSONObject innernode = (JSONObject) children.get(x);
                    //System.out.println(x + " " + innerObj + "\n");
                    innerNodes.add(findNodes(innernode, jsonObject.get("name").toString()));
                }
            }

        }

        NodeFactory nodeFactory = new NodeFactory(jsonObject, innerNodes);
        node = nodeFactory.getNode();
        node.setSuperType(superType);
        return node;
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        String code = "";
        ArrayList<String> subCode = new ArrayList<>();
        int i = 0;

        if(this.format.size() != getSubNodes().size() && getAttributes().get("type").toString().contains("tuple")){
            for(String type : this.format){
                if (type.isEmpty()){
                    subCode.add("");
                } else {
                    try {
                        subCode.add(getSubNodes().get(i).getCode());
                    } catch (IndexOutOfBoundsException e){
                    }
                    i++;
                }
            }
        } else
            subCode = getSubNodeCode();

        super.getCode();
        if (isInlineArray) {
            code += "[" + String.join(", ", subCode) + "]";
        } else {
            code += "(" + String.join(", ", subCode) + ")";
        }
        super.getCode();
        return code;
    }
}
