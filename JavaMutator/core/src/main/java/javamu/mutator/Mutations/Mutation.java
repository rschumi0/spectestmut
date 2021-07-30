package javamu.mutator.Mutations;

import com.github.javaparser.ast.stmt.BlockStmt;
import javamu.ASTparser.MyNode;
import javamu.common.BasicGrammar;
import javamu.mutator.NodeFinder;
import org.apache.log4j.Logger;

import java.util.*;

public  class Mutation {
    private String type;
    private String name;
    private Properties parameters;
    private List<String> addTo;
    private ArrayList<BasicGrammar.NodeType> acceptableTargets;
    private static final Logger logger =  Logger.getLogger(Mutation.class);
    private List<Mutation> mutations;

    public Mutation(String type, String name, Properties parameters, List<Mutation> mutations) {
        this.type = type;
        this.name = name;
        this.parameters = parameters;
        this.mutations = mutations;
        this.addTo = Arrays.asList(
                parameters.getOrDefault("addTo",BlockStmt.class.getSimpleName().toString()).toString().split(","));
                //parameters.getOrDefault("addTo","All").toString().split(","));
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

    public Boolean start(MyNode AST){
        if (getMutations() != null) {
            for(Mutation subMutation : getMutations()) {
                subMutation.start(AST);
            }
        }
        //if(MutationLibrary.BASIC_MUTATIONS.contains(getType())) {
        try {
            if(Class.forName("javamu.mutator.Mutations."+getType()) != null)
                logger.debug("Running Mutation: " + getName());
        } catch (ClassNotFoundException e) {
            logger.error("Not a basic Mutation");
            return false;
        }
        return true;

    }

    public ArrayList<MyNode> getNodeType(String input, MyNode ast){
        ArrayList<MyNode> output = new ArrayList<>();
        switch (input){
            case "Function-Block":
                return NodeFinder.find(BasicGrammar.NodeType.FunctionDefinition.toString(), ast);
            case "If-Block":
                return NodeFinder.find(BasicGrammar.NodeType.IfStatement.toString(), ast);
            case "For-Loop":
                return NodeFinder.find(BasicGrammar.NodeType.ForStatement.toString(), ast);
            case "While-Loop":
                return NodeFinder.find(BasicGrammar.NodeType.WhileStatement.toString(), ast);
            case "Modifier-Definition":
                return NodeFinder.find(BasicGrammar.NodeType.WhileStatement.toString(), ast);
            case "BlockStmt":
                return NodeFinder.find(BlockStmt.class.getSimpleName().toString(),ast);
            case "All":
                ArrayList<MyNode> allNodes = new ArrayList<>();
                for (BasicGrammar.NodeType targetType : this.acceptableTargets){
                    allNodes.addAll(NodeFinder.find(targetType.toString(), ast));
                }
                return allNodes;
            case "Random":
                ArrayList<BasicGrammar.NodeType> existingTargets = new ArrayList<>();
                for (BasicGrammar.NodeType targetType : this.acceptableTargets){
                    if(NodeFinder.find(targetType.toString(), ast).size() != 0){
                        existingTargets.add(targetType);
                    }
                }
                if(existingTargets.size() == 0) {
                    return new ArrayList<>();
                }
                BasicGrammar.NodeType outputType = existingTargets.get(new Random().nextInt(existingTargets.size()));
                logger.debug("Randomly picked: " + outputType.toString());
                return NodeFinder.find(outputType.toString(), ast);
            default:
                ArrayList<MyNode> all = new ArrayList<>();
                for (BasicGrammar.NodeType targetType : this.acceptableTargets){
                    all.addAll(NodeFinder.find(targetType.toString(), ast));
                }
                return all;
        }
    }
}
