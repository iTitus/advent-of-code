package io.github.ititus.aoc.aoc20.day04;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collector;

public class Passport {

    private static final Pattern HEX_COLOR = Pattern.compile("^#[0-9a-f]{6}$");
    private static final Pattern ID = Pattern.compile("^\\d{9}$");

    private final String birthYear;
    private final String issueYear;
    private final String expirationYear;
    private final String height;
    private final String hairColor;
    private final String eyeColor;
    private final String passportId;
    private final String countryId;

    private Passport(String birthYear, String issueYear, String expirationYear, String height, String hairColor,
                     String eyeColor, String passportId, String countryId) {
        this.birthYear = birthYear;
        this.issueYear = issueYear;
        this.expirationYear = expirationYear;
        this.height = height;
        this.hairColor = hairColor;
        this.eyeColor = eyeColor;
        this.passportId = passportId;
        this.countryId = countryId;
    }

    private static boolean validateBoundedInt(String number, int min, int max) {
        int n;
        try {
            n = Integer.parseInt(number);
        } catch (NumberFormatException ignored) {
            return false;
        }

        return min <= n && n <= max;
    }

    private static boolean validateHeight(String height) {
        if (height.endsWith("cm")) {
            return validateBoundedInt(height.substring(0, height.length() - "cm".length()), 150, 193);
        } else if (height.endsWith("in")) {
            return validateBoundedInt(height.substring(0, height.length() - "in".length()), 59, 76);
        }

        return false;
    }

    private static boolean validateHexColor(String color) {
        return HEX_COLOR.matcher(color).matches();
    }

    private static boolean validateColor(String color) {
        return switch (color) {
            case "amb", "blu", "brn", "gry", "grn", "hzl", "oth" -> true;
            default -> false;
        };
    }

    private static boolean validateId(String id) {
        return ID.matcher(id).matches();
    }

    public boolean areRequiredFieldsPresent() {
        return birthYear != null
                && issueYear != null
                && expirationYear != null
                && height != null
                && hairColor != null
                && eyeColor != null
                && passportId != null;
    }

    public boolean areRequiredFieldsPresentAndValid() {
        if (!areRequiredFieldsPresent()) {
            return false;
        } else if (!validateBoundedInt(birthYear, 1920, 2002)) {
            return false;
        } else if (!validateBoundedInt(issueYear, 2010, 2020)) {
            return false;
        } else if (!validateBoundedInt(expirationYear, 2020, 2030)) {
            return false;
        } else if (!validateHeight(height)) {
            return false;
        } else if (!validateHexColor(hairColor)) {
            return false;
        } else if (!validateColor(eyeColor)) {
            return false;
        }

        return validateId(passportId);
    }


    public static class PassportAccumulator {

        private final List<Passport> passports = new ArrayList<>();

        private boolean collectingPassport;
        /**
         * byr
         */
        private String birthYear;
        /**
         * iyr
         */
        private String issueYear;
        /**
         * eyr
         */
        private String expirationYear;
        /**
         * hgt
         */
        private String height;
        /**
         * hcl
         */
        private String hairColor;
        /**
         * ecl
         */
        private String eyeColor;
        /**
         * pid
         */
        private String passportId;
        /**
         * cid
         */
        private String countryId;

        public static Collector<String, PassportAccumulator, List<Passport>> collector() {
            return Collector.of(
                    PassportAccumulator::new,
                    PassportAccumulator::accumulate,
                    PassportAccumulator::combine,
                    PassportAccumulator::finish
            );
        }

        private void addPassport() {
            if (!collectingPassport) {
                throw new RuntimeException();
            }

            passports.add(new Passport(
                    birthYear,
                    issueYear,
                    expirationYear,
                    height,
                    hairColor,
                    eyeColor,
                    passportId,
                    countryId
            ));
            collectingPassport = false;
            birthYear = issueYear = expirationYear = height = hairColor = eyeColor = passportId = countryId = null;
        }

        private void accumulate(String line) {
            if (line.isBlank()) {
                addPassport();
                return;
            }

            collectingPassport = true;

            for (String kv : line.split(" ")) {
                String[] split = kv.split(":");
                if (split.length != 2) {
                    throw new RuntimeException();
                }

                String k = split[0];
                String v = split[1];

                if (v.isBlank()) {
                    throw new RuntimeException();
                }

                switch (k) {
                    case "byr" -> {
                        if (birthYear != null) {
                            throw new RuntimeException();
                        }
                        birthYear = v;
                    }
                    case "iyr" -> {
                        if (issueYear != null) {
                            throw new RuntimeException();
                        }
                        issueYear = v;
                    }
                    case "eyr" -> {
                        if (expirationYear != null) {
                            throw new RuntimeException();
                        }
                        expirationYear = v;
                    }
                    case "hgt" -> {
                        if (height != null) {
                            throw new RuntimeException();
                        }
                        height = v;
                    }
                    case "hcl" -> {
                        if (hairColor != null) {
                            throw new RuntimeException();
                        }
                        hairColor = v;
                    }
                    case "ecl" -> {
                        if (eyeColor != null) {
                            throw new RuntimeException();
                        }
                        eyeColor = v;
                    }
                    case "pid" -> {
                        if (passportId != null) {
                            throw new RuntimeException();
                        }
                        passportId = v;
                    }
                    case "cid" -> {
                        if (countryId != null) {
                            throw new RuntimeException();
                        }
                        countryId = v;
                    }
                    default -> throw new RuntimeException();
                }
            }
        }

        private PassportAccumulator combine(PassportAccumulator other) {
            throw new UnsupportedOperationException();
        }

        private List<Passport> finish() {
            addPassport();
            return passports;
        }
    }
}
