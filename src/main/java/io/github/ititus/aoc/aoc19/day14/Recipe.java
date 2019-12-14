package io.github.ititus.aoc.aoc19.day14;

import java.util.Objects;
import java.util.Set;

public final class Recipe {

    private final MaterialStack output;
    private final Set<MaterialStack> inputs;

    public Recipe(MaterialStack output, Set<MaterialStack> inputs) {
        this.output = Objects.requireNonNull(output);
        this.inputs = Set.copyOf(Objects.requireNonNull(inputs));
    }

    public boolean isBasic() {
        return inputs.size() == 1 && Recipes.ORE.equals(inputs.iterator().next().getMaterial());
    }

    public MaterialStack getOutput() {
        return output;
    }

    public Set<MaterialStack> getInputs() {
        return inputs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe)) {
            return false;
        }
        Recipe recipe = (Recipe) o;
        return output.equals(recipe.output) && inputs.equals(recipe.inputs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(output, inputs);
    }

    @Override
    public String toString() {
        return "Recipe{" + "output=" + output + ", inputs=" + inputs + '}';
    }
}
