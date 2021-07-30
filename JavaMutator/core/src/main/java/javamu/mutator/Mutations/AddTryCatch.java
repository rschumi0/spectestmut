package javamu.mutator.Mutations;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import javamu.ASTparser.MyNode;
import org.apache.commons.lang.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddTryCatch extends Mutation {

    public AddTryCatch(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(MyNode ast) {
        Boolean status = false;
        if(super.start(ast)){
            for(String type : getAddTo()) {
                //MyNode newNode = getMutauionNode();
                Statement tempFirstStmt = null;
                Statement tempLastStmt = null;
                com.github.javaparser.ast.type.Type retType = null;
                ArrayList<MyNode> targetBlocks = getNodeType(type, ast);
                for (MyNode target : targetBlocks) {
                    //newNode.getSubNodes().get(0).setName(getParameters().getProperty("condition"));

                    if(((BlockStmt)target.getNode()).getStatements().size() > 0 ){
                        Statement s =((BlockStmt)target.getNode()).getStatements().get(0);
                        if(s instanceof ExplicitConstructorInvocationStmt){
                            tempFirstStmt = s;
                            ((BlockStmt)target.getNode()).getStatements().remove(0);
                        }
                    }
                    if(((BlockStmt)target.getNode()).getStatements().size() != 0)
                    {
                        Statement s = ((BlockStmt)target.getNode()).getStatements().get(((BlockStmt)target.getNode()).getStatements().size()-1);
                        if(s instanceof ReturnStmt)
                        {
                            System.out.println(s.toString());
                            try {
                                tempLastStmt = s.asReturnStmt();
                                MethodDeclaration method = tempLastStmt.findAncestor(MethodDeclaration.class).get();
                                retType = method.getType();
                                //((BlockStmt)target.getNode()).getStatements().remove(((BlockStmt)target.getNode()).getStatements().size()-1);
                            }
                            catch(Exception e)
                            {
                                tempLastStmt = null;
                            }
                        }
                    }

                    CatchClause cc = new CatchClause(new Parameter(new ClassOrInterfaceType("Exception"), "a0"+RandomStringUtils.randomAlphanumeric(10)), new BlockStmt());
                    NodeList<CatchClause> nl = new NodeList<CatchClause>();
                    nl.add(cc);
                    TryStmt ts = new TryStmt(new BlockStmt(), nl, null);
                    int nested = (int) getParameters().getOrDefault("nested",1);
                    System.out.println("nested: " + nested);

//                    if((Boolean) getParameters().getOrDefault("onlyIf",false)){
//                        is.removeElseStmt();
//                    }

                    if(getParameters().getProperty("move") != null){
                        if(!getParameters().getProperty("move").toLowerCase().contains("try") && !getParameters().getProperty("move").toLowerCase().contains("catch")){
                            getParameters().setProperty("move",Math.random() < 0.5 ? "try" : "catch");
                        }
                        if(getParameters().getProperty("move").toLowerCase().contains("try"))
                        {
                            ts.getTryBlock().asBlockStmt().getStatements().addAll(((BlockStmt)target.getNode()).getStatements());
                            ((BlockStmt)target.getNode()).getStatements().clear();
                        }
                        else if (getParameters().getProperty("move").toLowerCase().contains("catch"))
                        {
                            ObjectCreationExpr exception = new ObjectCreationExpr();
                            exception.setType(new ClassOrInterfaceType("Exception"));
                            //NodeList<Expression> arguments = new NodeList<>();
                            //arguments.add(new StringLiteralExpr("test."));
                            //exception.setArguments(arguments);
                            ThrowStmt throwStmt = new ThrowStmt(exception);

                            ts.getTryBlock().addStatement(throwStmt);
                            ts.getCatchClauses().get(0).getBody().asBlockStmt().getStatements().addAll(((BlockStmt)target.getNode()).getStatements());
                            ((BlockStmt)target.getNode()).getStatements().clear();
                        }
                        else
                            return false;

//                        newNode.getSubNodes().get(moveTo).getSubNodes().addAll(
//                                target.findSubNode(BasicGrammar.NodeType.Block.toString()).getSubNodes());
//                        target.findSubNode(BasicGrammar.NodeType.Block.toString()).setSubNodes(new ArrayList<>());
//
//                        target.findSubNode(BasicGrammar.NodeType.Block.toString()).addSubNode(newNode);
                    }

                    if(nested > 1)
                    {
                        addNested(ts, Math.random() < 0.5, nested, (boolean)getParameters().getOrDefault("randomNestedThrow",false), true);
                    }

                    ((BlockStmt)target.getNode()).addStatement(ts);

                    if(tempFirstStmt != null){
                        ((BlockStmt)target.getNode()).getStatements().add(0,tempFirstStmt);
                        tempFirstStmt = null;
                    }
                    if((getParameters().getProperty("move") == null || getParameters().getProperty("move") != null && getParameters().getProperty("move").toLowerCase().contains("try")) && tempLastStmt != null){
                        System.out.println(tempLastStmt.toString());
                        //BlockStmt parentBlock = (BlockStmt) tempLastStmt.getParentNode().get();

                        //MethodDeclaration method = tempLastStmt.findAncestor(MethodDeclaration.class).get();
                        System.out.println(retType.toString());
                        //Optional<MethodDeclaration> md = tempLastStmt.findAncestor(MethodDeclaration.class);

                        //((ReturnStmt)tempLastStmt).getExpression().get();
                        String retString = "null";
                        //JavaParserFacade javaParserFacade = JavaParserFacade.get(typeSolver);
                        //Expression expr = ( ( ReturnStmt ) tempLastStmt ).getExpression().get();
                        if(retType.toString().equals("int")){
                            retString = "0";
                        }
                        else if(retType.toString().equals("boolean")){
                            retString = "true";
                        }
                        else if(retType.toString().equals("String")){
                            retString = "\"\"";
                        }
                        System.out.println(retString);


                        ((BlockStmt)target.getNode()).getStatements().add(new ReturnStmt(retString));
                        //((BlockStmt)target.getNode()).getStatements().add(tempLastStmt);
                        tempLastStmt = null;
                    }

                    status = true;
                }
            }
        }
        return status;
    }
    private void addNested(TryStmt ts, boolean tryCase, int count, boolean randomThrowInsert, boolean firstLevel){
        boolean cond = Math.random() < 0.5;
        IfStmt is1 = new IfStmt(new BooleanLiteralExpr(cond), new BlockStmt(), new BlockStmt());

        CatchClause cc = new CatchClause(new Parameter(new ClassOrInterfaceType("Exception"), "a"+count+RandomStringUtils.randomAlphanumeric(10)), new BlockStmt());
        NodeList<CatchClause> nl = new NodeList<CatchClause>();
        nl.add(cc);
        TryStmt ts1 = new TryStmt(new BlockStmt(), nl, null);

        boolean addThrowToTry = false;

        ThrowStmt throwStmt = null;
        if(!firstLevel && randomThrowInsert){
            if(Math.random() < 0.5){
                ObjectCreationExpr exception = new ObjectCreationExpr();
                NodeList<Expression> arguments = new NodeList<Expression>();
                arguments.add(new StringLiteralExpr(RandomStringUtils.randomAlphanumeric(10)));
                exception.setType(new ClassOrInterfaceType("Exception"));
                exception.setArguments(arguments);
                throwStmt = new ThrowStmt(exception);
                if(Math.random() < 0.5){
                    addThrowToTry = true;
                }
            }
        }
        if(tryCase){
            ts.getTryBlock().asBlockStmt().getStatements().addFirst(ts1);
            if(throwStmt != null && addThrowToTry){
                ts.getTryBlock().asBlockStmt().getStatements().add(throwStmt);
            }
        }
        else
        {
            ts.getCatchClauses().get(0).getBody().asBlockStmt().getStatements().addFirst(ts1);
            if(throwStmt != null && !addThrowToTry){
                ts.getTryBlock().asBlockStmt().getStatements().add(throwStmt);
            }
        }
        if(count > 0) {
            addNested(ts1, cond, --count,randomThrowInsert, false);
        }
    }



//    private MyNode addNested(MyNode node, int ifOrElse, int count){
//        MyNode base = getMutauionNode();
//        base.getSubNodes().get(0).setName(getParameters().getProperty("condition"));
//        node.addSubNode(base);
//        if (count == 0){
//            return base;
//        }
//        addNested(node.getSubNodes().get(0).getSubNodes().get(ifOrElse),ifOrElse,--count);
//        return node;
//    }
}
