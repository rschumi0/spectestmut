package solmu.mutator.Mutations;

import solmu.common.BasicGrammar;
import solmu.grammer.ContractDefinition;
import solmu.grammer.Node;
import solmu.mutator.NodeFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddMapping extends Mutation {
    public AddMapping(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(Node AST) {
        if(super.start(AST)) {
            ArrayList<Node> contracts = NodeFinder.find(BasicGrammar.NodeType.ContractDefinition, AST);
            for (Node node : contracts){
                ContractDefinition contract = (ContractDefinition) node;
                if (contract.getContractKind().equalsIgnoreCase("contract")){
                    Node newNode = getMutauionNode();
                    newNode.setName(BasicGrammar.randomName());
                    contract.addSubNode(newNode);
                }

            }
            return true;
        }
        return false;
    }
}
