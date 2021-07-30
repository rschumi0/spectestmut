package solmu.mutator.Mutations;

import solmu.common.BasicGrammar;
import solmu.grammer.Literal;
import solmu.grammer.Node;
import solmu.mutator.NodeFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddRequire extends Mutation {
    public AddRequire(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(Node AST) {
        if(super.start(AST)) {
            ArrayList<Node> contracts = NodeFinder.find(BasicGrammar.NodeType.ContractDefinition, AST);
            String checkValue = getParameters().getOrDefault("check","true").toString();
            for (Node contract : contracts) {
                Node functionCall = getMutauionNode();
                String tempName = BasicGrammar.randomName();
                Literal check = (Literal) functionCall.getSubNodes().get(0).getSubNodes().get(1);
                check.setName(checkValue);
                for(String type : getAddTo()) {
                    ArrayList<Node> targetBlocks = getNodeType(type, contract);
                    for (Node target : targetBlocks) {

                        Node block = target.findSubNode(BasicGrammar.NodeType.Block.toString());
                        if(!block.getCode().isEmpty()){
                            block.addSubNode(functionCall);
                        }
                    }
                }
            }

            return true;
        }
        return false;
    }
}
