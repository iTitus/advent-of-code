package io.github.ititus.aoc;

import io.github.ititus.aoc.common.AocDay;
import io.github.ititus.aoc.common.AocDayInput;
import io.github.ititus.aoc.common.AocDaySolution;
import io.github.ititus.aoc.common.ReflectionUtil;
import org.apache.commons.cli.*;

import java.util.Map;

public final class AocMain {

    private AocMain() {
    }

    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption(Option.builder("h").longOpt("help").desc("display help").build());
        options.addOption(Option.builder("y").longOpt("year").hasArg().type(Number.class).desc("year to execute").build());
        options.addOption(Option.builder("d").longOpt("day").hasArg().type(Number.class).desc("day to execute").build());

        CommandLineParser parser = new DefaultParser();
        HelpFormatter helpFormatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("Unexpected exception:" + e.getMessage());
            helpFormatter.printHelp("advent-of-code", options);
            return;
        }

        if (cmd.hasOption("help")) {
            helpFormatter.printHelp("advent-of-code", options);
            return;
        }

        Integer year = null;
        Integer day = null;

        if (cmd.hasOption("year")) {
            Object o = cmd.getParsedOptionValue("year");
            if (o instanceof Number) {
                year = ((Number) o).intValue();
            }
        }

        if (cmd.hasOption("day")) {
            Object o = cmd.getParsedOptionValue("day");
            if (o instanceof Number) {
                day = ((Number) o).intValue();
            }
        }

        System.out.printf("Executing all days that match year=%s, day=%s%n", year, day);

        Integer finalYear = year;
        Integer finalDay = day;
        var solutions = ReflectionUtil.findSolutions(AocMain.class.getPackage());
        solutions.entrySet().stream()
                .filter(e -> {
                    boolean yearMatches = finalYear == null || finalYear == e.getKey().getYear();
                    boolean dayMatches = finalDay == null || finalDay == e.getKey().getDay();
                    return yearMatches && dayMatches;
                })
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(AocMain::execute);
    }

    private static void execute(Map.Entry<AocDay, AocDaySolution> entry) {
        AocDay day = entry.getKey();

        AocDayInput input = AocDayInput.of(day);
        AocDaySolution aoc = entry.getValue();

        System.out.println("\n" + "#".repeat(80));
        System.out.printf("Executing year=%04d, day=%02d%n%n", day.getYear(), day.getDay());

        System.out.println("Tests:");
        try {
            aoc.executeTests();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return;
        }

        System.out.println("Input:");
        try {
            aoc.readInput(input);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return;
        }

        System.out.println("\nPart 1:");
        try {
            System.out.println(aoc.part1());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        System.out.println("\nPart 2:");
        try {
            System.out.println(aoc.part2());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
