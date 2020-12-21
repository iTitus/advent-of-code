package io.github.ititus.aoc.aoc20.day21;

import java.util.HashSet;
import java.util.Set;

public class Food {

    private final Set<String> ingredients, allergens;

    private Food(Set<String> ingredients, Set<String> allergens) {
        this.ingredients = ingredients;
        this.allergens = allergens;
    }

    public static Food of(String line) {
        Set<String> ingredients, allergens;

        int idx = line.indexOf('(');
        if (idx >= 0) {
            allergens = Set.of(line.substring(idx + 10, line.length() - 1).split(", "));
            line = line.substring(0, idx - 1);
        } else {
            allergens = Set.of();
        }

        ingredients = Set.of(line.split(" "));

        return new Food(ingredients, allergens);
    }

    public Set<String> getIngredients() {
        return ingredients;
    }

    public Set<String> getAllergens() {
        return allergens;
    }

    public int count(Set<String> safeIngredients) {
        Set<String> set = new HashSet<>(ingredients);
        set.retainAll(safeIngredients);
        return set.size();
    }

    @Override
    public String toString() {
        String s = String.join(" ", ingredients);
        if (!allergens.isEmpty()) {
            s += " (contains " + String.join(", ", allergens) + ")";
        }

        return s;
    }
}
