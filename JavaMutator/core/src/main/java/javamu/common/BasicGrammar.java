package javamu.common;

public class BasicGrammar {

    public static String StatementEnd = ";\n";
    public static String[] typeName = {"ElementaryTypeName","UserDefinedTypeName", "ArrayTypeName", "Mapping",
            "FunctionTypeName", "address", "payable"};
    public static String space = " ";
    public enum NodeType {
        ModifierDefinition,
        InheritanceSpecifier,
        Conditional,
        StorageLocation,
        Continue,
        Break,
        UnaryOperation,
        BinaryOperation,
        WhileStatement,
        ElementaryTypeNameExpression,
        ForStatement,
        IfStatement,
        ContractDefinition,
        ArrayTypeName,
        Assignment,
        PragmaDirective,
        SourceUnit,
        ImportDirective,
        Block,
        ExpressionStatement,
        FunctionCall,
        FunctionDefinition,
        Identifier,
        IndexAccess,
        Return,
        Literal,
        InlineAssembly,
        Mapping,
        MemberAccess,
        ModifierInvocation,
        ParameterList,
        StateMutability,
        StructDefinition,
        UserDefinedTypeName,
        VariableDeclaration,
        ElementaryTypeName,
        TupleExpression,
        PlaceholderStatement,
        EnumDefinition,
        EnumValue,
        DoWhileStatement,
        EmitStatement,
        EventDefinition,
        NewExpression,
        Throw,
        UsingForDirective,
        VariableDeclarationStatement
    }

    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyz";
    public static String randomName() {
        int count = 10;
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }
}


