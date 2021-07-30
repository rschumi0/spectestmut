package javamu.grammer;

import org.json.simple.JSONObject;
import javamu.common.BasicGrammar;

import java.util.ArrayList;

public class ImportDirective extends OldNode {
    private String absolutePath;
    private String unitAlias;

    public String getUnitAlias() {
        return unitAlias;
    }

    public void setUnitAlias(String unitAlias) {
        this.unitAlias = unitAlias;
    }

    public ImportDirective(JSONObject node, ArrayList<OldNode> subNodes) {
        super(node, subNodes);
        this.absolutePath = getAttributes().get("absolutePath").toString();
        this.unitAlias = getAttributes().get("unitAlias").toString();
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }
    /**
     * this Method is to recreate the solidity code from the OldNode
     * @return original solidity code snippet which represent the AST node
     */
    @Override
    public String getCode() {
        super.getCode();
        String code = "import \"" + getAbsolutePath() + "\"";
        if(!getUnitAlias().isEmpty()) {
            code += " as " + getUnitAlias();
        }
        code += BasicGrammar.StatementEnd;

        return code;
    }
}
