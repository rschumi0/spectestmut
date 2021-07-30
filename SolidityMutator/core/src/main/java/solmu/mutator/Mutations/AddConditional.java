package solmu.mutator.Mutations;

import solmu.common.BasicGrammar;
import solmu.grammer.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddConditional extends Mutation {
    public AddConditional(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(Node ast) {
        Boolean status = false;

        if(super.start(ast)){
            for(String type : getAddTo()){
                ArrayList<Node> targetBlocks = getNodeType(type, ast);
                for (Node target : targetBlocks) {
                    Node newNode = getMutauionNode();
                    Node block = target.findSubNode(BasicGrammar.NodeType.Block.toString());
                    if(block != null){
                        block.addSubNode(newNode);
                    }
                    status = true;
                }
            }
        }
        return status;
    }
}
