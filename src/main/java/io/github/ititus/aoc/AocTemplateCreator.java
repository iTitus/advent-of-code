package io.github.ititus.aoc;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Locale;

public class AocTemplateCreator {

    public static void main(String[] args) throws Exception {
        Path sources = Path.of("src/main/java/io/github/ititus/aoc");
        Path runs = Path.of(".run");

        for (int year = 2015; year <= 2023; year++) {
            for (int day = 1; day <= 25; day++) {
                createSourceFile(sources, year, day);
                createRunConfigFile(runs, year, day);
            }
        }
    }

    private static void createSourceFile(Path dir, int year, int day) throws IOException {
        String yearString = String.format(Locale.ROOT, "%02d", year % 100);
        String dayString = String.format(Locale.ROOT, "%02d", day);

        Path parent = dir.resolve("aoc" + yearString).resolve("day" + dayString);
        Files.createDirectories(parent);

        Path file = parent.resolve("Day" + dayString + ".java");
        try {
            Files.writeString(file, createSource(year, day), StandardOpenOption.CREATE_NEW);
        } catch (FileAlreadyExistsException ignored) {
        }
    }

    private static String createSource(int year, int day) {
        String yearString = String.format(Locale.ROOT, "%02d", year % 100);
        String dayString = String.format(Locale.ROOT, "%02d", day);
        return "package io.github.ititus.aoc.aoc" + yearString + ".day" + dayString + ";\n" +
                """

                        import io.github.ititus.aoc.common.Aoc;
                        import io.github.ititus.aoc.common.AocInput;
                        import io.github.ititus.aoc.common.AocSolution;
                                                
                        """ +
                "@Aoc(year = " + year + ", day = " + day + ")\n" +
                "public class Day" + dayString + " implements AocSolution {\n" +
                """
                                        
                            @Override
                            public void executeTests() {
                            }
                                        
                            @Override
                            public void readInput(AocInput input) {
                            }
                                        
                            @Override
                            public Object part1() {
                                return null;
                            }
                                        
                            @Override
                            public Object part2() {
                                return null;
                            }
                        }
                        """;
    }

    private static void createRunConfigFile(Path dir, int year, int day) throws IOException {
        String dayString = String.format(Locale.ROOT, "%02d", day);

        Files.createDirectories(dir);

        Path file = dir.resolve("Day" + dayString + " (" + year + ").run.xml");
        try {
            Files.writeString(file, createRunConfig(year, day), StandardOpenOption.CREATE_NEW);
        } catch (FileAlreadyExistsException ignored) {
        }
    }

    private static String createRunConfig(int year, int day) {
        String dayString = String.format(Locale.ROOT, "%02d", day);
        return "<component name=\"ProjectRunConfigurationManager\">\n" +
                "  <configuration default=\"false\" name=\"Day" + dayString + " (" + year + ")\" type=\"Application\" factoryName=\"Application\">\n" +
                """
                          <option name="MAIN_CLASS_NAME" value="io.github.ititus.aoc.AocMain" />
                          <module name="advent-of-code.main" />
                        """ +
                "  <option name=\"PROGRAM_PARAMETERS\" value=\"-y " + year + " -d " + day + "\" />\n" +
                """
                            <method v="2">
                              <option name="Make" enabled="true" />
                            </method>
                          </configuration>
                        </component>
                        """;
    }
}
