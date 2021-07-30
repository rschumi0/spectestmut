package javamu.mutator.Mutations;

import javamu.ASTparser.MyNode;
import javamu.mutator.NodeFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddFunctionDef extends Mutation {
    public AddFunctionDef(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(MyNode AST) {
        if(super.start(AST)) {
            ArrayList<MyNode> classes = NodeFinder.find("class", AST);
            int count =Integer.parseInt(getParameters().getOrDefault("count",1).toString());
            String stateMutability =
                    getParameters().getOrDefault("stateMutability","pure").toString();
            String visibility = "internal";
            for(int i = 0; i < count; i++) {
                /*for (MyNode cls : classes) {

                    MyNode functionDef = getMutauionNode().getSubNodes().get(0);
                    MyNode functionCall = getMutauionNode().getSubNodes().get(1);
                    String tempName = BasicGrammar.randomName();
                    functionDef.setName(tempName);
                    functionCall.getSubNodes().get(0).getSubNodes().get(0).setName(tempName);
                    FunctionDefinition functionDefinition = (FunctionDefinition) functionDef;
                    functionDefinition.setStateMutability(stateMutability);
                    functionDefinition.setVisibility(visibility);
                    if (((ContractDefinition) cls).getContractKind().equals("interface")){
                        functionDefinition.getSubNodes().remove(functionDefinition.getSubNodes().size()-1);
                        functionDefinition.setVisibility("external");
                        cls.addSubNode(functionDefinition);
                        continue;
                    }
                    cls.addSubNode(functionDefinition);
                    for(String type : getAddTo()) {
                        ArrayList<MyNode> targetBlocks = getNodeType(type, cls);
                        for (MyNode target : targetBlocks) {
                            if(target.getName().equalsIgnoreCase(functionDef.getName())){
                                continue;
                            }
                            MyNode block = target.findSubNode(BasicGrammar.NodeType.Block.toString());
                            if(!block.getCode().isEmpty()){
                                block.addSubNode(functionCall);
                            }
                        }
                    }
                }*/
            }
            return true;
        }
        return false;
    }
}
