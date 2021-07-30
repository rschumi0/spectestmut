package solmu.mutator.Mutations;

import org.apache.commons.lang.RandomStringUtils;
import solmu.common.BasicGrammar;
import solmu.core.Main;
import solmu.gen.MathExprGenerator;
import solmu.grammer.*;
import solmu.mutator.NodeFinder;

import java.util.*;

public class AddComputations extends Mutation {
    Random rand;
    public AddComputations(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
        rand = new Random();
    }

    @Override
    public Boolean start(Node AST) {
        if(super.start(AST)) {
            ArrayList<Node> contracts = NodeFinder.find(BasicGrammar.NodeType.ContractDefinition, AST);
            for (Node contract : contracts) {
                for(int x=0;x < 1;x++) {
                    String[] types = {"uint", "int"};
                    String type = types[rand.nextInt(types.length)];
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


                    /*ArrayList<String> vars = new ArrayList<>();
                    for (int i = 0; i < rand.nextInt(4) + 2; i++) {
                        int typePostfix = (rand.nextInt(33) * 8);
                        Node newNode = getMutauionNode("AddConstant");
                        newNode.getSubNodes().get(0).setName(type + (typePostfix == 0 ? "" : typePostfix));
                        newNode.getSubNodes().get(1).setName(randomElement(type + (typePostfix == 0 ? "" : typePostfix), rand));

                        VariableDeclaration variableDeclaration = (VariableDeclaration) newNode;
                        String name = "v" + RandomStringUtils.randomAlphanumeric(5);
                        variableDeclaration.setConstant(false);
                        variableDeclaration.setName(name);
                        vars.add(name);
                        block.addSubNode(variableDeclaration);
                    }

                    Node retNode = getMutauionNode("AddReturn");
                    //Set constant type
                    Return ret = (Return) retNode;


                    String compString = "";//"(";
                    int i = 0;
                    ArrayList<String> ops = new ArrayList<>(Arrays.asList("+", "-", "*", "/")); //,"%"));
//                if(type.equals("uint")){
//                    ops.add("**");
//                }


                    for (String var : vars) {
                        String prefix = "";
                        String postfix = "";
                        if (rand.nextInt(10) < 3) {
                            //var = ""+(rand.nextInt() & Integer.MAX_VALUE); ;//randomElement(types[rand.nextInt(types.length)],rand);
                            var = randomElement(type, rand);
                        } else {
                        }
                        if (i == vars.size() - 1) {
                            compString += prefix + var + postfix;// + ")";
                        } else {
                            compString += prefix + var + postfix + " " + ops.get(rand.nextInt(ops.size())) + " ";
                        }
                        i++;
                    }
                    ret.getSubNodes().get(0).setName(compString);*/

                    MathExprGenerator mGen;
                    String resVar;

                    mGen = new MathExprGenerator(rand);
                    Map<String, String> vars = mGen.genVarsWithInitStatement(3, block,type);
                    String compString = mGen.generateCompString(vars, 2,0);
                    Node newNode = getMutauionNode("AddConstant");
                    newNode.getSubNodes().get(0).setName(type);
                    newNode.getSubNodes().get(1).setName(compString);
                    VariableDeclaration variableDeclaration = (VariableDeclaration) newNode;
                    resVar = "v" + RandomStringUtils.randomAlphanumeric(5);
                    variableDeclaration.setConstant(false);
                    variableDeclaration.setName(resVar);

//                    Node newNode;
//                    boolean calcSuccess = true;
//                    do {
//                        mGen = new MathExprGenerator(rand);
//                        Map<String, String> vars = mGen.genVarsWithInitStatement(6, block,type);
//                        resVar = vars.keySet().iterator().next();
//                        BigInteger resValue = mGen.getVarValues().get(resVar);
//
//                        vars.remove(resVar);//TODO remove
//                        String compString = mGen.generateCompString(vars, 3,2);//TODO change// 2);
//                        String[] assignOps = new String[]{"="};//, "+=", "-=", "*=", "/=", "%=", "<<=", ">>=", "&=", "^=", "|="};
//                        newNode = getMutauionNode("addExpressionStmt");
//                        JSONObject o = newNode.getSubNodes().get(0).getAttributes();
//                        String assignOp = assignOps[rand.nextInt(assignOps.length)];
//                        System.out.println(newNode.getSubNodes().get(0).getCode());
//                        //System.out.println(newNode.getAttributes().toString());
//                        System.out.println(newNode.getSubNodes().get(0).getAttributes().toString());
//                        System.out.println(newNode.getSubNodes().get(0).getSubNodes().get(0).getAttributes().toString());
//
//                        System.out.println("assignOp " + assignOp);
//                        o.put("operator", assignOp);
//                        newNode.getSubNodes().get(0).setAttributes(o);
//                        newNode.getSubNodes().get(0).setJsonObject(newNode.getSubNodes().get(0).getJsonObject());
//
//                        JsonParser jsonParser = new JsonParser();
//                        newNode = jsonParser.parse(newNode.getJsonObject().toJSONString());
//                        System.out.println(newNode.getSubNodes().get(0).getJsonObject());
//
//                        newNode.getSubNodes().get(0).setAttributes(o);
//                        newNode.getSubNodes().get(0).getSubNodes().get(0).setName(resVar);
//                        newNode.getSubNodes().get(0).getSubNodes().get(1).setName(compString);
//
//                        calcSuccess = true;
//                        if (!assignOp.equals("=")) {
//                            ArrayList<BigInteger> vals = new ArrayList<>();
//                            vals.add(resValue);
//
//                            vals.add(mGen.getCompRes());
//                            ArrayList<String> ops = new ArrayList<>();
//                            ops.add(assignOp.replace("=", ""));
//
//                            System.out.println("--------- " + vals.get(0) + " " + ops.get(0) + " " + vals.get(1));
//                            calcSuccess =mGen.calcCompRes(vals, ops, type);
//
//                            System.out.println("--------- Res " + mGen.getCompRes());
//                            mGen.getVarValues().put(resVar, mGen.getCompRes());
//                        }
//                    } while(!calcSuccess);
                    block.getSubNodes().add(block.getSubNodes().size(), newNode);




//                    for (String var : mGen.getVarValues().keySet()) {
//                        Node newNode1 = getMutauionNode("AddAssert");
//                        newNode1.getSubNodes().get(0).getSubNodes().get(1).setName(var + " == " + mGen.getVarValues().get(var).toString());
//                        block.getSubNodes().add(block.getSubNodes().size(), newNode1);
//                    }


                    Node retNode = getMutauionNode("AddReturn");
                    Return ret = (Return) retNode;


                    ret.getSubNodes().get(0).setName(""+resVar+"");


                    block.getSubNodes().add(block.getSubNodes().size(), ret);

                    contract.addSubNode(functionDefinition);

                    System.out.println(functionDefinition.getCode());



                    String res = mGen.getCompRes().toString();
//                    if(type.equals("uint")){
//                        res = mGen.getCompRes().mod(BigInteger.ONE.add(BigInteger.ONE).pow(256)).toString();
//                    }
//                    else{
//                        res = trimBits(mGen.getCompRes(),256).toString();
//                    }
                    //res = trimBits(mGen.getCompRes(),256).toString();
                    //res = mGen.getCompRes().and(BigInteger.ONE.shiftLeft(256).subtract(BigInteger.ONE)).toString();

//                    else
//                    {
//                        res = mGen.getCompRes().mod(BigInteger.ONE.add(BigInteger.ONE).pow(256).divide(BigInteger.ONE.add(BigInteger.ONE))).toString();
//                    }
//                    if(type.equals("uint") && res.startsWith("-")){
//
//                        res = parseBigIntegerPositive(res,256).toString();
//                    }



                    Main.TestFuctions.add(functionDefinition.getCode().replace(resVar+";", resVar+"; // "+res )+ "\n");
//                    try {
//                        String filename = "TestFunctions.txt";
//                        FileWriter fw = new FileWriter(filename, true); //the true will append the new data
//                        fw.write(functionDefinition.getCode());//appends the string to the file
//                        fw.close();
//                    } catch (IOException ioe) {
//                        System.err.println("IOException: " + ioe.getMessage());
//                    }




                    String testString = "  it('" + "Test for function " + tempName + "', async () => {\n" +
                            "    const metaCoinInstance = await MetaCoin.deployed();\n" +
                            "\t\n" +
                            "    await metaCoinInstance." + tempName + ".call(function(error, returned) {\n" +
                            "\t  if(!error) {\n" +
                            "\t    myassert(returned.toString(10), \""+res+"\", \"Test for function " + tempName + " \");\n" +
                            "\t  } else {\n" +
                            "\t    console.error(error);\n" +
                            "\t  }});"+
                            //"    assert.equal(res,\""+mGen.getCompRes().toString()+"\");\n" +
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
