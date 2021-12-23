package io.github.ititus.aoc.aoc21.day23;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;

import java.util.*;

@Aoc(year = 2021, day = 23)
public class Day23 implements AocSolution {

    Burrow startConfiguration;

    static int cost(char c, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }

        return n * switch (c) {
            case 'A' -> 1;
            case 'B' -> 10;
            case 'C' -> 100;
            case 'D' -> 1000;
            default -> throw new IllegalArgumentException();
        };
    }

    private static int dfs(Burrow startConfiguration) {
        int minCost = -1;

        Deque<Burrow> stack = new ArrayDeque<>();
        stack.push(startConfiguration);

        while (!stack.isEmpty()) {
            Burrow b = stack.pop();
            if (minCost >= 0 && b.cost > minCost) {
                continue;
            } else if (b.isDone()) {
                if (minCost < 0 || b.cost < minCost) {
                    minCost = b.cost;
                }

                continue;
            }

            for (Burrow move : b.allMoves()) {
                if (minCost < 0 || move.cost <= minCost) {
                    stack.push(move);
                }
            }
        }

        return minCost;
    }

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("""
                #############
                #...........#
                ###B#C#B#D###
                  #A#D#C#A#
                  #########""");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        int roomCount = 4;
        int space = 2;
        startConfiguration = new Burrow();
        List<String> lines = input.lines()
                .skip(2)
                .limit(space)
                .toList();

        Arrays.fill(startConfiguration.hallway, '.');
        for (int i = 0; i < roomCount; i++) {
            Room r = new Room((char) ('A' + i), space);
            for (int j = 0; j < 2; j++) {
                r.contents[j] = lines.get(j).charAt(3 + i * 2);
            }

            startConfiguration.rooms[i] = r;
        }
    }

    @Override
    public Object part1() {
        return dfs(startConfiguration);
    }

    @Override
    public Object part2() {
        return dfs(startConfiguration.extend(new char[][] { { 'D', 'D' }, { 'C', 'B' }, { 'B', 'A' }, { 'A', 'C' } }));
    }

    static class Burrow {

        final Room[] rooms = new Room[4];
        final char[] hallway = new char[7];
        int cost;

        List<Burrow> allMoves() {
            // hallway -> room
            outer:
            for (int i = 0; i < hallway.length; i++) {
                char c = hallway[i];
                if (c == '.') {
                    continue;
                }

                int targetIndex = c - 'A';
                Room target = rooms[targetIndex];
                if (!target.noMovesNecessary()) {
                    continue;
                }

                int targetPos = target.contents.length - 1;
                while (target.contents[targetPos] != '.') {
                    targetPos--;
                }

                int hallwayTargetPos = targetIndex + 1;
                int dist;
                if (i <= hallwayTargetPos) {
                    for (int j = i + 1; j <= hallwayTargetPos; j++) {
                        if (hallway[j] != '.') {
                            continue outer;
                        }
                    }

                    dist = 2 * (hallwayTargetPos - i + 1);
                    if (i == 0) {
                        dist--;
                    }
                } else {
                    for (int j = i - 1; j >= hallwayTargetPos + 1; j--) {
                        if (hallway[j] != '.') {
                            continue outer;
                        }
                    }

                    dist = 2 * (i - hallwayTargetPos);
                    if (i == hallway.length - 1) {
                        dist--;
                    }
                }

                dist += targetPos;
                Burrow copy = copy();
                copy.cost += cost(c, dist);
                copy.rooms[targetIndex].contents[targetPos] = c;
                copy.hallway[i] = '.';

                // putting an amphipod back into its room is always optimal - the order doesn't matter
                return List.of(copy);
            }

            // room -> hallway
            List<Burrow> moves = new ArrayList<>();
            for (int i = 0; i < rooms.length; i++) {
                Room r = rooms[i];
                if (r.noMovesNecessary()) {
                    continue;
                }

                for (int j = 0; j < r.contents.length; j++) {
                    char c = r.contents[j];
                    if (c == '.') {
                        continue;
                    }

                    for (int k = i + 1; k >= 0; k--) {
                        if (hallway[k] != '.') {
                            break;
                        }

                        int dist = 2 * (i + 2 - k) + j;
                        if (k == 0) {
                            dist--;
                        }

                        Burrow copy = copy();
                        copy.cost += cost(c, dist);
                        copy.hallway[k] = c;
                        copy.rooms[i].contents[j] = '.';
                        moves.add(copy);
                    }
                    for (int k = i + 2; k < hallway.length; k++) {
                        if (hallway[k] != '.') {
                            break;
                        }

                        int dist = 2 * (k - i - 1) + j;
                        if (k == hallway.length - 1) {
                            dist--;
                        }

                        Burrow copy = copy();
                        copy.cost += cost(c, dist);
                        copy.hallway[k] = c;
                        copy.rooms[i].contents[j] = '.';
                        moves.add(copy);
                    }

                    break;
                }
            }

            return moves;
        }

        boolean isDone() {
            for (char c : hallway) {
                if (c != '.') {
                    return false;
                }
            }

            for (Room r : rooms) {
                if (!r.noMovesNecessary()) {
                    return false;
                }
            }

            return true;
        }

        Burrow copy() {
            Burrow copy = new Burrow();
            copy.cost = cost;
            for (int i = 0; i < rooms.length; i++) {
                copy.rooms[i] = rooms[i].copy();
            }

            System.arraycopy(hallway, 0, copy.hallway, 0, hallway.length);
            return copy;
        }

        Burrow extend(char[][] extension) {
            if (rooms.length != 4 || extension.length != 4) {
                throw new RuntimeException();
            }

            Burrow copy = copy();
            for (int i = 0; i < rooms.length; i++) {
                Room r = rooms[i];
                if (r.contents.length != 2 || extension[i].length != 2) {
                    throw new RuntimeException();
                }

                copy.rooms[i] = new Room((char) ('A' + i), new char[] { r.contents[0], extension[i][0], extension[i][1], r.contents[1] });
            }

            return copy;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append("#############\n");
            b.append('#').append(hallway[0]).append(hallway[1]).append('.').append(hallway[2]).append('.').append(hallway[3]).append('.').append(hallway[4]).append('.').append(hallway[5]).append(hallway[6]).append('#').append('\n');
            b.append("###").append(rooms[0].contents[0]).append('#').append(rooms[1].contents[0]).append('#').append(rooms[2].contents[0]).append('#').append(rooms[3].contents[0]).append("###\n");
            for (int i = 1; i < rooms[0].contents.length; i++) {
                b.append("  #").append(rooms[0].contents[i]).append('#').append(rooms[1].contents[i]).append('#').append(rooms[2].contents[i]).append('#').append(rooms[3].contents[i]).append('#').append('\n');
            }

            return b + "  #########";
        }
    }

    static class Room {

        final char owner;
        final char[] contents;

        Room(char owner, int space) {
            this.owner = owner;
            this.contents = new char[space];
            Arrays.fill(this.contents, '.');
        }

        private Room(char owner, char[] contents) {
            this.owner = owner;
            this.contents = contents;
        }

        Room copy() {
            return new Room(owner, Arrays.copyOf(contents, contents.length));
        }

        boolean noMovesNecessary() {
            for (char c : contents) {
                if (c != '.' && c != owner) {
                    return false;
                }
            }

            return true;
        }
    }
}
