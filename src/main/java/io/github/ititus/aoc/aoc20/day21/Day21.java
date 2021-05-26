package io.github.ititus.aoc.aoc20.day21;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Aoc(year = 2020, day = 21)
public class Day21 implements AocSolution {

    private List<Food> foods;
    private Map<String, String> allergenToIngredient;

    @Override
    public void executeTests() {
        readInput(new AocStringInput("""
                mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
                trh fvjkl sbzzf mxmxvkd (contains dairy)
                sqjhc fvjkl (contains soy)
                sqjhc mxmxvkd sbzzf (contains fish)
                """));
        System.out.println("Part 1 (expected 5): " + part1());
        System.out.println("Part 2 (expected mxmxvkd,sqjhc,fvjkl): " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        try (Stream<String> stream = input.lines()) {
            foods = stream
                    .map(Food::of)
                    .collect(Collectors.toList());
        }

        Map<String, Set<String>> allergenToIngredients = new HashMap<>();
        for (Food food : foods) {
            for (String allergen : food.getAllergens()) {
                allergenToIngredients.computeIfAbsent(allergen, k -> new HashSet<>(food.getIngredients()))
                        .retainAll(food.getIngredients());
            }
        }

        boolean work;
        do
        {
            work = false;
            for (Map.Entry<String, Set<String>> entry : allergenToIngredients.entrySet()) {
                String allergen = entry.getKey();
                Set<String> ingredients = entry.getValue();

                if (ingredients.isEmpty()) {
                    throw new RuntimeException();
                } else if (ingredients.size() == 1) {
                    String ingredient = ingredients.iterator().next();
                    for (Map.Entry<String, Set<String>> other : allergenToIngredients.entrySet()) {
                        if (allergen.equals(other.getKey())) {
                            continue;
                        }

                        work |= other.getValue().remove(ingredient);
                    }
                }
            }
        } while (work);

        allergenToIngredient = allergenToIngredients.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    if (e.getValue().size() != 1) {
                        throw new RuntimeException();
                    }

                    return e.getValue().iterator().next();
                }));
    }

    @Override
    public Object part1() {
        Set<String> safeIngredients = foods.stream()
                .flatMap(f -> f.getIngredients().stream())
                .collect(Collectors.toSet());
        safeIngredients.removeAll(allergenToIngredient.values());

        return foods.stream()
                .mapToInt(f -> f.count(safeIngredients))
                .sum();
    }

    @Override
    public Object part2() {
        return allergenToIngredient.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.joining(","));
    }
}
