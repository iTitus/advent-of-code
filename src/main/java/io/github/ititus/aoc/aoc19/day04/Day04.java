package io.github.ititus.aoc.aoc19.day04;

import io.github.ititus.aoc.common.InputProvider;

import java.util.Arrays;

public class Day04 {

    public static void main(String[] args) {
        int[] range =
                Arrays.stream(InputProvider.readString(2019, 4).split("-")).map(String::strip).mapToInt(Integer::parseInt).toArray();
        int lower = range[0];
        int higher = range[1];


        int count1 = 0, count2 = 0;
        for (int i = lower; i <= higher; i++) {
            int minRepeatingCount = getMinRepeatingCount(i);
            if (minRepeatingCount >= 2) {
                count1++;
                if (minRepeatingCount == 2) {
                    count2++;
                }
            }
        }

        // 1
        System.out.println("### 1 ###");
        System.out.println(count1);

        // 2
        System.out.println("### 2 ###");
        System.out.println(count2);
    }

    private static int getMinRepeatingCount(int password) {
        char[] chars = String.valueOf(password).toCharArray();
        if (chars.length != 6) {
            return -1;
        }

        int minRepeatingCount = -1;
        int curRepeatingCount = 1;
        char last = chars[0];
        for (int i = 1; i < chars.length; i++) {
            char c = chars[i];

            if (c < last) {
                return -1;
            } else if (c == last) {
                curRepeatingCount++;
            }

            if (c > last || i == chars.length - 1) {
                if (curRepeatingCount > 1 && (minRepeatingCount == -1 || curRepeatingCount < minRepeatingCount)) {
                    minRepeatingCount = curRepeatingCount;
                }
                curRepeatingCount = 1;
            }

            last = c;
        }

        return minRepeatingCount;
    }
}
