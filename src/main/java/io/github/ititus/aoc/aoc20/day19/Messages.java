package io.github.ititus.aoc.aoc20.day19;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;

import static java.lang.Integer.parseInt;

public class Messages {

    private final Int2ObjectMap<Rule> rules;
    private final List<String> messages;

    private Messages(Int2ObjectMap<Rule> rules, List<String> messages) {
        this.rules = rules;
        this.messages = messages;
    }

    public static Collector<String, MessagesAccumulator, Messages> collector() {
        return Collector.of(
                MessagesAccumulator::new,
                MessagesAccumulator::accumulate,
                MessagesAccumulator::combine,
                MessagesAccumulator::finish
        );
    }

    public long countMatches(int rule) {
        Rule r = rules.get(rule);
        Pattern p = Pattern.compile(r.toRegex(rules));
        return messages.stream()
                .filter(p.asMatchPredicate())
                .count();
    }

    public long countAdvancedMatches(int rule) {
        Int2ObjectOpenHashMap<Rule> rules = new Int2ObjectOpenHashMap<>(this.rules);
        rules.put(8, null); // OrRule.of("42 | 42 8")
        rules.put(11, null); // OrRule.of("42 31 | 42 11 31")
        Rule r = rules.get(rule);
        if (!(r instanceof CompoundRule)
                || !Arrays.equals(((CompoundRule) r).getRules(), new int[] { 8, 11 })) {
            throw new RuntimeException();
        }

        Pattern r42 = Pattern.compile(rules.get(42).toRegex(rules));
        Pattern r31 = Pattern.compile(rules.get(31).toRegex(rules));

        return messages.stream()
                .filter(m -> {
                    // matcher for r_42^m r_31^n where m>n>=1
                    // this is disgusting
                    Matcher matcher1 = r42.matcher(m);
                    Matcher matcher2 = r31.matcher(m);
                    int matches1 = 0, matches2;
                    int lastMatchEnd1 = 0, lastMatchEnd2;

                    while (matcher1.find()) {
                        boolean valid = lastMatchEnd1 == matcher1.start();
                        if (valid) {
                            lastMatchEnd1 = matcher1.end();
                            matches1++;
                        }

                        matches2 = 0;
                        if (matches1 >= 2 && matcher2.find(lastMatchEnd1) && matcher2.start() == lastMatchEnd1) {
                            lastMatchEnd2 = matcher2.end();
                            matches2++;
                            if (lastMatchEnd2 == m.length() && matches1 > matches2 && matches2 >= 1) {
                                return true;
                            }

                            while (matcher2.find()) {
                                if (lastMatchEnd2 != matcher2.start()) {
                                    break;
                                }

                                lastMatchEnd2 = matcher2.end();
                                matches2++;

                                if (lastMatchEnd2 == m.length() && matches1 > matches2 && matches2 >= 1) {
                                    return true;
                                }
                            }
                        }

                        if (!valid) {
                            break;
                        }
                    }

                    return false;
                })
                .count();
    }

    private static class MessagesAccumulator {

        private final Int2ObjectMap<Rule> rules = new Int2ObjectOpenHashMap<>();
        private final List<String> messages = new ArrayList<>();

        private boolean parsingRules = true;

        public void accumulate(String line) {
            if (line.isBlank()) {
                if (parsingRules) {
                    parsingRules = false;
                    return;
                } else {
                    throw new RuntimeException();
                }
            }

            if (parsingRules) {
                int idx = line.indexOf(':');
                if (idx < 0) {
                    throw new RuntimeException();
                }

                int id = parseInt(line.substring(0, idx));
                String rule = line.substring(idx + 2);

                Rule r;
                if (rule.charAt(0) == '"') {
                    r = new LiteralRule(rule.substring(1, rule.length() - 1));
                } else {
                    r = OrRule.of(rule);
                }
                rules.put(id, r);
            } else {
                messages.add(line);
            }
        }

        public MessagesAccumulator combine(MessagesAccumulator other) {
            throw new UnsupportedOperationException();
        }

        public Messages finish() {
            return new Messages(
                    rules,
                    messages
            );
        }
    }
}
