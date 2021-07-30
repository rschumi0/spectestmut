package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Return extends  Node {

    public Return(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
        try {
            getSubNodes().get(0).setEndStament(false);
        } catch (IndexOutOfBoundsException e){

        }
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        try {
            return "return " + getSubNodes().get(0).getCode() + getStatementEnd();
        } catch (IndexOutOfBoundsException e){
            return "return" + getStatementEnd();
        }
    }
}
