package solmu.mutator.Mutations;

import org.apache.commons.lang.RandomStringUtils;
import solmu.common.BasicGrammar;
import solmu.grammer.Node;
import solmu.grammer.VariableDeclaration;
import solmu.mutator.NodeFinder;

import java.util.*;

public class AddArray extends Mutation {
    Random rand;
    public AddArray(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
        rand = new Random();
    }

    @Override
    public Boolean start(Node AST) {
        if(super.start(AST)) {


            ArrayList<Node> contracts = NodeFinder.find(BasicGrammar.NodeType.ContractDefinition, AST);
            for (Node contract : contracts) {

                int nested = rand.nextInt(3)+1;

                String type = getParameters().getOrDefault("type","uint8").toString();
                ArrayList<Integer> sizes = new ArrayList<>();
                for (int i=0; i < nested; i++){
                    int size = rand.nextInt(2)+2;

                    type += "["+size+"]";
                    sizes.add(size);
                    // access += "[0]";
                }
                String value = getParameters().getOrDefault("value","").toString();
                value = nestedArrayConstruction("int",nested,rand,sizes);
                System.out.println(type+ "    " +value);
                String name = getParameters().getOrDefault("name","a"+ RandomStringUtils.randomAlphanumeric(5)).toString();
                Boolean isvariable= (Boolean) getParameters().getOrDefault("variable",true);


                Node newNode = getMutauionNode();
                //Set constant type
                newNode.getSubNodes().get(0).setName(type);
                //Set constant value
                newNode.getSubNodes().get(1).setName(value);

                VariableDeclaration variableDeclaration = (VariableDeclaration) newNode;
                variableDeclaration.setConstant(!isvariable);
                variableDeclaration.setName(name);
                contract.addSubNode(variableDeclaration);
            }
            return true;
        }
        return false;
    }
    String nestedArrayConstruction(String type, int nesting, Random rand, ArrayList<Integer> sizes){
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
                ret +=  randomElement(type, rand) + ",";
            }
        }
        else if(nesting > 0){
            for(int i = 0; i < size;i++){
                ret +=  nestedArrayConstruction(type,nesting,rand, sizes) + ",";
            }
        }
        else{
            assert false;
        }

        return "["+ret.substring(0,ret.length()-1)+"]";
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
