package io.github.ititus.aoc.common;

import io.github.ititus.io.IO;

import java.io.IOException;
import java.io.UncheckedIOException;
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
import java.util.Locale;
import java.util.stream.Stream;

public final class AocFileInput implements AocInput {

    private final AocDay day;

    public AocFileInput(AocDay day) {
        this.day = day;
    }

    private static String getToken() {
        try {
            return Files.readString(Path.of(AocInput.class.getResource("/token.txt").toURI()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Stream<String> lines() {
        try {
            return Files.lines(getInput());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String readString() {
        try {
            return Files.readString(getInput());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public List<String> readAllLines() {
        try {
            return Files.readAllLines(getInput());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
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
