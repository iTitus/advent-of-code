package io.github.ititus.aoc.aoc21.day11;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntImmutableList;
import it.unimi.dsi.fastutil.ints.IntList;

import java.util.List;

@Aoc(year = 2021, day = 11)
public class Day11 implements AocSolution {

    static final int MAX_ENERGY = 9;

    Ground initial;

    @Override
    public void executeTests() {
    }

    @Override
    public void readInput(AocInput input) {
        initial = Ground.parse(input.readAllLines());
    }

    @Override
    public Object part1() {
        Ground current = initial;
        for (int i = 0; i < 100; i++) {
            current = current.step();
        }

        return current.flashes();
    }

    @Override
    public Object part2() {
        Ground current = initial;
        int i;
        for (i = 1; ; i++) {
            int flashes = current.flashes();
            current = current.step();
            int newFlashes = current.flashes();
            if (newFlashes - flashes == current.sizeX() * current.sizeY()) {
                break;
            }
        }

        return i;
    }

    record Ground(
            int sizeX,
            int sizeY,
            int flashes,
            IntList energies
    ) {

        Ground {
            if (sizeX != 10 || sizeY != 10) {
                throw new IllegalArgumentException();
            }
        }

        static Ground parse(List<String> lines) {
            IntList energies = new IntArrayList(100);
            int sizeY = lines.size();
            int sizeX = -1;
            for (String line : lines) {
                line = line.strip();
                if (sizeX < 0) {
                    sizeX = line.length();
                } else if (line.length() != sizeX) {
                    throw new IllegalArgumentException();
                }

                line.chars()
                        .map(c -> c - '0')
                        .forEachOrdered(energies::add);
            }

            return new Ground(sizeX, sizeY, 0, new IntImmutableList(energies));
        }

        Ground step() {
            IntList energies = new IntArrayList(this.energies);
            for (int i = 0; i < energies.size(); i++) {
                energies.set(i, energies.getInt(i) + 1);
            }

            int flashes = 0;
            boolean didSomething;
            do
            {
                didSomething = false;
                for (int y = 0; y < sizeY; y++) {
                    for (int x = 0; x < sizeX; x++) {
                        int i = x + y * sizeX;
                        int e = energies.getInt(i);
                        if (e > MAX_ENERGY) {
                            flashes++;
                            energies.set(i, 0);
                            for (int dy = -1; dy <= 1; dy++) {
                                for (int dx = -1; dx <= 1; dx++) {
                                    int nx = x + dx;
                                    int ny = y + dy;
                                    if (nx < 0 || nx >= sizeX || ny < 0 || ny >= sizeY) {
                                        continue;
                                    } else if (nx == x && ny == y) {
                                        continue;
                                    }

                                    int ni = nx + ny * sizeX;
                                    int n = energies.getInt(ni);
                                    if (n != 0) {
                                        energies.set(ni, n + 1);
                                        didSomething = true;
                                    }
                                }
                            }
                        }
                    }
                }
            } while (didSomething);

            return new Ground(sizeX, sizeY, this.flashes + flashes, new IntImmutableList(energies));
        }
    }
}
