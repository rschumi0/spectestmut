package solmu.mutator.Mutations;

import solmu.common.BasicGrammar;
import solmu.grammer.Node;
import solmu.grammer.VariableDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddConstant extends Mutation {
    public AddConstant(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(Node AST) {
        if(super.start(AST)) {
            String type = getParameters().getOrDefault("type","uint").toString();
            String value = getParameters().getOrDefault("value","32").toString();
            String name = getParameters().getOrDefault("name", BasicGrammar.randomName()).toString();
            Boolean isvariable= (Boolean) getParameters().getOrDefault("variable",false);


//            ArrayList<Node> contracts = NodeFinder.find(BasicGrammar.NodeType.ContractDefinition, AST);
//            for (Node contract : contracts) {
//                ArrayList<Node> targetBlocks = getNodeType(type, AST);
//                for (Node target : targetBlocks) {

            for(String t : getAddTo()) {
                ArrayList<Node> targetBlocks = getNodeType(t, AST);
                for (Node target : targetBlocks) {

                    Node newNode = getMutauionNode();
                    //Set constant type
                    newNode.getSubNodes().get(0).setName(type);
                    //Set constant value
                    newNode.getSubNodes().get(1).setName(value);

                    VariableDeclaration variableDeclaration = (VariableDeclaration) newNode;
                    variableDeclaration.setConstant(!isvariable);
                    variableDeclaration.setName(name);

                    Node block = target.findSubNode(BasicGrammar.NodeType.Block.toString());
                    if(!block.getCode().isEmpty()){
                        block.addSubNode(variableDeclaration);
                    }
                }
            }
            return true;
        }
        return false;
    }
}
