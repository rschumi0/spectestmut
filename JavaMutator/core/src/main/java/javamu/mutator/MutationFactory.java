package javamu.mutator;

import javamu.mutator.Mutations.*;
import java.util.ArrayList;
import java.util.List;

public class MutationFactory {

    private MutationPlan baseMutation;

    public MutationFactory(MutationPlan baseMutation) {
        this.baseMutation = baseMutation;
    }

    public Mutation getMutaion() {
        List<Mutation> innerMutations = new ArrayList<>();
        if (baseMutation.getInnerMutations() != null){
            for(MutationPlan mutationPlan : baseMutation.getInnerMutations()){
                innerMutations.add(new MutationFactory(mutationPlan).getMutaion());
            }
        }

        switch (baseMutation.getType()) {
            case "AddIfElse" :
                return new AddIfElse(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddWhile" :
                return new AddWhile(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddFunctionDef" :
                return new AddFunctionDef(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddConstant" :
                return new AddConstant(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddArray" :
                return new AddArray(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddTryCatch" :
                return new AddTryCatch(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddSynchronized" :
                return new AddSynchronized(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddLabelledBlock":
                return new AddLabelledBlock(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddFor":
                return new AddFor(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddForEach":
                return new AddForEach(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddDoWhile":
                return new AddDoWhile(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddVarAndCheck":
                return new AddVarAndCheck(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddComputations":
                return new AddComputations(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddConditions":
                return new AddConditions(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddThread":
                return new AddThread(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddCasts":
                return new AddCasts(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            default:
                return new Mutation(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
        }
    }
}
