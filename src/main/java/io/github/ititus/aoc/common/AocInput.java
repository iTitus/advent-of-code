package io.github.ititus.aoc.common;

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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class AocInput {

    private final AocDay day;

    public AocInput(AocDay day) {
        this.day = day;
    }

    private static String getToken() {
        try {
            return Files.readString(Path.of(AocInput.class.getResource("/token.txt").toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String readString() {
        try {
            return Files.readString(getInput());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public BigInteger[] readAsIntCodeMemory() {
        return Arrays.stream(readString().split(","))
                .map(String::strip)
                .map(BigIntegerMath::of)
                .toArray(BigInteger[]::new);
    }

    public List<String> readAllLines() {
        try {
            return Files.readAllLines(getInput());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Stream<String> lines() {
        try {
            return Files.lines(getInput());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public IntList readAllLinesAsInt() {
        try (Stream<String> stream = lines()) {
            return stream
                    .mapToInt(Integer::parseInt)
                    .collect(IntArrayList::new, IntList::add, IntList::addAll);
        }
    }

    public LongList readAllLinesAsLong() {
        try (Stream<String> stream = lines()) {
            return stream
                    .mapToLong(Long::parseLong)
                    .collect(LongArrayList::new, LongList::add, LongList::addAll);
        }
    }

    public List<BigInteger> readAllLinesAsBigInteger() {
        try (Stream<String> stream = lines()) {
            return stream
                    .map(BigIntegerMath::of)
                    .collect(Collectors.toList());
        }
    }

    public DoubleList readAllLinesAsDouble() {
        try (Stream<String> stream = lines()) {
            return stream
                    .mapToDouble(Double::parseDouble)
                    .collect(DoubleArrayList::new, DoubleList::add, DoubleList::addAll);
        }
    }

    public List<BigRational> readAllLinesAsBigRational() {
        try (Stream<String> stream = lines()) {
            return stream
                    .map(BigRational::of)
                    .collect(Collectors.toList());
        }
    }

    private Path getInput() {
        String resourcePath = String.format(Locale.ROOT, "/%d/%02d/input.txt", day.getYear(), day.getDay());
        URL url = AocInput.class.getResource(resourcePath);
        if (url == null) {
            Path p = Path
                    .of(String.format(Locale.ROOT, "src/main/resources/%d/%02d/input.txt", day.getYear(), day.getDay()))
                    .toAbsolutePath()
                    .normalize();
            if (Files.isRegularFile(p)) {
                return p;
            }

            System.out.println("Downloading input...");
            return downloadInput();
        }

        try {
            return Path.of(url.toURI()).toAbsolutePath().normalize();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Path downloadInput() {
        URL url;
        try {
            url = new URL(String.format(Locale.ROOT, "https://adventofcode.com/%d/day/%d/input", day.getYear(),
                    day.getDay()));
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }

        Path p = Path
                .of(String.format(Locale.ROOT, "src/main/resources/%d/%02d/input.txt", day.getYear(), day.getDay()))
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
}
