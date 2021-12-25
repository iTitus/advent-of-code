package io.github.ititus.aoc.aoc21.day25;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharList;

import java.util.List;

@Aoc(year = 2021, day = 25)
public class Day25 implements AocSolution {

    Cucumbers cucumbers;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        cucumbers = Cucumbers.parse(
                input.lines()
                        .map(String::strip)
                        .filter(l -> !l.isEmpty())
                        .toList()
        );
    }

    @Override
    public Object part1() {
        Cucumbers current = cucumbers;
        for (int n = 1; ; n++) {
            Cucumbers next = current.step();
            if (next == null) {
                return n;
            }

            current = next;
        }
    }

    @Override
    public Object part2() {
        return null;
    }

    record Cucumbers(
            int sizeX,
            int sizeY,
            CharList map
    ) {

        static Cucumbers parse(List<String> lines) {
            int sizeY = lines.size();
            int sizeX = -1;
            CharList map = null;
            for (String line : lines) {
                if (sizeX < 0) {
                    sizeX = line.length();
                    map = new CharArrayList(sizeX * sizeY);
                } else if (sizeX != line.length()) {
                    throw new RuntimeException();
                }

                for (int x = 0; x < sizeX; x++) {
                    map.add(line.charAt(x));
                }
            }

            return new Cucumbers(sizeX, sizeY, map);
        }

        Cucumbers step() {
            boolean didSomething = false;
            CharList oldMap = map;
            CharList newMap = new CharArrayList(oldMap);
            for (int y = 0; y < sizeY; y++) {
                for (int x = 0; x < sizeX; x++) {
                    int pos = x + y * sizeX;
                    if (oldMap.getChar(pos) == '>') {
                        int nextPos = ((x + 1) % sizeX) + y * sizeX;
                        if (oldMap.getChar(nextPos) == '.') {
                            newMap.set(pos, '.');
                            newMap.set(nextPos, '>');
                            didSomething = true;
                        }
                    }
                }
            }

            oldMap = newMap;
            newMap = new CharArrayList(oldMap);
            for (int y = 0; y < sizeY; y++) {
                for (int x = 0; x < sizeX; x++) {
                    int pos = x + y * sizeX;
                    if (oldMap.getChar(pos) == 'v') {
                        int nextPos = x + ((y + 1) % sizeY) * sizeX;
                        if (oldMap.getChar(nextPos) == '.') {
                            newMap.set(pos, '.');
                            newMap.set(nextPos, 'v');
                            didSomething = true;
                        }
                    }
                }
            }

            return didSomething ? new Cucumbers(sizeX, sizeY, newMap) : null;
        }
    }
}
