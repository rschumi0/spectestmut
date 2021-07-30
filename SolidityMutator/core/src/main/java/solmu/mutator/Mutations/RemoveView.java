package solmu.mutator.Mutations;

import solmu.common.BasicGrammar;
import solmu.grammer.FunctionDefinition;
import solmu.grammer.Node;
import solmu.mutator.NodeFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RemoveView extends Mutation {
    public RemoveView(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(Node AST) {
        if(super.start(AST)) {
            ArrayList<Node> functions = NodeFinder.find(BasicGrammar.NodeType.FunctionDefinition, AST);

            for (Node function : functions) {
                FunctionDefinition functionDefinition = (FunctionDefinition) function;
                if(functionDefinition.getStateMutability().equals("view")){
                    functionDefinition.setStateMutability("");
                }
            }
        }
        return true;
    }
}
