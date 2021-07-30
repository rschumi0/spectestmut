package javamu.mutator.Mutations;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import javamu.ASTparser.MyNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AddIfElse extends Mutation {

    public AddIfElse(String type, String name, Properties parameters, List<Mutation> mutations) {
        super(type, name, parameters, mutations);
    }

    @Override
    public Boolean start(MyNode ast) {
        Boolean status = false;
        if(super.start(ast)){
            for(String type : getAddTo()) {
                //MyNode newNode = getMutauionNode();
                ArrayList<MyNode> targetBlocks = getNodeType(type, ast);
                for (MyNode target : targetBlocks) {
                    //newNode.getSubNodes().get(0).setName(getParameters().getProperty("condition"));
                    IfStmt is = new IfStmt(new BooleanLiteralExpr(), new BlockStmt(), new BlockStmt());
                    int nested = (int) getParameters().getOrDefault("nested",1);
                    System.out.println("nested: " + nested);


                    if((Boolean) getParameters().getOrDefault("onlyIf",false)){
                        is.removeElseStmt();
                    }

                    if(getParameters().getProperty("move") != null){
                        if(getParameters().getProperty("move").toLowerCase().contains("true"))
                        {
                            is.getThenStmt().asBlockStmt().getStatements().addAll(((BlockStmt)target.getNode()).getStatements());
                            ((BlockStmt)target.getNode()).getStatements().clear();
                        }
                        else if (getParameters().getProperty("move").toLowerCase().contains("false"))
                        {
                            is.setCondition(new BooleanLiteralExpr(false));
                            is.getElseStmt().get().asBlockStmt().getStatements().addAll(((BlockStmt)target.getNode()).getStatements());
                            ((BlockStmt)target.getNode()).getStatements().clear();
                        }
                        else
                            return false;

//                        newNode.getSubNodes().get(moveTo).getSubNodes().addAll(
//                                target.findSubNode(BasicGrammar.NodeType.Block.toString()).getSubNodes());
//                        target.findSubNode(BasicGrammar.NodeType.Block.toString()).setSubNodes(new ArrayList<>());
//
//                        target.findSubNode(BasicGrammar.NodeType.Block.toString()).addSubNode(newNode);
                    }else
                    {
                        System.out.println("nested: " + nested);
                        addNested(is, is.getCondition().asBooleanLiteralExpr().getValue(), nested);
                        //target.findSubNode(BasicGrammar.NodeType.Block.toString()).addSubNode(newNode);
                        ((BlockStmt)target.getNode()).addStatement(is);
                    }
                    status = true;
                }
            }
        }
        return status;
    }
    private void addNested(IfStmt is, boolean thenCase, int count){
        boolean cond = Math.random() < 0.5;
        IfStmt is1 = new IfStmt(new BooleanLiteralExpr(cond), new BlockStmt(), new BlockStmt());
        if(thenCase){
            is.getThenStmt().asBlockStmt().addStatement(is1);
        }
        else
        {
            is.getElseStmt().get().asBlockStmt().addStatement(is1);
        }
        if(count > 0) {
            addNested(is1, cond, --count);
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
