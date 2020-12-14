package io.github.ititus.aoc.aoc20.day14;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongListIterator;

import java.util.List;

import static io.github.ititus.aoc.common.FastUtilStreams.stream;
import static io.github.ititus.math.base.BaseConverters.BINARY;
import static java.lang.Long.parseLong;

public class Memory {

    private static final int BIT_LENGTH = 36;

    private final Long2LongMap memory = new Long2LongOpenHashMap();
    private final boolean version2;
    private final LongArrayList allFloatingMasks = new LongArrayList(1 << 9);
    private long maskOnes, maskZeros, maskFloating;

    public Memory(boolean version2) {
        this.version2 = version2;
    }

    public long execute(List<String> commands) {
        for (String command : commands) {
            if (command.startsWith("ma")) {
                String mask = command.substring(7); // "mask = <value>"
                if (mask.length() != BIT_LENGTH) {
                    throw new RuntimeException();
                }
                maskOnes = BINARY.decodeToLong(mask
                        .replace('X', '0')
                );
                maskZeros = BINARY.decodeToLong(mask
                        .replace('1', 'X')
                        .replace('0', '1')
                        .replace('X', '0')
                );
                maskFloating = BINARY.decodeToLong(mask
                        .replace('1', '0')
                        .replace('X', '1')
                );

                if (version2) {
                    allFloatingMasks.clear();

                    int bc = Long.bitCount(maskFloating);
                    int[] floatingBits = new int[bc];
                    for (int i = 0, j = 0; j < BIT_LENGTH; j++) {
                        if ((maskFloating & (1L << j)) != 0) {
                            floatingBits[i++] = j;
                        }
                    }

                    long maskCount = 1L << bc;
                    for (long i = 0; i < maskCount; i++) {
                        long floatMask = maskOnes;
                        for (int j = 0; j < bc; j++) {
                            if ((i & (1L << j)) != 0) {
                                floatMask |= 1L << floatingBits[j];
                            }
                        }
                        allFloatingMasks.add(floatMask);
                    }
                }
            } else {
                int index = command.indexOf('=');
                if (index < 0) {
                    throw new RuntimeException();
                }

                long address = parseLong(command.substring(4, index - 2)); // "mem[<address>] = <value>"
                long value = parseLong(command.substring(index + 2));
                if (version2) {
                    address = address & maskZeros;
                    for (LongListIterator it = allFloatingMasks.iterator(); it.hasNext(); ) {
                        long mask = it.nextLong();
                        memory.put(address | mask, value);
                    }
                } else {
                    memory.put(address, (value & maskFloating) | maskOnes);
                }
            }
        }

        return stream(memory.values()).sum();
    }
}
