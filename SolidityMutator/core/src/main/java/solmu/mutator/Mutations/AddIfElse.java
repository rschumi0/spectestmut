package solmu.mutator.Mutations;

import solmu.common.BasicGrammar;
import solmu.grammer.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddIfElse extends Mutation {

    public AddIfElse(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(Node ast) {
        Boolean status = false;

        if(super.start(ast)){
            for(String type : getAddTo()) {
                ArrayList<Node> targetBlocks = getNodeType(type, ast);
                for (Node target : targetBlocks) {
                    Node newNode = getMutauionNode();
                    newNode.getSubNodes().get(0).setName(getParameters().getProperty("condition"));

                    int nested = (int) getParameters().getOrDefault("nested",1);
                    if((Boolean) getParameters().getOrDefault("onlyIf",false)){
                        newNode.getSubNodes().remove(2);
                    }

                    if(getParameters().getProperty("move") != null){
                        int moveTo;
                        if(getParameters().getProperty("move").toLowerCase().contains("true"))
                            moveTo = 1;
                        else if (getParameters().getProperty("move").toLowerCase().contains("false"))
                            moveTo = 2;
                        else
                            return false;

                        newNode.getSubNodes().get(moveTo).getSubNodes().addAll(
                                target.findSubNode(BasicGrammar.NodeType.Block.toString()).getSubNodes());
                        target.findSubNode(BasicGrammar.NodeType.Block.toString()).setSubNodes(new ArrayList<>());

                        target.findSubNode(BasicGrammar.NodeType.Block.toString()).addSubNode(newNode);
                    }else
                        newNode = addNested(newNode.getSubNodes().get(1), 1, nested);
                        target.findSubNode(BasicGrammar.NodeType.Block.toString()).addSubNode(newNode);
                    status = true;
                }
            }
        }
        return status;
    }

    private Node addNested(Node node, int ifOrElse, int count){
        Node base = getMutauionNode();
        base.getSubNodes().get(0).setName(getParameters().getProperty("condition"));
        node.addSubNode(base);
        if (count == 0){
            return base;
        }
        addNested(node.getSubNodes().get(0).getSubNodes().get(ifOrElse),ifOrElse,--count);
        return node;
    }
}
