package io.github.ititus.aoc.common;

import io.github.ititus.commons.io.HttpStatus;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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

            System.out.println("Downloading input for " + day.getDay() + "/" + day.getYear() + "...");
            return downloadInput();
        }

        try {
            return Path.of(url.toURI()).toAbsolutePath().normalize();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private Path downloadInput() {
        URI uri = URI.create(String.format(Locale.ROOT, "https://adventofcode.com/%d/day/%d/input", day.getYear(), day.getDay()));

        Path p = Path
                .of(String.format(Locale.ROOT, "src/main/resources/%d/%02d/input.txt", day.getYear(), day.getDay()))
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(p.getParent());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        try (HttpClient hc = HttpClient.newBuilder().build()) {
            HttpRequest req = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("Cookie", "session=" + getToken())
                    .build();
            HttpResponse<Path> resp = hc.send(req, HttpResponse.BodyHandlers.ofFile(p));

            HttpStatus status = HttpStatus.of(resp.statusCode());
            if (!status.isOk()) {
                Files.deleteIfExists(p);
                throw new RuntimeException(status.toString());
            }

            return resp.body();
        } catch (IOException e1) {
            throw new UncheckedIOException(e1);
        } catch (InterruptedException e2) {
            throw new RuntimeException(e2);
        }
    }
}
