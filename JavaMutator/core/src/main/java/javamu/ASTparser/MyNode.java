package javamu.ASTparser;

import com.github.javaparser.ast.Node;
import javamu.common.SystemInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyNode  {

    private Node n;


    public MyNode(Node n) {
        this.n= n;//super(!n.getTokenRange().isPresent() ? null : n.getTokenRange().get());
    }



    /*public MyNode(CompilationUnit cu) {
        super(!cu.getTokenRange().isPresent() ? null : cu.getTokenRange().get(),!cu.getPackageDeclaration().isPresent() ? null : cu.getPackageDeclaration().get(),cu.getImports(),cu.getTypes(),!cu.getModule().isPresent() ? null : cu.getModule().get());
    }

    protected MyNode(TokenRange tokenRange, PackageDeclaration packageDeclaration, NodeList<ImportDeclaration> imports, NodeList<TypeDeclaration<?>> types, ModuleDeclaration module) {
        super(tokenRange,packageDeclaration,imports,types,module);
    }*/

    /*@Override
    public <R, A> R accept(final GenericVisitor<R, A> v, A arg) {
        //return v.visit(this, arg);
        return null;
    }

    @Override
    public <A> void accept(final VoidVisitor<A> v, A arg) {
        //v.visit(this, arg);
    }*/



    private String name;
    private String visibility;
    private Boolean codeGenarated = false;
    private Boolean endStament = true;
    private String statementEnd;

    public int compareTo(MyNode node) {
        return n.hashCode() - node.hashCode();
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


    public Boolean getCodeGenarated() {
        return codeGenarated;
    }

    public void setCodeGenarated(Boolean codeGenarated) {
        this.codeGenarated = codeGenarated;
    }

    public String getSuperType() {
        return n.getParentNode().getClass().getSimpleName();
    }



    public int getId() {
        return n.hashCode();
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
        return n.getClass().getSimpleName();
    }


    public ArrayList<MyNode> getSubNodes() {
        //Collections.sort(subNodes, new SortById());
        ArrayList<MyNode>  arrayList = new ArrayList<MyNode>();
        for (Node n: n.getChildNodes()) {
            arrayList.add(new MyNode(n));
        }
        return arrayList;
    }

    public void setSubNodes(ArrayList<MyNode> nodes) {
        n.getChildNodes().clear();
        for (MyNode n1: nodes) {
            n.getChildNodes().add(n1.getNode());
        }
    }



    /**
     * Find a list of sub nodes which matches a list of node types
     * @param types list of node type
     * @return list of noses which match the node types
     */
    public ArrayList<MyNode> findSubNodes(ArrayList<String> types) {
        ArrayList<MyNode> nodes = new ArrayList<MyNode>();

        /*for (String type: types){
            List<Node> ns = null;
            try {
                ns = getChildNodesByType(Class.forName(type));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            for(Node n: ns){
                nodes.add((MyNode)n);
            }
        }*/

        for (Node node : n.getChildNodes()) {
            if(types.contains(node.getClass().getSimpleName())) {
                nodes.add( new MyNode(node));
            }
        }
        //Collections.sort(nodes, new MyNode.SortById());
        return nodes;
    }


    /**
     * search the list of node witch is of a given type
     * @param type node type
     * @return list of nodes which matches the given type
     */
    public ArrayList<MyNode> findSubNodes(String type) {
        ArrayList<String> list = new ArrayList<String>();
        list.add(type);
        return this.findSubNodes(list);
    }
    /**
     * search the list of node witch is not of a given type
     * @param type node type
     * @return list of nodes which doesn't matches the given type
     */
    public ArrayList<MyNode> findSubNodesNot(String type) {
        ArrayList<MyNode> nodes = new ArrayList<MyNode>();
        for (Node node : n.getChildNodes()) {
            if(!type.equalsIgnoreCase(node.getClass().getSimpleName())) {
                nodes.add(new MyNode(node));
            }
        }
        //Collections.sort(nodes, new MyNode.SortById());
        return nodes;
    }

    /**
     * get the code of all sub nodes
     * @return list of codes of all sub nodes
     */
    public ArrayList<String> getSubNodeCode() {
        /*ArrayList<String> subCode = new ArrayList<>();
        for(OldNode node : getSubNodes()) {
            subCode.add(node.getCode());
        }
        return subCode;
         */
        ArrayList<String> list = new ArrayList<String>();
        for(Node n : n.getChildNodes()){
            list.add(n.toString());
        }
        return list;
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


    public MyNode findSubNode(String type) {
        for (Node node : n.getChildNodes()) {
            if(type.contains(node.getClass().getSimpleName())) {
                return new MyNode(node);
            }
        }
        return null;
    }

    public ArrayList<MyNode> findSubNodes(ArrayList<String> type, String superType) {
        ArrayList<MyNode> nodes = new ArrayList<MyNode>();
        for (Node node : n.getChildNodes()) {
            if(type.contains(node.getClass().getSimpleName()) && superType.equalsIgnoreCase(node.getParentNode().getClass().getSimpleName())) {
                nodes.add(new MyNode(node));
            }
        }
        Collections.sort(nodes, new MyNode.SortById());
        return nodes;
    }
    public MyNode findSubNode(String nodetype, String superType) {
        for (Node node : n.getChildNodes()) {
            if(nodetype.contains(node.getClass().getSimpleName()) && superType.equalsIgnoreCase(node.getParentNode().getClass().getSimpleName())) {
                return new MyNode(node);
            }
        }
        return null;
    }

    public ArrayList<MyNode> findSupperTypeNodes(ArrayList<String> types) {
        ArrayList<MyNode> nodes = new ArrayList<MyNode>();
        for (Node node : n.getChildNodes()) {
            if(types.contains(node.getParentNode().getClass().getSimpleName())) {
                nodes.add(new MyNode(node));
            }
        }
        //Collections.sort(nodes, new MyNode.SortById());
        return nodes;
    }
    public MyNode findSupperTypeNode(String type) {
        for (Node node : n.getChildNodes()) {
            if(type.equals(node.getParentNode().getClass().getSimpleName())) {
                return new MyNode(node);
            }
        }
        return null;
    }


    public void addSubNode(MyNode newNode) {
        n.getChildNodes().add(newNode.getNode());
        //this.subNodes.add(0, newNode);
    }

    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    public String getCode() {
        codeGenarated = true;
        SystemInfo.usedNodeCount++;
        return n.toString();
    }

    public Node getNode() {
        return n;
    }

    public void setNode(Node node) {
        this.n = node;
    }

    class SortById implements Comparator<MyNode>
    {
        public int compare(MyNode a, MyNode b)
        {
            return a.hashCode() - b.hashCode();
        }
    }
}
