package solmu.mutator.Mutations;

import solmu.common.BasicGrammar;
import solmu.grammer.FunctionDefinition;
import solmu.grammer.Node;
import solmu.mutator.NodeFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddEvent extends Mutation{
    public AddEvent(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    /**
     * This method willl randamize the nmae of the event and add event definition to contract and
     * emit statement to function definitions
     * @param ast input AST to be modified
     * @return status of mutation
     */
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
                    newNode.getSubNodes().get(0).getSubNodes().get(0).getSubNodes().get(0).setName(name);
                    contract.addSubNode(newNode.getSubNodes().get(1));
                    ArrayList<Node> functionBlocks = NodeFinder.find(BasicGrammar.NodeType.FunctionDefinition, contract);
                    for (Node function : functionBlocks) {
                        FunctionDefinition functionDefinition = (FunctionDefinition) function;
                        for(String type : getAddTo()){
                            if (!functionDefinition.getStateMutability().equals("view") &&
                                    !functionDefinition.getStateMutability().equals("pure")) {
                                ArrayList<Node> targetBlocks = getNodeType(type, function);
                                for (Node target : targetBlocks) {
                                    Node block = target.findSubNode(BasicGrammar.NodeType.Block.toString());
                                    if (block != null){
                                        block.addSubNode(newNode.getSubNodes().get(0));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
