package io.github.ititus.aoc;

public final class Downloader {

    private Downloader() {
    }

    public static void main(String[] args) {
        for (int year = 2015; year <= 2020; year++) {
            for (int day = 1; day <= 25; day++) {
                try {
                    InputProvider.getInput(year, day);
                } catch (Exception e) {
                    System.out.println("Could not load year " + year + " day " + day);
                }
            }
        }
    }
}
