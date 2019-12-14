package io.github.ititus.aoc.aoc19.day14;

import io.github.ititus.math.number.BigRational;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class Recipes {

    public static final String ORE = "ORE";
    public static final String FUEL = "FUEL";

    private final Set<Recipe> recipes;

    public Recipes(Set<Recipe> recipes) {
        this.recipes = Set.copyOf(recipes);
    }

    public static Builder builder() {
        return new Builder();
    }

    public Recipe getRecipe(String material) {
        List<Recipe> valid = recipes.stream().filter(r -> r.getOutput().getMaterial().equals(material)).collect(Collectors.toList());
        if (valid.size() != 1) {
            throw new RuntimeException(valid.size() + " recipes found for " + material + ", expected 1");
        }
        return valid.get(0);
    }

    public BigInteger calculateRequiredOre(MaterialStack stack) {
        if (ORE.equals(stack.getMaterial())) {
            return stack.getAmount();
        }

        MaterialStackSet excessIngredients = new MaterialStackSet();
        MaterialStackSet consumedIngredients = new MaterialStackSet();
        consumedIngredients.add(stack);

        while (!consumedIngredients.isEmpty()) {
            MaterialStack s = consumedIngredients.stream().filter(s_ -> !isBasic(s_.getMaterial())).findFirst().orElseGet(
                    () -> consumedIngredients.stream().filter(s_ -> !ORE.equals(s_.getMaterial())).findFirst().orElseGet(
                            () -> consumedIngredients.get(0)
                    )
            );
            if (ORE.equals(s.getMaterial())) {
                break;
            }

            consumedIngredients.remove(s.getMaterial());

            Recipe r = getRecipe(s.getMaterial());
            MaterialStack out = r.getOutput();
            BigInteger producedAmount = out.getAmount();

            BigInteger requiredProcesses = BigRational.of(s.getAmount()).divide(BigRational.of(producedAmount)).roundToBigInt(RoundingMode.CEILING);
            BigInteger wasted = producedAmount.multiply(requiredProcesses).subtract(s.getAmount());
            if (wasted.signum() > 0) {
                excessIngredients.add(new MaterialStack(s.getMaterial(), wasted));
            }

            r.getInputs().stream()
                    .map(s_ -> {
                                MaterialStack req = s_.multiply(requiredProcesses);

                                Optional<MaterialStack> alreadyPresent = excessIngredients.get(req.getMaterial());
                                if (alreadyPresent.isPresent()) {
                                    BigInteger reqAm = req.getAmount();
                                    BigInteger presAm = alreadyPresent.get().getAmount();
                                    if (reqAm.compareTo(presAm) < 0) {
                                        presAm = presAm.subtract(reqAm);
                                        reqAm = BigInteger.ZERO;
                                    } else {
                                        reqAm = reqAm.subtract(presAm);
                                        presAm = BigInteger.ZERO;
                                    }

                                    if (presAm.signum() > 0) {
                                        excessIngredients.set(alreadyPresent.get().withAmount(presAm));
                                    } else {
                                        excessIngredients.remove(alreadyPresent.get().getMaterial());
                                    }

                                    return req.withAmount(reqAm);
                                }

                                return req;
                            }
                    )
                    .filter(s_ -> s_.getAmount().signum() > 0)
                    .forEach(consumedIngredients::add);
        }

        if (consumedIngredients.size() != 1) {
            throw new RuntimeException();
        }
        MaterialStack ore = consumedIngredients.get(0);
        if (!ORE.equals(ore.getMaterial())) {
            throw new RuntimeException();
        }

        return ore.getAmount();
    }

    public BigInteger calculateMaxProduced(BigInteger oreInput, String output) {
        if (ORE.equals(output)) {
            return oreInput;
        }

        // Exponential Search
        BigInteger current = BigInteger.ONE;
        while (true) {
            BigInteger required = calculateRequiredOre(new MaterialStack(output, current));

            int c = required.compareTo(oreInput);
            if (c == 0) {
                return current;
            } else if (c > 0) { // required > oreInput
                break;
            }

            current = current.shiftLeft(1);
        }

        BigInteger min = current.shiftRight(1);
        BigInteger max = current.subtract(BigInteger.ONE);

        // Binary Search
        while (true) {
            if (max.subtract(min).compareTo(BigInteger.ONE) <= 0) {
                return min;
            }

            current = min.add(max).shiftRight(1);
            BigInteger required = calculateRequiredOre(new MaterialStack(output, current));
            int c = required.compareTo(oreInput);
            if (c == 0) {
                return current;
            } else if (c > 0) { // required > oreInput
                max = current.subtract(BigInteger.ONE);
            } else { // required < oreInput
                min = current;
            }
        }
    }

    public boolean isBasic(String material) {
        return ORE.equals(material) || getRecipe(material).isBasic();
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    @Override
    public String toString() {
        return "Recipes{" + "recipes=" + recipes + '}';
    }

    public static class Builder {

        private final Set<Recipe> recipes;

        private Builder() {
            this.recipes = new HashSet<>();
        }

        public Builder parseAddRecipe(String recipe) {
            String[] recipeParts = recipe.split("=>");
            if (recipeParts.length != 2) {
                throw new IllegalArgumentException();
            }

            String output = recipeParts[1];
            MaterialStack out = MaterialStack.parse(output.strip());

            String inputs = recipeParts[0];
            String[] inputParts = inputs.split(",");
            Set<MaterialStack> in = Arrays.stream(inputParts).map(MaterialStack::parse).collect(Collectors.toUnmodifiableSet());

            addRecipe(out, in);
            return this;
        }

        public Builder addRecipe(Recipe recipe) {
            recipes.add(recipe);
            return this;
        }

        public Builder addRecipe(MaterialStack output, Set<MaterialStack> inputs) {
            recipes.add(new Recipe(output, inputs));
            return this;
        }

        public Recipes build() {
            return new Recipes(recipes);
        }
    }
}
