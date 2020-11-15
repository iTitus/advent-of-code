package io.github.ititus.aoc.aoc19.day16;

import java.util.Arrays;
import java.util.stream.IntStream;

public class FlawedFrequencyTransmission {

    private static final int MESSAGE_LENGTH = 8;
    private static final int MESSAGE_OFFSET_LENGTH = 7;
    private static final int[] BASE_PATTERN = { 0, 1, 0, -1 };
    private static final int BASE_PATTERN_LENGTH = BASE_PATTERN.length;

    private final int offset;
    private final int[] numbers;

    public FlawedFrequencyTransmission(int repeat, boolean useOffset, int[] numbers) {
        this.offset = useOffset ? extractNumber(MESSAGE_OFFSET_LENGTH, numbers) : 0;
        this.numbers = IntStream.range(0, repeat)
                .flatMap(i -> Arrays.stream(numbers))
                .skip(offset)
                .toArray();
    }

    private static int extractNumber(int length, int[] arr) {
        int exp = 1;
        int n = 0;
        for (int i = length - 1; i >= 0; i--) {
            n += exp * arr[i];
            exp *= 10;
        }

        return n;
    }

    public int decode(long phaseCount) {
        phase(phaseCount);
        return extractNumber(MESSAGE_LENGTH, numbers);
    }

    private void phase(long phaseCount) {
        if (offset < numbers.length / 2) {
            int[] temp = new int[numbers.length];
            for (long i = 0; i < phaseCount; i++) {
                for (int j = numbers.length - 1; j >= 0; j--) {
                    int n = 0;
                    for (int k = j; k < numbers.length; k++) {
                        int p = BASE_PATTERN[((offset + k + 1) / (offset + j + 1)) % BASE_PATTERN_LENGTH];
                        n += numbers[k] * p;
                    }
                    temp[j] = Math.abs(n % 10);
                }
                System.arraycopy(temp, 0, numbers, 0, numbers.length);
            }
        } else {
            for (long i = 0; i < phaseCount; i++) {
                for (int j = numbers.length - 2; j >= 0; j--) {
                    numbers[j] = (numbers[j] + numbers[j + 1]) % 10;
                }
            }
        }
    }
}

