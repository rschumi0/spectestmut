package solmu.mutator.Mutations;

import solmu.common.BasicGrammar;
import solmu.grammer.Node;
import solmu.mutator.NodeFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddModifier extends Mutation {
    public AddModifier(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(Node ast) {
        if (super.start(ast)){
            ArrayList<Node> contracts = NodeFinder.find(BasicGrammar.NodeType.ContractDefinition, ast);
            int count =Integer.parseInt(getParameters().getOrDefault("count",1).toString());
            for(int i = 0; i < count; i++) {
                for (Node contract : contracts) {
                    String name = BasicGrammar.randomName();
                    Node newNode = getMutauionNode();
                    newNode.getSubNodes().get(1).setName(name);
                    newNode.getSubNodes().get(0).getSubNodes().get(0).setName(name);
                    contract.addSubNode(newNode.getSubNodes().get(1));
                    ArrayList<Node> functionBlocks = NodeFinder.find(BasicGrammar.NodeType.FunctionDefinition, contract);
                    for (Node function : functionBlocks) {
                        if(!function.findSubNode(BasicGrammar.NodeType.Block.toString()).getCode().isEmpty())
                            function.addSubNode(newNode.getSubNodes().get(0));
                    }
                }
            }
            return true;
        }
        return false;
    }
}
