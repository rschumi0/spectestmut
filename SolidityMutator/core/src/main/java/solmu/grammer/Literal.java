package solmu.grammer;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Literal extends Node{


    private String token;
    private String subdenomination;
    public Literal(JSONObject node, ArrayList<Node> subNodes) {
        super(node, subNodes);
        if(getName().isEmpty()){
            try {
                setName(getAttributes().get("value").toString());
            }catch (NullPointerException e){
                setName("hex\""+getAttributes().get("hexvalue").toString()+"\"");
            }
        }
        this.token = getAttributes().get("token").toString();
        this.subdenomination = "";
        if (getAttributes().get("subdenomination") != null){
            this.subdenomination = " " + getAttributes().get("subdenomination").toString();
        }
        setName(preserveEscapeChar(getName()));
    }
    /**
     * this Method is to recreate the solidity code from the Node
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        if (token.equalsIgnoreCase("string") && !getName().startsWith("hex")) {
            return "\"" + getName() + "\"";
        } else {
            return  getName() + subdenomination;
        }
    }

    public String preserveEscapeChar(String string) {
        string= string.
                replace("\n","\\n").
                replace("\t","\\t");
        string = unicode(string);

        return string;
    }

    private String unicode(String string) {
        int k =0;
        for (char i='\u0000';i<='\u0019';i++) {
            String hex;
            if(Integer.toHexString(k).length() < 2){
                hex = "0" + Integer.toHexString(k);
            }
            else{
                hex = Integer.toHexString(k);
            }
            string = string.replace(String.valueOf(i),"\\x"+hex);
            k++;
        }
        return string;
    }
}
