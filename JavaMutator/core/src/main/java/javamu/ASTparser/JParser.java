package javamu.ASTparser;

import org.apache.log4j.Logger;
import com.github.javaparser.ast.Node;
import com.github.javaparser.*;

public class JParser {

    private static final Logger logger =  Logger.getLogger(NodeFactory.class);

    public Node parse(String javaStr){
        Node n = StaticJavaParser.parse(javaStr);
        return n;

    }

}
