package solmu.mutator.Mutations;

import org.apache.commons.lang.RandomStringUtils;
import solmu.common.BasicGrammar;
import solmu.core.Main;
import solmu.grammer.*;
import solmu.mutator.NodeFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class AddArrayAndCheck extends Mutation {
    Random rand;
    public AddArrayAndCheck(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
        rand = new Random();
    }

    @Override
    public Boolean start(Node AST) {
        if(super.start(AST)) {
            ArrayList<Node> contracts = NodeFinder.find(BasicGrammar.NodeType.ContractDefinition, AST);
            for (Node contract : contracts) {

                Node functionDef = getMutauionNode("AddFunctionDef").getSubNodes().get(0);
                Node functionCall = getMutauionNode("AddFunctionDef").getSubNodes().get(1);
                String tempName = BasicGrammar.randomName();
                functionDef.setName(tempName);
                functionCall.getSubNodes().get(0).getSubNodes().get(0).setName(tempName);
                FunctionDefinition functionDefinition = (FunctionDefinition) functionDef;
                //functionDefinition.setStateMutability("pure");
                functionDefinition.setStateMutability("returns (uint8)");
                functionDefinition.setVisibility("public");
                if (((ContractDefinition) contract).getContractKind().equals("interface")){
                    functionDefinition.getSubNodes().remove(functionDefinition.getSubNodes().size()-1);
                    functionDefinition.setVisibility("external");
                    contract.addSubNode(functionDefinition);
                    continue;
                }
                Node block = functionDefinition.findSubNode(BasicGrammar.NodeType.Block.toString());

                int nested = rand.nextInt(6)+1;

                String type = getParameters().getOrDefault("type","uint8").toString();
                ArrayList<Integer> sizes = new ArrayList<>();
                ArrayList<Object> values = new ArrayList<>();
                String access = "";
                for (int i=0; i < nested; i++){
                    int size = rand.nextInt(2)+2;

                    type += "["+size+"]";
                    sizes.add(size);
                    access = "["+rand.nextInt(size)+"]" + access;
                }
                String name = getParameters().getOrDefault("name","a"+ RandomStringUtils.randomAlphanumeric(5)).toString();
                String value = getParameters().getOrDefault("value","").toString();
                value = nestedArrayConstruction("int",nested,rand,sizes,values);
                String accessValue = getAccessValue(values,access);
                access = name+access;
                System.out.println(access + "####"+accessValue);

                System.out.println("---------------------------------Values");
                //printVals(values);
                System.out.println(type+ "    " +value);

                Boolean isvariable= (Boolean) getParameters().getOrDefault("variable",true);


                Node newNode = getMutauionNode("AddArray");
                //Set constant type
                newNode.getSubNodes().get(0).setName(type);
                //Set constant value
                newNode.getSubNodes().remove(1);
                //newNode.getSubNodes().get(1).setName(value);

                VariableDeclaration variableDeclaration = (VariableDeclaration) newNode;
                variableDeclaration.setConstant(!isvariable);
                variableDeclaration.setName(name);

                Node newNode1 = getMutauionNode("AddArray");
                //Set constant type
                newNode1.getSubNodes().get(0).setName("");
                //Set constant value
                newNode1.getSubNodes().get(1).setName(value);

                VariableDeclaration variableDeclaration1 = (VariableDeclaration) newNode1;
                variableDeclaration1.setConstant(!isvariable);
                variableDeclaration1.setName(name);

                Node retNode = getMutauionNode("AddReturn");
                //Set constant type
                Return ret = (Return) retNode;
                ret.getSubNodes().get(0).setName(access);

                //Set constant value


                contract.addSubNode(variableDeclaration);
                block.addSubNode(ret);
                block.addSubNode(variableDeclaration1);

                contract.addSubNode(functionDefinition);

                System.out.println(variableDeclaration.getCode() + "\n"+functionDefinition.getCode());
                Main.TestFuctions.add( variableDeclaration.getCode() + "\n"+functionDefinition.getCode().replace(access+";", access+"; // "+accessValue )+ "\n");

                String testString = "  it('" + "Test for function " + tempName + "', async () => {\n" +
                        "    const metaCoinInstance = await MetaCoin.deployed();\n" +
                        "\t\n" +
                        "    await metaCoinInstance." + tempName + ".call(function(error, returned) {\n" +
                        "\t  if(!error) {\n" +
                        "\t    myassert(returned.toString(10), \""+accessValue+"\", \"Test for function " + tempName + " \");\n" +
                        "\t  } else {\n" +
                        "\t    console.error(error);\n" +
                        "\t  }});"+
                        //"    assert.equal(res,\""+mGen.getCompRes().toString()+"\");\n" +
                        "\n" +
                        "  });\n";

                Main.TestCalls.add(testString);

            }
            return true;
        }
        return false;
    }
    String nestedArrayConstruction(String type, int nesting, Random rand, ArrayList<Integer> sizes,ArrayList<Object> values){
        String ret = "";
//        int elementCnt = rand.nextInt((3 - 1) + 1)  + 1;
//        if(nestedArraySizes.containsKey(nesting)){
//            nestedArraySizes.put(nesting,Math.min(nestedArraySizes.get(nesting),elementCnt));
//        }
//        else {
//            nestedArraySizes.put(nesting, elementCnt);

        nesting = nesting - 1;
        int size = sizes.get(nesting);
        if(nesting == 0) {
            for(int i = 0; i < size;i++){
                String el = randomElement(type, rand);
                values.add(el);
                ret +=  el + ",";
            }
        }
        else if(nesting > 0){
            for(int i = 0; i < size;i++){
                ArrayList<Object> valuesNew = new ArrayList<>();
                values.add(valuesNew);
                ret +=  nestedArrayConstruction(type,nesting,rand, sizes,valuesNew) + ",";
            }
        }
        else{
            assert false;
        }

        return "["+ret.substring(0,ret.length()-1)+"]";
    }
    private String getAccessValue(ArrayList<Object> values,String access){
        if(access == null || access.isEmpty() || values == null || values.isEmpty())
        {
            return "";
        }
        access = access.trim().substring(0,access.length() - 1).replaceFirst("\\[","");
        String[] ids = access.split("\\]\\[");
        Object tempValue = values;
        for (String id: ids) {
            Object val = ((ArrayList<Object> )tempValue).get(Integer.parseInt(id));
            if(val instanceof String) {
                return (String)val;
            }
            else
            {
                tempValue = val;
            }
        }
        return "";
    }
    private void printVals(ArrayList<Object> values){
        for (Object o: values) {
            if(o instanceof String){
                System.out.print(o);
            }
            else {
                System.out.print("[");
                printVals((ArrayList<Object>) o);
                System.out.print("]");
            }
        }
    }

    private String randomElement(String type,Random rand){
        switch (type){
            //case "int": return ""+rand.nextInt();
            case "String": return "\""+RandomStringUtils.randomAlphanumeric(rand.nextInt(3)+1)+"\"";
            case "boolean": return ""+rand.nextBoolean();
            case "float": return ""+rand.nextFloat()+"f";
            case "double": return ""+rand.nextDouble();
            default: return ""+rand.nextInt(11);//+rand.nextInt();
        }
    }
}
