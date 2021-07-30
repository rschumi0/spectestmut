package solmu.grammer;

import org.json.simple.JSONObject;
import solmu.common.SystemInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This is the base class for each node type.
 */
public abstract class Node implements Comparable<Node> {
    private String name;
    private String type;
    private ArrayList<Node> subNodes;
    private int id;
    private String visibility;
    private JSONObject jsonObject;
    private String superType;
    private Boolean codeGenarated = false;
    private JSONObject attributes;
    private Boolean endStament = true;
    private String statementEnd;

    public Node(){
         name = "Dummuy";
         type = "Dummy";
         subNodes = new ArrayList<>();
         id = -1000;
         visibility = "Dummuy";
         jsonObject = new JSONObject();
         superType  = "Dummuy";
         codeGenarated = false;
         attributes = new JSONObject();
         endStament = true;
         statementEnd =  "Dummuy";
    }

    public Node(Node copy) {
        this.attributes = copy.getAttributes();
        this.type = copy.getType();
        this.name = copy.getName();
        this.subNodes = new ArrayList<>();
        for (Node subNode : copy.getSubNodes()) {
            this.subNodes.add(subNode);
        }
        this.id = copy.getId();
        this.visibility = copy.getVisibility();
        this.jsonObject = copy.getJsonObject();
        this.statementEnd = copy.getStatementEnd();
        this.superType = copy.getSuperType();
        this.codeGenarated = false;
        this.endStament = copy.isEndStament();

    }

    /**
     * Constructor which will initalize the basic attributes of the node according to json object of node
     * @param node json object of node
     * @param subNodes list of sub nodes of the node
     */

    public Node(JSONObject node, ArrayList<Node> subNodes) {
        if(node != null) {
            SystemInfo.nodeCount ++;
            this.attributes = (JSONObject) node.get("attributes");
            String name = "";
            String visibility = "public";
            if (attributes != null){
                if (attributes.get("name") != null){
                    name = attributes.get("name").toString();
                }
                if (attributes.get("visibility") != null){
                    visibility = attributes.get("visibility").toString();
                }
            }

            this.type = node.get("name").toString();
            this.name = name;
            this.subNodes = subNodes;
            this.id = Integer.parseInt(node.get("id").toString());
            this.visibility = visibility;
            this.jsonObject = node;
            this.statementEnd = ";\n";
            try{
                SystemInfo.nodesMap.put(getType(),SystemInfo.nodesMap.get(getType()).intValue() +1);
            }catch (NullPointerException e){
                SystemInfo.nodesMap.put("empty",Integer.valueOf(1));
            }
        }
    }

    @Override
    public int compareTo(Node node) {
        return this.id - node.id;
    }

    @Override
    public int hashCode() {
        return this.id + this.name.hashCode() + this.subNodes.hashCode() + 57;
    }

    @Override
    public final boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if (obj == null)
            return false;

        if(this instanceof Node){
            Boolean state = true;
            Node node = (Node) obj;
            if(!this.name.equals(node.name)){
                state = false;
            }
            if(this.id != node.id)
                state = false;
            if(!this.type.equalsIgnoreCase(node.type))
                state = false;
            if (!this.subNodes.equals(node.subNodes))
                state = false;

            return state;
        }
        return false;
    }


    public String getStatementEnd() {
        if (!isEndStament())
            setStatementEnd("");
        return statementEnd;
    }

    public void setStatementEnd(String statementEnd) {
        this.statementEnd = statementEnd;
    }

    public Boolean isEndStament() {
        return endStament;
    }

    public void setEndStament(Boolean endStament) {
        this.endStament = endStament;
    }

    public JSONObject getAttributes() {
        return attributes;
    }

    public void setAttributes(JSONObject attributes) {
        this.attributes = attributes;
    }

    public Boolean getCodeGenarated() {
        return codeGenarated;
    }

    public void setCodeGenarated(Boolean codeGenarated) {
        this.codeGenarated = codeGenarated;
    }

    public String getSuperType() {
        return superType;
    }

    public void setSuperType(String superType) {
        this.superType = superType;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Node> getSubNodes() {
        //Collections.sort(subNodes, new SortById());
        return subNodes;
    }

    public void setSubNodes(ArrayList<Node> subNodes) {
        this.subNodes = subNodes;
    }

    /**
     * Find a list of sub nodes which matches a list of node types
     * @param type list of node type
     * @return list of noses which match the node types
     */
    public ArrayList<Node> findSubNodes(ArrayList<String> type) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (Node node : getSubNodes()) {
            if(type.contains(node.getType())) {
                nodes.add(node);
            }
        }
        Collections.sort(nodes, new SortById());
        return nodes;
    }


    /**
     * search the list of node witch is of a given type
     * @param type node type
     * @return list of nodes which matches the given type
     */
    public ArrayList<Node> findSubNodes(String type) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (Node node : getSubNodes()) {
            if(type.equalsIgnoreCase(node.getType())) {
                nodes.add(node);
            }
        }
        Collections.sort(nodes, new SortById());
        return nodes;
    }
    /**
     * search the list of node witch is not of a given type
     * @param type node type
     * @return list of nodes which doesn't matches the given type
     */
    public ArrayList<Node> findSubNodesNot(String type) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (Node node : getSubNodes()) {
            if(!type.equalsIgnoreCase(node.getType())) {
                nodes.add(node);
            }
        }
        Collections.sort(nodes, new SortById());
        return nodes;
    }

    /**
     * get the code of all sub nodes
     * @return list of codes of all sub nodes
     */
    public ArrayList<String> getSubNodeCode() {
        ArrayList<String> subCode = new ArrayList<>();
        for(Node node : getSubNodes()) {
            subCode.add(node.getCode());
        }
        return subCode;
    }

    /**
     * get sub nodes after a certain point
     * @param id
     * @return
     */
    public ArrayList<String> getSubNodeCodeFrom(int id) {
        ArrayList<String> subCode = new ArrayList<>();
        for(int i = id; i < getSubNodes().size(); i++) {
            subCode.add(getSubNodes().get(i).getCode());
        }
        return subCode;
    }


    public Node findSubNode(String type) {
        for (Node node : getSubNodes()) {
            if (type.equalsIgnoreCase(node.getType())) {
                return node;
            }
        }
        Node emptyNode = new Node(this.jsonObject, new ArrayList<>()) {
            @Override
            public String getCode() {
                return "";
            }
        };
        return emptyNode;
    }

    public ArrayList<Node> findSubNodes(ArrayList<String> type, String superType) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (Node node : getSubNodes()) {
            if(type.contains(node.getType()) && superType.equalsIgnoreCase(node.getSuperType())) {
                nodes.add(node);
            }
        }
        Collections.sort(nodes, new SortById());
        return nodes;
    }
    public Node findSubNode(String nodetype, String superType) {
        for (Node node : getSubNodes()) {
            if (nodetype.equalsIgnoreCase(node.getType()) && superType.equalsIgnoreCase(node.getSuperType())) {
                return node;
            }
        }
        return null;
    }

    public ArrayList<Node> findSupperTypeNodes(ArrayList<String> type) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        for (Node node : getSubNodes()) {
            if(type.contains(node.getSuperType())) {
                nodes.add(node);
            }
        }
        Collections.sort(nodes, new SortById());
        return nodes;
    }
    public Node findSupperTypeNode(String type) {
        for (Node node : getSubNodes()) {
            if (type.equalsIgnoreCase(node.getSuperType())) {
                return node;
            }
        }
        return null;
    }

    public Boolean codeComplete() {
        for (Node node : getSubNodes()) {
            if(!getCodeGenarated() || getCodeGenarated() == null)  {
                return false;
            }
        }
        return true;
    }

    public void addSubNode(Node newNode) {
        this.subNodes.add(0, newNode);
    }

    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    public String getCode() {
        codeGenarated = true;
        SystemInfo.usedNodeCount++;
        return "";
    }

    class SortById implements Comparator<Node>
    {
        public int compare(Node a, Node b)
        {
            return a.id - b.id;
        }
    }
}
