package io.github.ititus.aoc;

import io.github.ititus.io.IO;
import io.github.ititus.math.number.BigIntegerMath;
import io.github.ititus.math.number.BigRational;
import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class InputProvider {

    private InputProvider() {
    }

    public static String readString(int year, int day) {
        try {
            return Files.readString(getInput(year, day));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static List<String> readAllLines(int year, int day) {
        try {
            return Files.readAllLines(getInput(year, day));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static Stream<String> lines(int year, int day) {
        try {
            return Files.lines(getInput(year, day));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static IntList readAllLinesAsInt(int year, int day) {
        try (Stream<String> stream = lines(year, day)) {
            return stream
                    .mapToInt(Integer::parseInt)
                    .collect(IntArrayList::new, IntList::add, IntList::addAll);
        }
    }

    public static LongList readAllLinesAsLong(int year, int day) {
        try (Stream<String> stream = lines(year, day)) {
            return stream
                    .mapToLong(Long::parseLong)
                    .collect(LongArrayList::new, LongList::add, LongList::addAll);
        }
    }

    public static List<BigInteger> readAllLinesAsBigInteger(int year, int day) {
        try (Stream<String> stream = lines(year, day)) {
            return stream
                    .map(BigIntegerMath::of)
                    .collect(Collectors.toList());
        }
    }

    public static DoubleList readAllLinesAsDouble(int year, int day) {
        try (Stream<String> stream = lines(year, day)) {
            return stream
                    .mapToDouble(Double::parseDouble)
                    .collect(DoubleArrayList::new, DoubleList::add, DoubleList::addAll);
        }
    }

    public static List<BigRational> readAllLinesAsBigRational(int year, int day) {
        try (Stream<String> stream = lines(year, day)) {
            return stream
                    .map(BigRational::of)
                    .collect(Collectors.toList());
        }
    }

    public static Path getInput(int year, int day) {
        if (year < 2015 || day < 1 || day > 25) {
            throw new IllegalArgumentException();
        }

        String resourcePath = "/" + year + "/" + (day < 10 ? "0" + day : day) + "/input.txt";
        URL url = InputProvider.class.getResource(resourcePath);
        if (url == null) {
            System.out.println("Downloading input.");
            return downloadInput(year, day);
        }

        try {
            return Path.of(url.toURI()).toAbsolutePath().normalize();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path downloadInput(int year, int day) {
        URL url;
        try {
            url = new URL("https://adventofcode.com/" + year + "/day/" + day + "/input");
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }

        Path p = Path
                .of("src/main/resources/" + year + "/" + (day < 10 ? "0" + day : day) + "/input.txt")
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(p.getParent());

            URLConnection c = url.openConnection();
            c.setRequestProperty("Cookie", "session=" + getToken());
            c.connect();

            try (
                    ReadableByteChannel in = Channels.newChannel(c.getInputStream());
                    FileChannel out = IO.openCreateNewFileChannel(p)
            ) {
                IO.copy(in, out);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return p;
    }

    private static String getToken() {
        try {
            return Files.readString(Path.of(InputProvider.class.getResource("/token.txt").toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
