package solmu.mutator.Mutations;

import solmu.common.BasicGrammar;
import solmu.grammer.ContractDefinition;
import solmu.grammer.FunctionDefinition;
import solmu.grammer.Node;
import solmu.mutator.NodeFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddFunctionDef extends Mutation {
    public AddFunctionDef(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(Node AST) {
        if(super.start(AST)) {
            ArrayList<Node> contracts = NodeFinder.find(BasicGrammar.NodeType.ContractDefinition, AST);
            int count =Integer.parseInt(getParameters().getOrDefault("count",1).toString());
            String stateMutability =
                    getParameters().getOrDefault("stateMutability","pure").toString();
            String visibility = "internal";
            for(int i = 0; i < count; i++) {
                for (Node contract : contracts) {

                    Node functionDef = getMutauionNode().getSubNodes().get(0);
                    Node functionCall = getMutauionNode().getSubNodes().get(1);
                    String tempName = BasicGrammar.randomName();
                    functionDef.setName(tempName);
                    functionCall.getSubNodes().get(0).getSubNodes().get(0).setName(tempName);
                    FunctionDefinition functionDefinition = (FunctionDefinition) functionDef;
                    functionDefinition.setStateMutability(stateMutability);
                    functionDefinition.setVisibility(visibility);
                    if (((ContractDefinition) contract).getContractKind().equals("interface")){
                        functionDefinition.getSubNodes().remove(functionDefinition.getSubNodes().size()-1);
                        functionDefinition.setVisibility("external");
                        contract.addSubNode(functionDefinition);
                        continue;
                    }
                    contract.addSubNode(functionDefinition);
                    for(String type : getAddTo()) {
                        ArrayList<Node> targetBlocks = getNodeType(type, contract);
                        for (Node target : targetBlocks) {
                            if(target.getName().equalsIgnoreCase(functionDef.getName())){
                                continue;
                            }
                            Node block = target.findSubNode(BasicGrammar.NodeType.Block.toString());
                            if(!block.getCode().isEmpty()){
                                block.addSubNode(functionCall);
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
