package solmu.mutator.Mutations;

import solmu.common.BasicGrammar;
import solmu.grammer.Break;
import solmu.grammer.Continue;
import solmu.grammer.Node;

import java.util.Properties;

public class BreakContinue {
    public static Node AddBreakContinue(Node mutationNode, Properties parameters){
        Boolean addBreak = (Boolean) parameters.getOrDefault("Break",false);
        Boolean addContinue = (Boolean) parameters.getOrDefault("Continue",false);
        if (addBreak){
            Break breakNode = new Break();
            Node block = mutationNode.findSubNode(BasicGrammar.NodeType.Block.toString());
            block.addSubNode(breakNode);
        }
        if (addContinue){
            Continue breakNode = new Continue();
            Node block = mutationNode.findSubNode(BasicGrammar.NodeType.Block.toString());
            block.addSubNode(breakNode);
        }
        return mutationNode;
    }
}
