package solmu.mutator;

import solmu.common.BasicGrammar;
import solmu.grammer.Node;

import java.util.ArrayList;

public class NodeFinder {

    public static ArrayList<Node> find(BasicGrammar.NodeType type, Node node) {
        ArrayList<Node> nodes = new ArrayList<>();
        if(node.getType().equalsIgnoreCase(type.toString())) {
            nodes.add(node);
        }
        for (Node subNode : node.getSubNodes()) {
            nodes.addAll(find(type, subNode));
        }
        return nodes;
    }

    public static ArrayList<Node> findUnused(Node node) {
        ArrayList<Node> nodes = new ArrayList<>();
        if(!node.getCodeGenarated()) {
            nodes.add(node);
        }
        for (Node subNode : node.getSubNodes()) {
            nodes.addAll(findUnused(subNode));
        }
        return nodes;
    }

    public static ArrayList<Node> find(BasicGrammar.NodeType type, BasicGrammar.NodeType superType, Node node) {
        ArrayList<Node> nodes = new ArrayList<>();
        if(node.getType().equalsIgnoreCase(type.toString()) &&
                node.getSuperType().equalsIgnoreCase(superType.toString())) {
            nodes.add(node);
        }
        for (Node subNode : node.getSubNodes()) {
            nodes.addAll(find(type, superType, subNode));
        }
        return nodes;
    }

    public static Node find(int id, Node node) {
        Node output = null;
        if( id == node.getId()) {
            return node;
        }
        for (Node subNode : node.getSubNodes()) {
            output = find(id, subNode);
            if (output != null) {
                return output;
            }
        }
        return null;
    }
}
