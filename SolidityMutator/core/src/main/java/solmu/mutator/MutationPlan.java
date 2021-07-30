package solmu.mutator;

import java.util.List;
import java.util.Properties;

public class MutationPlan {
    private String type;
    private String name;
    private Properties parameters = new Properties();
    private List<MutationPlan> innerMutations;

    public List<MutationPlan> getInnerMutations() {
        return innerMutations;
    }

    public void setInnerMutations(List<MutationPlan> innerMutations) {
        this.innerMutations = innerMutations;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Properties getParameters() {
        return parameters;
    }

    public void setParameters(Properties parameters) {
        this.parameters = parameters;
    }


}
