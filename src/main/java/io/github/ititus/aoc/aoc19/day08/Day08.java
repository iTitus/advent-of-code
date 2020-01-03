package io.github.ititus.aoc.aoc19.day08;

import io.github.ititus.aoc.InputProvider;

import java.io.PrintWriter;

public class Day08 {

    public static void main(String[] args) {
        String encodedImage = InputProvider.readString(2019, 8).strip();

        SpaceEncodedImage image = new SpaceEncodedImage(25, 6);
        image.decode(encodedImage);

        // 1
        System.out.println("### 1 ###");
        System.out.println(image.getChecksum());

        // 2
        System.out.println("### 2 ###");
        image.render(new PrintWriter(System.out, true));
    }
}
