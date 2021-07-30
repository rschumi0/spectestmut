package solmu.mutator;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import solmu.grammer.Node;
import solmu.mutator.Mutations.Mutation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class is to read the mutation plan and start executing it
 */
public class MutationEngine {
    private static final Logger logger =  Logger.getLogger(MutationEngine.class);
    private Boolean mutationComplete = false;
    private String location;

    public MutationEngine(String location) {
        this.location = location;
    }

    public Boolean isMutationComplete() {
        return mutationComplete;
    }

    public void setMutationComplete(Boolean mutationComplete) {
        this.mutationComplete = mutationComplete;
    }


    public MutationPlan readYaml() {
        try(FileInputStream fileInputStream = new FileInputStream(new File(location))) {
            MutationPlan mutation = new Yaml().loadAs(fileInputStream, MutationPlan.class);
            return mutation;
        } catch ( IOException e) {
            logger.error("File not found");
            return null;
        }
    }

    public void start(Node ast) {
        MutationPlan mutationPlan = readYaml();
        MutationFactory mutationFactory = new MutationFactory(mutationPlan);
        Mutation mutation = mutationFactory.getMutaion();
        mutation.start(ast);
    }

}
