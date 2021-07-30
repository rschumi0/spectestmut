package solmu.ASTparser;


import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import solmu.grammer.Node;

import java.util.ArrayList;

public class JsonParser {

    private static final Logger logger =  Logger.getLogger(NodeFactory.class);

    public Node parse(String json){
        JSONParser parser = new JSONParser();
        Object obj;
        /*try {
            json = new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            logger.error(e);
        }*/

        try {
            obj = parser.parse(json);
            JSONObject jsonObject = (JSONObject) obj;
            return findNodes(jsonObject,jsonObject.get("name").toString());
        } catch (ParseException e) {
            logger.error(e);
            return null;
        }

    }

    public Node findNodes(JSONObject jsonObject, String superType){
        Node node;
        ArrayList<Node> innerNodes = new ArrayList<>();
        if (jsonObject.get("children") != null) {
            JSONArray children = (JSONArray) jsonObject.get("children");
            if (children != null){
                for(int x = 0; x < children.size(); x ++){
                    JSONObject innernode = (JSONObject) children.get(x);
                    innerNodes.add(findNodes(innernode, jsonObject.get("name").toString()));
                }
            }

        }

        NodeFactory nodeFactory = new NodeFactory(jsonObject, innerNodes);
        node = nodeFactory.getNode();
        node.setSuperType(superType);

        return node;
    }

}
