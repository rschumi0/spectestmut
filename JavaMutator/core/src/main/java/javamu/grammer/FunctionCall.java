package javamu.grammer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javamu.common.BasicGrammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class FunctionCall extends OldNode {

    private HashMap<String, OldNode> keys = null;
    public FunctionCall(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
        if(getAttributes().get("names") != null) {
            JSONArray jsonArray = (JSONArray) getAttributes().get("names");
            if (jsonArray.get(0) != null) {
                keys = new LinkedHashMap<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    keys.put(jsonArray.get(i).toString(),getSubNodes().get(i + 1));
                }
            }
        }
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        String code = getSubNodes().get(0).getCode() + "(" ;
        if(getSubNodes().size() > 1) {
            if (keys != null) {
                code = code + "{";
                Iterator<String> iterator = keys.keySet().iterator();
                while (iterator.hasNext()) {
                    String varName = iterator.next();
                    String comma = "";
                    if (iterator.hasNext()) {
                        comma = ", ";
                    }
                    keys.get(varName).setEndStament(false);
                    code = code + varName + ":" + keys.get(varName).getCode() + comma;
                }
                code = code + "}";
            } else {
                for(int i = 1; i < getSubNodes().size(); i++){
                    getSubNodes().get(i).setEndStament(false);
                    if (i == 1){
                        code = code + getSubNodes().get(i).getCode() + BasicGrammar.space;
                    }else {
                        code = code + "," + getSubNodes().get(i).getCode() + BasicGrammar.space;
                    }
                }
            }
        }
        code = code +")";
        return code;
    }
}
