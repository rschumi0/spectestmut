package javamu.ASTparser;

import javamu.grammer.*;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import java.util.ArrayList;

public class NodeFactory {
    private String type;
    private ArrayList<OldNode> innerNodes;
    private JSONObject node;
    private static final Logger logger =  Logger.getLogger(NodeFactory.class);


    public NodeFactory(JSONObject node, ArrayList<OldNode> innerNodes) {
        this.type = node.get("name").toString();
        this.innerNodes = innerNodes;
        this.node = node;
    }

    public OldNode getNode() {
        switch (type) {
            case "ElementaryTypeName" :
                return new ElementaryTypeName(node, innerNodes);
            case "VariableDeclaration" :
                return new VariableDeclaration(node, innerNodes);
            case "ContractDefinition" :
                return new ContractDefinition(node, innerNodes);
            case "StructDefinition" :
                return new StructDefinition(node, innerNodes);
            case "Mapping" :
                return new Mapping(node, innerNodes);
            case "TupleExpression" :
                return new TupleExpression(node, innerNodes);
            case "UserDefinedTypeName" :
                return new UserDefinedTypeName(node, innerNodes);
            case "ArrayTypeName" :
                return new ArrayTypeName(node, innerNodes);
            case "FunctionDefinition" :
                return new FunctionDefinition(node, innerNodes);
            case "ParameterList" :
                return new ParameterList(node, innerNodes);
            case "StateMutability" :
                return new StateMutability(node, innerNodes);
            case "ModifierInvocation" :
                return new ModifierInvocation(node, innerNodes);
            case "Identifier" :
                return new Identifier(node, innerNodes);
            case "EnumValue" :
                return new EnumValue(node, innerNodes);
            case "Block" :
                return new Block(node, innerNodes);
            case "ImportDirective" :
                return new ImportDirective(node, innerNodes);
            case "InlineAssembly" :
                return new InlineAssembly(node, innerNodes);
            case "DoWhileStatement" :
                return new DoWhileStatement(node, innerNodes);
            case "EnumDefinition" :
                return new EnumDefinition(node, innerNodes);
            case "Assignment" :
                return new Assignment(node, innerNodes);
            case "ExpressionStatement" :
                return new ExpressionStatement(node, innerNodes);
            case "Literal" :
                return new Literal(node, innerNodes);
            case "Conditional" :
                return new Conditional(node, innerNodes);
            case "Throw" :
                return new Throw(node, innerNodes);
            case "MemberAccess" :
                return new MemberAccess(node, innerNodes);
            case "Continue" :
                return new Continue(node, innerNodes);
            case "Break" :
                return new Break(node, innerNodes);
            case "IndexAccess" :
                return new IndexAccess(node, innerNodes);
            case "NewExpression" :
                return new NewExpression(node, innerNodes);
            case "FunctionCall" :
                return new FunctionCall(node, innerNodes);
            case "VariableDeclarationStatement" :
                return new VariableDeclarationStatement(node, innerNodes);
            case "BinaryOperation" :
                return new BinaryOperation(node, innerNodes);
            case "UnaryOperation" :
                return new UnaryOperation(node, innerNodes);
            case "WhileStatement" :
                return new WhileStatement(node, innerNodes);
            case "ElementaryTypeNameExpression" :
                return new ElementaryTypeNameExpression(node, innerNodes);
            case "ForStatement" :
                return new ForStatement(node, innerNodes);
            case "IfStatement" :
                return new IfStatement(node, innerNodes);
            case "EmitStatement" :
                return new EmitStatement(node, innerNodes);
            case "SourceUnit" :
                return new SourceUnit(node, innerNodes);
            case "PragmaDirective" :
                return new PragmaDirective(node, innerNodes);
            case "EventDefinition" :
                return new EventDefinition(node, innerNodes);
            case "ModifierDefinition" :
                return new ModifierDefinition(node, innerNodes);
            case "PlaceholderStatement" :
                return new PlaceholderStatement(node, innerNodes);
            case "InheritanceSpecifier" :
                return new InheritanceSpecifier(node, innerNodes);
            case "UsingForDirective" :
                return new UsingForDirective(node, innerNodes);
            case "StorageLocation" :
                return new StorageLocation(node, innerNodes);
            case "Return" :
                return new Return(node, innerNodes);
            default:
                 return new OldNode(node, innerNodes) {

                     @Override
                     public String getCode() {
                         logger.error("Error New OldNode type: " + getType());
                         return "Error new OldNode Type" + getType() + "";
                     }
                 };
        }
    }
}
