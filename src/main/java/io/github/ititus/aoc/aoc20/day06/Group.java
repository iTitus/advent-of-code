package io.github.ititus.aoc.aoc20.day06;

import it.unimi.dsi.fastutil.chars.Char2IntMap;
import it.unimi.dsi.fastutil.chars.Char2IntMaps;
import it.unimi.dsi.fastutil.chars.Char2IntOpenHashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

public class Group {

    private final int size;
    private final Char2IntMap answers;

    private Group(int size, Char2IntMap answers) {
        this.size = size;
        this.answers = answers;
    }

    public int getAnyYesCount() {
        return answers.size();
    }

    public long getAllYesCount() {
        return answers.char2IntEntrySet().stream()
                .mapToInt(Char2IntMap.Entry::getIntValue)
                .filter(n -> n == size)
                .count();
    }

    public static class GroupAccumulator {

        private final List<Group> groups = new ArrayList<>();

        private final Char2IntMap answers = new Char2IntOpenHashMap();
        private boolean collectingGroup;
        private int size;

        public static Collector<String, GroupAccumulator, List<Group>> collector() {
            return Collector.of(
                    GroupAccumulator::new,
                    GroupAccumulator::accumulate,
                    GroupAccumulator::combine,
                    GroupAccumulator::finish
            );
        }

        private void addGroup() {
            if (!collectingGroup) {
                throw new RuntimeException();
            }

            groups.add(new Group(size, Char2IntMaps.unmodifiable(new Char2IntOpenHashMap(answers))));

            collectingGroup = false;
            size = 0;
            answers.clear();
        }

        private void accumulate(String line) {
            if (line.isBlank()) {
                addGroup();
                return;
            }

            collectingGroup = true;

            size++;
            for (int i = 0; i < line.length(); i++) {
                char answer = line.charAt(i);
                if (answer < 'a' || answer > 'z') {
                    throw new RuntimeException();
                }

                answers.merge(answer, 1, Integer::sum);
            }
        }

        private GroupAccumulator combine(GroupAccumulator other) {
            throw new UnsupportedOperationException();
        }

        private List<Group> finish() {
            addGroup();
            return groups;
        }
    }
}
