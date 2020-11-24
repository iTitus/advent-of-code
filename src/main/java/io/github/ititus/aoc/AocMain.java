package io.github.ititus.aoc;

import com.google.common.reflect.ClassPath;
import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocDay;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.stream.Collectors;

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
        var solutions = findSolutions(AocMain.class.getPackage(), finalYear, finalDay);
        solutions.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEachOrdered(AocMain::execute);
    }

    private static void execute(Map.Entry<AocDay, AocSolution> entry) {
        AocDay day = entry.getKey();

        AocInput input = new AocInput(day);
        AocSolution aoc = entry.getValue();

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

    @SuppressWarnings("UnstableApiUsage")
    private static Map<AocDay, AocSolution> findSolutions(Package root, Integer finalYear, Integer finalDay) {
        String prefix = root.getName() + ".";
        ClassLoader cl = AocMain.class.getClassLoader();

        ClassPath cp;
        try {
            cp = ClassPath.from(cl);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return cp.getAllClasses().stream()
                .filter(ci -> ci.getName().startsWith(prefix))
                .map(ClassPath.ClassInfo::load)
                .filter(AocSolution.class::isAssignableFrom)
                .filter(c -> {
                    Aoc aoc = c.getDeclaredAnnotation(Aoc.class);
                    if (aoc == null) {
                        return false;
                    }

                    if ((finalYear == null || finalDay == null) && aoc.skip()) {
                        return false;
                    }

                    return (finalYear == null || finalYear == aoc.year())
                            && (finalDay == null || finalDay == aoc.day());
                })
                .collect(Collectors.<Class<?>, AocDay, AocSolution>toMap(
                        c -> new AocDay(c.getDeclaredAnnotation(Aoc.class)),
                        c -> {
                            try {
                                return (AocSolution) c.getConstructor().newInstance();
                            } catch (ReflectiveOperationException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ));
    }
}
