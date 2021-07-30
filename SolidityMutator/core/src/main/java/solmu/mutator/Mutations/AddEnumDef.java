package solmu.mutator.Mutations;

import solmu.common.BasicGrammar;
import solmu.grammer.Node;
import solmu.mutator.NodeFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddEnumDef extends Mutation {
    public AddEnumDef(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(Node AST) {
        if(super.start(AST)) {
            ArrayList<Node> contracts = NodeFinder.find(BasicGrammar.NodeType.ContractDefinition, AST);
            int count =Integer.parseInt(getParameters().getOrDefault("count",1).toString());
            for(int i = 0; i < count; i++) {
                for (Node contract : contracts) {
                    String name = getParameters().getOrDefault("name",BasicGrammar.randomName()).toString();
                    Node newNode = getMutauionNode();
                    newNode.setName(name);
                    contract.addSubNode(newNode);
                }
            }
            return true;
        }
        return false;
    }
}
