package javamu.mutator.Mutations;

import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.ContinueStmt;
import javamu.ASTparser.MyNode;
import javamu.common.BasicGrammar;

import java.util.Properties;

public class BreakContinue {
    public static MyNode AddBreakContinue(MyNode mutationNode, Properties parameters){
        Boolean addBreak = (Boolean) parameters.getOrDefault("Break",false);
        Boolean addContinue = (Boolean) parameters.getOrDefault("Continue",false);
        if (addBreak){
            BreakStmt breakNode = new BreakStmt();
            MyNode block = mutationNode.findSubNode(BasicGrammar.NodeType.Block.toString());
            block.addSubNode(new MyNode(breakNode));
        }
        if (addContinue){
            ContinueStmt breakNode = new ContinueStmt();
            MyNode block = mutationNode.findSubNode(BasicGrammar.NodeType.Block.toString());
            block.addSubNode(new MyNode( breakNode));
        }
        return mutationNode;
    }
}
