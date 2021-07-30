package solmu.mutator.Mutations;

import org.apache.commons.lang.RandomStringUtils;
import solmu.common.BasicGrammar;
import solmu.core.Main;
import solmu.gen.BoolExprGenerator;
import solmu.grammer.*;
import solmu.mutator.NodeFinder;

import java.util.*;

public class AddConditions extends Mutation {
    Random rand;
    public AddConditions(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
        rand = new Random();
    }

    @Override
    public Boolean start(Node AST) {
        if(super.start(AST)) {
            ArrayList<Node> contracts = NodeFinder.find(BasicGrammar.NodeType.ContractDefinition, AST);
            for (Node contract : contracts) {
                for(int x=0;x < 2;x++) {

                    String type = "int";
                    Node functionDef = getMutauionNode("AddFunctionDef").getSubNodes().get(0);
                    Node functionCall = getMutauionNode("AddFunctionDef").getSubNodes().get(1);
                    String tempName = BasicGrammar.randomName();
                    functionDef.setName(tempName);
                    functionCall.getSubNodes().get(0).getSubNodes().get(0).setName(tempName);
                    FunctionDefinition functionDefinition = (FunctionDefinition) functionDef;
                    //functionDefinition.setStateMutability("pure");
                    functionDefinition.setStateMutability("returns (" + type + ")");
                    functionDefinition.setVisibility("public");
                    if (((ContractDefinition) contract).getContractKind().equals("interface")) {
                        functionDefinition.getSubNodes().remove(functionDefinition.getSubNodes().size() - 1);
                        functionDefinition.setVisibility("external");
                        contract.addSubNode(functionDefinition);
                        continue;
                    }
                    Node block = functionDefinition.findSubNode(BasicGrammar.NodeType.Block.toString());

                    BoolExprGenerator bGen = new BoolExprGenerator(rand);
                    Map<String, String> vars = bGen.genVarsWithInitStatement(4, block);
                    String compString = bGen.genBoolString(vars, 1, 4);


                    Node newNode = getMutauionNode("AddConstant");
                    newNode.getSubNodes().get(0).setName("bool");

                    newNode.getSubNodes().get(1).setName(compString);

                    VariableDeclaration variableDeclaration = (VariableDeclaration) newNode;
                    String name = "v" + RandomStringUtils.randomAlphanumeric(5);
                    variableDeclaration.setConstant(false);
                    variableDeclaration.setName(name);
                    block.getSubNodes().add(block.getSubNodes().size(), variableDeclaration);

//                    for (String var : bGen.getVarValues().keySet()) {
//                        Node newNode1 = getMutauionNode("AddAssert");
//                        newNode1.getSubNodes().get(0).getSubNodes().get(1).setName(var + " == " + bGen.getVarValues().get(var).toString());
//                        block.getSubNodes().add(block.getSubNodes().size(), newNode1);
//                    }


                    Node retNode = getMutauionNode("AddReturn");
                    Return ret = (Return) retNode;


                    ret.getSubNodes().get(0).setName("1");


                    block.getSubNodes().add(block.getSubNodes().size(), ret);

                    contract.addSubNode(functionDefinition);

                    System.out.println(functionDefinition.getCode());

//                    try {
//                        String filename = "TestFunctions.txt";
//                        FileWriter fw = new FileWriter(filename, true); //the true will append the new data
//                        fw.write(functionDefinition.getCode());//appends the string to the file
//                        fw.close();
//                    } catch (IOException ioe) {
//                        System.err.println("IOException: " + ioe.getMessage());
//                    }
                    Main.TestFuctions.add(functionDefinition.getCode()+ "\n");
                    String testString = "  it('" + "Test for function " + tempName + "', async () => {\n" +
                            "    const metaCoinInstance = await MetaCoin.deployed();\n" +
                            "\t\n" +
                            "    const    res = (await metaCoinInstance." + tempName + ".call()).toNumber();\n" +
                            "    assert.equal(res,1);\n" +
                            "\n" +
                            "  });\n";
                    Main.TestCalls.add(testString);
//                    try {
//                        String filename = "TestCalls.txt";
//                        FileWriter fw = new FileWriter(filename, true); //the true will append the new data
//                        fw.write(testString);//appends the string to the file
//                        fw.close();
//                        System.out.println("write done!");
//                    } catch (IOException ioe) {
//                        System.err.println("IOException: " + ioe.getMessage());
//                    }


                }

            }
            return true;
        }
        return false;
    }



}
