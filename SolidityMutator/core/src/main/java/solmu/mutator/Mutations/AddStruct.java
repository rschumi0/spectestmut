package solmu.mutator.Mutations;

import org.apache.commons.lang.RandomStringUtils;
import solmu.common.BasicGrammar;
import solmu.grammer.Node;
import solmu.grammer.StructDefinition;
import solmu.grammer.VariableDeclaration;
import solmu.mutator.NodeFinder;

import java.util.*;

public class AddStruct extends Mutation {
    Random rand;
    public AddStruct(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
        rand = new Random();
    }

    @Override
    public Boolean start(Node AST) {
        if(super.start(AST)) {


            ArrayList<Node> contracts = NodeFinder.find(BasicGrammar.NodeType.ContractDefinition, AST);
            for (Node contract : contracts) {

                int size = rand.nextInt(3)+1;

                String type = getParameters().getOrDefault("type","int").toString();

//                for (int i=0; i < size; i++){
//                    String[] types= {"String", "boolean", "float", "double", "int"};
//                    String t = types[rand.nextInt(types.length)];
//
//                    type += "["+size+"]";
//                }
                //String value = getParameters().getOrDefault("value","").toString();
                //value = nestedArrayConstruction("int",nested,rand,sizes);
                //System.out.println(type+ "    " +value);
                String name = getParameters().getOrDefault("name","a"+ RandomStringUtils.randomAlphanumeric(5)).toString();
                Boolean isvariable= (Boolean) getParameters().getOrDefault("variable",true);


                Node newNode = getMutauionNode();
                //Set constant type
                //newNode.getSubNodes().get(1).setName("asdfasdf");
                //Set constant value
                //newNode.getSubNodes().get(1).setType("int");
                //ArrayList<Node> subNodes = new ArrayList<>();

                //Set constant type

                ArrayList<String> structVarTypes = new ArrayList<>();
                String[] types= {"bool", "int", "uint","string"};//, "bytes"};//,"address"};
                for(int i = 0; i < 10; i++){
                    Node n = getMutauionNode("VarDecl");


                    type = types[rand.nextInt(types.length)];
                    structVarTypes.add(type);
                    //n.setType(type);
                   // n.getSubNodes().get(0).getAttributes().get(0).setName(type);
                    n.setName("v"+ RandomStringUtils.randomAlphanumeric(5));
                    n.setId(1000+i);
                    n.getSubNodes().get(0).setName(type);

                    //n.getSubNodes().get(0).getAttributes().put("name",type);
                    System.out.println(n.getCode());
                    newNode.addSubNode(n);
                    //subNodes.add(n);
                    System.out.println(type);

                }


                StructDefinition sd = (StructDefinition) newNode;
                contract.addSubNode(sd);
                System.out.println(sd.getCode().toString());

//                Properties params = new Properties();
//                params.setProperty("type","Asdf memory");
//                params.setProperty("value", "");
//                this.mutations.add(new AddConstant("AddConstant","test",params,null));

                Node structInit= getMutauionNode("AddConstant");

                structInit.getSubNodes().get(0).setName("Asdf memory");
                //Set constant value

                String value = "Asdf(";//"Asdf(0,0,0,0,0,0,0,0,0,0)";
                for(String t: structVarTypes){
                    value += randomElement(t,rand)+",";
                }
                structVarTypes.clear();

                value = value.substring(0, value.length() - 1) + ")";
                structInit.getSubNodes().get(1).setName(value);
                System.out.println(value);

                VariableDeclaration variableDeclaration = (VariableDeclaration) structInit;
                variableDeclaration.setConstant(false);
                variableDeclaration.setName("a"+ RandomStringUtils.randomAlphanumeric(5));


                for(String t : getAddTo()) {
                    ArrayList<Node> targetBlocks = getNodeType(t, contract);
                    for (Node target : targetBlocks) {
                        Node block = target.findSubNode(BasicGrammar.NodeType.Block.toString());
                        if(!block.getCode().isEmpty()){
                            block.addSubNode(variableDeclaration);
                        }
                    }
                }


            }
            return true;
        }
        return false;
    }


    private String randomElement(String type,Random rand){
        switch (type){
            //case "int": return ""+rand.nextInt();
            case "string": return "\""+RandomStringUtils.randomAlphanumeric(rand.nextInt(3)+1)+"\"";
            case "bool": return ""+rand.nextBoolean();
            case "uint": return ""+Math.abs(rand.nextInt());
            //case "address": return "0x"+getRandomHexString(40);
            case "bytes": return "0x"+getRandomHexString(rand.nextInt(102)+1);
            // "float": return ""+rand.nextFloat()+"f";
            //case "double": return ""+rand.nextDouble();
            default: return ""+rand.nextInt();//rand.nextInt(11);//+rand.nextInt();
        }
    }
    private String getRandomHexString(int numchars){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, numchars);
    }
}
