package solmu.mutator.Mutations;

import solmu.common.BasicGrammar;
import solmu.grammer.Node;
import solmu.mutator.NodeFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddDoWhile extends Mutation {
    public AddDoWhile(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(Node AST) {
        if(super.start(AST)) {
            ArrayList<Node> contracts = NodeFinder.find(BasicGrammar.NodeType.ContractDefinition, AST);
            int count =Integer.parseInt(getParameters().getOrDefault("count",1).toString());
            int nested = (int) getParameters().getOrDefault("nested",1);
            for(int i = 0; i < count; i++) {
                for (Node contract : contracts) {
                    Node newNode = getMutauionNode();
                    newNode.getSubNodes().get(0).setName(
                            getParameters().getOrDefault("condition","true").toString());
                    newNode = addNested(newNode.getSubNodes().get(1), nested);
                    newNode = BreakContinue.AddBreakContinue(newNode,getParameters());
                    for(String type : getAddTo()) {
                        ArrayList<Node> targetBlocks = getNodeType(type, contract);
                        for (Node target : targetBlocks) {
                            Node block = target.findSubNode(BasicGrammar.NodeType.Block.toString());
                            if(!block.getCode().isEmpty()){
                                block.addSubNode(newNode);
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    private Node addNested(Node node, int count){

        Node base = getMutauionNode();
        base.getSubNodes().get(0).setName(getParameters().getOrDefault("condition","true").toString());
        base = BreakContinue.AddBreakContinue(base,getParameters());
        node.addSubNode(base);
        if (count == 0){
            return base;
        }
        addNested(node.getSubNodes().get(0).getSubNodes().get(1),--count);
        return node;
    }
}
