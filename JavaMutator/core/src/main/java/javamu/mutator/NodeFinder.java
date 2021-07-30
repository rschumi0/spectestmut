package javamu.mutator;

import javamu.ASTparser.MyNode;

import java.util.ArrayList;

public class NodeFinder {

    public static ArrayList<MyNode> find(String type, MyNode node) {
        ArrayList<MyNode> nodes = new ArrayList<>();
        if(node.getType().equalsIgnoreCase(type.toString())) {
            nodes.add(node);
        }
        for (MyNode subNode : node.getSubNodes()) {
            nodes.addAll(find(type, subNode));
        }
        return nodes;
    }

    public static ArrayList<MyNode> findUnused(MyNode node) {
        ArrayList<MyNode> nodes = new ArrayList<>();
        if(!node.getCodeGenarated()) {
            nodes.add(node);
        }
        for (MyNode subNode : node.getSubNodes()) {
            nodes.addAll(findUnused(subNode));
        }
        return nodes;
    }

    public static ArrayList<MyNode> find(String type, String superType, MyNode node) {
        ArrayList<MyNode> nodes = new ArrayList<>();
        if(node.getType().equalsIgnoreCase(type.toString()) &&
                node.getSuperType().equalsIgnoreCase(superType.toString())) {
            nodes.add(node);
        }
        for (MyNode subNode : node.getSubNodes()) {
            nodes.addAll(find(type, superType, subNode));
        }
        return nodes;
    }

    public static MyNode find(int id, MyNode node) {
        MyNode output = null;
        if( id == node.getId()) {
            return node;
        }
        for (MyNode subNode : node.getSubNodes()) {
            output = find(id, subNode);
            if (output != null) {
                return output;
            }
        }
        return null;
    }
}
