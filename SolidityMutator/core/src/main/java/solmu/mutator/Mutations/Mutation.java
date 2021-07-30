package solmu.mutator.Mutations;

import org.apache.log4j.Logger;
import solmu.ASTparser.JsonParser;
import solmu.common.BasicGrammar;
import solmu.common.MutationLibrary;
import solmu.common.Util;
import solmu.grammer.Node;
import solmu.mutator.NodeFinder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public  class Mutation {
    private String type;
    private String name;
    private Properties parameters;
    private List<String> addTo;
    protected ArrayList<BasicGrammar.NodeType> acceptableTargets;
    private static final Logger logger =  Logger.getLogger(Mutation.class);
    protected List<Mutation> mutations;

    public Mutation(String type, String name, Properties parameters, List<Mutation> mutations) {
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.mutations = mutations;
        this.addTo = Arrays.asList(
                parameters.getOrDefault("addTo","All").toString().split(","));
        this.acceptableTargets = new ArrayList<>(Arrays.asList(
                BasicGrammar.NodeType.FunctionDefinition,
                BasicGrammar.NodeType.IfStatement,
                BasicGrammar.NodeType.ForStatement,
                BasicGrammar.NodeType.WhileStatement,
                BasicGrammar.NodeType.ModifierDefinition
                ));
    }

    public ArrayList<BasicGrammar.NodeType> getAcceptableTargets() {
        return acceptableTargets;
    }

    public List<String> getAddTo() {
        return addTo;
    }

    public void setAddTo(List<String> addTo) {
        this.addTo = addTo;
    }

    public void setAcceptableTargets(ArrayList<BasicGrammar.NodeType> acceptableTargets) {
        this.acceptableTargets = acceptableTargets;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Properties getParameters() {
        return parameters;
    }

    public void setParameters(Properties parameters) {
        this.parameters = parameters;
    }

    public List<Mutation> getMutations() {
        return mutations;
    }

    public void setMutations(List<Mutation> mutations) {
        this.mutations = mutations;
    }

    public Boolean start(Node AST){
        if (getMutations() != null) {
            for(Mutation subMutation : getMutations()) {
                subMutation.start(AST);
            }
        }
        if(MutationLibrary.BASIC_MUTATIONS.contains(getType())) {
            logger.debug("Running Mutation: " + getName());
            return true;
        }
        logger.error("Not a basic Mutation");
        return false;
    }

    public Node getMutauionNode() {
        return getMutauionNode(getType());
    }

    public static Node getMutauionNode(String type) {
        String dir = Paths.get("resources").toString();
        String file = Paths.get(dir, type + ".json").toString();
        String json = "";
        try {
            json = new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            logger.error(e);
        }
        if (Util.isNullOrEmplty(json)){
            logger.error("resources Dir is missing");
            return null;
        }
        JsonParser jsonParser = new JsonParser();
        Node mutationNode = jsonParser.parse(json);
        mutationNode.setId(-(new Random().nextInt(50)));
        return mutationNode;
    }

    public ArrayList<Node> getNodeType(String input, Node ast){
        ArrayList<Node> output = new ArrayList<>();
        System.out.println(input);
        switch (input){

            case "Function-Block":
                return NodeFinder.find(BasicGrammar.NodeType.FunctionDefinition, ast);
            case "If-Block":
                return NodeFinder.find(BasicGrammar.NodeType.IfStatement, ast);
            case "For-Loop":
                return NodeFinder.find(BasicGrammar.NodeType.ForStatement, ast);
            case "While-Loop":
                return NodeFinder.find(BasicGrammar.NodeType.WhileStatement, ast);
            case "Modifier-Definition":
                return NodeFinder.find(BasicGrammar.NodeType.WhileStatement, ast);
            case "All":
                ArrayList<Node> allNodes = new ArrayList<>();
                for (BasicGrammar.NodeType targetType : this.acceptableTargets){
                    allNodes.addAll(NodeFinder.find(targetType, ast));
                }
                return allNodes;
            case "Random":
                ArrayList<BasicGrammar.NodeType> existingTargets = new ArrayList<>();
                for (BasicGrammar.NodeType targetType : this.acceptableTargets){
                    if(NodeFinder.find(targetType, ast).size() != 0){
                        existingTargets.add(targetType);
                    }
                }
                if(existingTargets.size() == 0) {
                    return new ArrayList<>();
                }
                BasicGrammar.NodeType outputType = existingTargets.get(new Random().nextInt(existingTargets.size()));
                logger.debug("Randomly picked: " + outputType.toString());
                return NodeFinder.find(outputType, ast);
            default:
                ArrayList<Node> all = new ArrayList<>();
                for (BasicGrammar.NodeType targetType : this.acceptableTargets){
                    all.addAll(NodeFinder.find(targetType, ast));
                }
                return all;
        }
    }
}
