package solmu.mutator;

import solmu.mutator.Mutations.*;

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
            case "AddModifier" :
                return new AddModifier(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddEvent" :
                return new AddEvent(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddForLoop" :
                return new AddForLoop(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddMapping" :
                return new AddMapping(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddWhile" :
                return new AddWhile(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddEnumDef" :
                return new AddEnumDef(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddConditional" :
                return new AddConditional(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddFunctionDef" :
                return new AddFunctionDef(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "RemoveView" :
                return new RemoveView(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddRequire" :
                return new AddRequire(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddConstant" :
                return new AddConstant(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddArray" :
                return new AddArray(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddStruct" :
                return new AddStruct(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddDoWhile" :
                return new AddDoWhile(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddArrayAndCheck" :
                return new AddArrayAndCheck(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddComputations" :
                return new AddComputations(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            case "AddConditions" :
                return new AddConditions(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
            default:
                return new Mutation(baseMutation.getType(),baseMutation.getName(),baseMutation.getParameters(),
                        innerMutations);
        }
    }
}
