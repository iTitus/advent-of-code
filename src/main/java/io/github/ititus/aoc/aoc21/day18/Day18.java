package io.github.ititus.aoc.aoc21.day18;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;

import java.util.List;

@Aoc(year = 2021, day = 18)
public class Day18 implements AocSolution {

    List<SnailfishNumber> numbers;

    @Override
    public void executeTests() {
        System.out.println("expected: [[[[0,9],2],3],4] | actual: " + SnailfishNumber.parse("[[[[[9,8],1],2],3],4]").explode().orElseThrow());
        System.out.println("expected: [7,[6,[5,[7,0]]]] | actual: " + SnailfishNumber.parse("[7,[6,[5,[4,[3,2]]]]]").explode().orElseThrow());
        System.out.println("expected: [[6,[5,[7,0]]],3] | actual: " + SnailfishNumber.parse("[[6,[5,[4,[3,2]]]],1]").explode().orElseThrow());
        System.out.println("expected: [[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]] | actual: " + SnailfishNumber.parse("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]").explode().orElseThrow());
        System.out.println("expected: [[3,[2,[8,0]]],[9,[5,[7,0]]]] | actual: " + SnailfishNumber.parse("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]").explode().orElseThrow());

        System.out.println("expected: [5,5] | actual: " + SnailfishNumber.parse("10").split().orElseThrow());
        System.out.println("expected: [5,5] | actual: " + SnailfishNumber.parse("11").split().orElseThrow());

        System.out.println("expected: [[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]] | actual: " + SnailfishNumber.parse("[[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]],[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]]").reduce());

        AocInput input = new AocStringInput("""
                [1,1]
                [2,2]
                [3,3]
                [4,4]""");
        readInput(input);
        SnailfishNumber sum = numbers.stream()
                .reduce(SnailfishNumber::add)
                .orElseThrow();
        System.out.println("expected: [[[[1,1],[2,2]],[3,3]],[4,4]] | actual: " + sum);

        input = new AocStringInput("""
                [1,1]
                [2,2]
                [3,3]
                [4,4]
                [5,5]""");
        readInput(input);
        sum = numbers.stream()
                .reduce(SnailfishNumber::add)
                .orElseThrow();
        System.out.println("expected: [[[[3,0],[5,3]],[4,4]],[5,5]] | actual: " + sum);

        input = new AocStringInput("""
                [1,1]
                [2,2]
                [3,3]
                [4,4]
                [5,5]
                [6,6]""");
        readInput(input);
        sum = numbers.stream()
                .reduce(SnailfishNumber::add)
                .orElseThrow();
        System.out.println("expected: [[[[5,0],[7,4]],[5,5]],[6,6]] | actual: " + sum);

        input = new AocStringInput("""
                [[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
                [7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
                [[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
                [[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
                [7,[5,[[3,8],[1,4]]]]
                [[2,[2,2]],[8,[8,1]]]
                [2,9]
                [1,[[[9,3],9],[[9,0],[0,7]]]]
                [[[5,[7,4]],7],1]
                [[[[4,2],2],6],[8,7]]""");
        readInput(input);
        sum = numbers.stream()
                .reduce(SnailfishNumber::add)
                .orElseThrow();
        System.out.println("expected: [[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]] | actual: " + sum);

        System.out.println("expected: 143 | actual: " + SnailfishNumber.parse("[[1,2],[[3,4],5]]").magnitude());
        System.out.println("expected: 1384 | actual: " + SnailfishNumber.parse("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]").magnitude());
        System.out.println("expected: 445 | actual: " + SnailfishNumber.parse("[[[[1,1],[2,2]],[3,3]],[4,4]]").magnitude());
        System.out.println("expected: 791 | actual: " + SnailfishNumber.parse("[[[[3,0],[5,3]],[4,4]],[5,5]]").magnitude());
        System.out.println("expected: 1137 | actual: " + SnailfishNumber.parse("[[[[5,0],[7,4]],[5,5]],[6,6]]").magnitude());
        System.out.println("expected: 3488 | actual: " + SnailfishNumber.parse("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]").magnitude());

        input = new AocStringInput("""
                [[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
                [[[5,[2,8]],4],[5,[[9,9],0]]]
                [6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
                [[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
                [[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
                [[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
                [[[[5,4],[7,7]],8],[[8,3],8]]
                [[9,3],[[9,9],[6,[4,9]]]]
                [[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
                [[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]""");
        readInput(input);
        sum = numbers.stream()
                .reduce(SnailfishNumber::add)
                .orElseThrow();
        System.out.println("expected: [[[[6,6],[7,6]],[[7,7],[7,0]]],[[[7,7],[7,7]],[[7,8],[9,9]]]] | actual: " + sum);
        System.out.println("expected: 4140 | actual: " + part1());
        System.out.println("expected: 3993 | actual: " + part2());
    }

    @Override
    public void readInput(AocInput input) {
        numbers = input.lines()
                .map(String::strip)
                .filter(s -> !s.isEmpty())
                .map(SnailfishNumber::parse)
                .toList();
    }

    @Override
    public Object part1() {
        return numbers.stream()
                .reduce(SnailfishNumber::add)
                .orElseThrow()
                .magnitude();
    }

    @Override
    public Object part2() {
        return numbers.stream()
                .flatMapToInt(n -> numbers.stream()
                        .filter(n_ -> n_ != n)
                        .map(n::add)
                        .mapToInt(SnailfishNumber::magnitude)
                )
                .max()
                .orElseThrow();
    }
}
