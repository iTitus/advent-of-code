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
        return n * switch (c) {
            case 'A' -> 1;
            case 'B' -> 10;
            case 'C' -> 100;
            case 'D' -> 1000;
            default -> throw new IllegalArgumentException();
        };
    }

    private static int dfs(Burrow startConfiguration) {
        if (startConfiguration.isDone()) {
            return startConfiguration.cost;
        }

        Deque<Burrow> stack = new ArrayDeque<>();
        stack.push(startConfiguration);

        int minCost = -1;
        while (!stack.isEmpty()) {
            Burrow b = stack.pop();
            for (Burrow move : b.allMoves()) {
                if (minCost < 0 || move.cost < minCost) {
                    if (move.isDone()) {
                        minCost = move.cost;
                    } else {
                        stack.push(move);
                    }
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
        List<String> lines = input.lines()
                .filter(s -> !s.isBlank())
                .skip(2)
                .toList();

        final int space = lines.size() - 1;
        startConfiguration = new Burrow();
        Arrays.fill(startConfiguration.hallway, '.');
        for (int roomIndex = 0; roomIndex < startConfiguration.rooms.length; roomIndex++) {
            Room r = new Room((char) ('A' + roomIndex), space);
            for (int pos = 0; pos < space; pos++) {
                r.contents[pos] = lines.get(pos).charAt(3 + roomIndex * 2);
            }

            startConfiguration.rooms[roomIndex] = r;
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
        final char[] hallway = new char[2 + (rooms.length - 1) + 2];
        int cost;

        List<Burrow> allMoves() {
            // hallway -> room
            outer:
            for (int hallwayPos = 0; hallwayPos < hallway.length; hallwayPos++) {
                char c = hallway[hallwayPos];
                if (c == '.') {
                    continue;
                }

                int targetIndex = c - 'A';
                Room target = rooms[targetIndex];
                if (!target.noMovesNecessary()) {
                    continue;
                }

                int hallwayTargetPos = targetIndex + 1;
                int distToRoom;
                if (hallwayPos <= hallwayTargetPos) {
                    for (int otherHallwayPos = hallwayPos + 1; otherHallwayPos <= hallwayTargetPos; otherHallwayPos++) {
                        if (hallway[otherHallwayPos] != '.') {
                            continue outer;
                        }
                    }

                    distToRoom = 2 * (hallwayTargetPos - hallwayPos + 1);
                    if (hallwayPos == 0) {
                        distToRoom--;
                    }
                } else {
                    for (int otherHallwayPos = hallwayPos - 1; otherHallwayPos >= hallwayTargetPos + 1; otherHallwayPos--) {
                        if (hallway[otherHallwayPos] != '.') {
                            continue outer;
                        }
                    }

                    distToRoom = 2 * (hallwayPos - hallwayTargetPos);
                    if (hallwayPos == hallway.length - 1) {
                        distToRoom--;
                    }
                }

                int targetPos = target.contents.length - 1;
                while (target.contents[targetPos] != '.') {
                    targetPos--;
                }

                Burrow copy = copy();
                copy.cost += cost(c, distToRoom + targetPos);
                copy.rooms[targetIndex].contents[targetPos] = c;
                copy.hallway[hallwayPos] = '.';

                // putting an amphipod back into its room is always optimal - the order doesn't matter
                return List.of(copy);
            }

            // room -> hallway
            List<Burrow> moves = new ArrayList<>();
            for (int roomIndex = 0; roomIndex < rooms.length; roomIndex++) {
                Room r = rooms[roomIndex];
                if (r.noMovesNecessary()) {
                    continue;
                }

                for (int roomPos = 0; roomPos < r.contents.length; roomPos++) {
                    char c = r.contents[roomPos];
                    if (c == '.') {
                        continue;
                    }

                    for (int hallwayPos = roomIndex + 1; hallwayPos >= 0; hallwayPos--) {
                        if (hallway[hallwayPos] != '.') {
                            break;
                        }

                        int dist = 2 * (roomIndex + 2 - hallwayPos) + roomPos;
                        if (hallwayPos == 0) {
                            dist--;
                        }

                        Burrow copy = copy();
                        copy.cost += cost(c, dist);
                        copy.hallway[hallwayPos] = c;
                        copy.rooms[roomIndex].contents[roomPos] = '.';
                        moves.add(copy);
                    }

                    for (int hallwayPos = roomIndex + 2; hallwayPos < hallway.length; hallwayPos++) {
                        if (hallway[hallwayPos] != '.') {
                            break;
                        }

                        int dist = 2 * (hallwayPos - roomIndex - 1) + roomPos;
                        if (hallwayPos == hallway.length - 1) {
                            dist--;
                        }

                        Burrow copy = copy();
                        copy.cost += cost(c, dist);
                        copy.hallway[hallwayPos] = c;
                        copy.rooms[roomIndex].contents[roomPos] = '.';
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
                if (!r.isDone()) {
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

        boolean isDone() {
            for (char c : contents) {
                if (c != owner) {
                    return false;
                }
            }

            return true;
        }
    }
}
