package io.github.ititus.aoc;

import io.github.ititus.aoc.common.AocDay;
import io.github.ititus.aoc.common.AocFileInput;

public final class Downloader {

    private Downloader() {
    }

    public static void main(String[] args) {
        for (int year = 2015; year <= 2020; year++) {
            for (int day = 1; day <= 25; day++) {
                AocDay aocDay = new AocDay(year, day);
                try {
                    new AocFileInput(aocDay).readString();
                } catch (Exception e) {
                    System.out.println("Could not load " + aocDay);
                }
            }
        }
    }
}
