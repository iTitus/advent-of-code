package io.github.ititus.aoc.aoc20.day16;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

public class TicketReport {

    private final Map<String, TicketProperty> properties;
    private final Ticket myTicket;
    private final List<Ticket> nearbyTickets;

    private TicketReport(Map<String, TicketProperty> properties, Ticket myTicket, List<Ticket> nearbyTickets) {
        this.properties = properties;
        this.myTicket = myTicket;
        this.nearbyTickets = nearbyTickets;
    }

    public static Collector<String, TicketReportAccumulator, TicketReport> collector() {
        return Collector.of(
                TicketReportAccumulator::new,
                TicketReportAccumulator::accumulate,
                TicketReportAccumulator::combine,
                TicketReportAccumulator::finish
        );
    }

    public int getTicketScanningErrorRate() {
        return nearbyTickets.stream()
                .flatMapToInt(t ->
                        t.streamProperties()
                                .filter(propValue -> properties.values().stream().noneMatch(p -> p.matches(propValue)))
                )
                .sum();
    }

    private boolean isValid(Ticket t) {
        return t.streamProperties().noneMatch(i -> properties.values().stream().noneMatch(p -> p.matches(i)));
    }

    public long determineProperties(Predicate<String> resultFilter) {
        List<Ticket> ticketsToConsider = new ArrayList<>();
        if (!isValid(myTicket)) {
            throw new RuntimeException();
        }

        ticketsToConsider.add(myTicket);
        nearbyTickets.stream()
                .filter(this::isValid)
                .forEach(ticketsToConsider::add);

        int propertyCount = properties.size();
        List<Set<String>> validProperties = new ArrayList<>(propertyCount);
        for (int i = 0; i < propertyCount; i++) {
            validProperties.add(new HashSet<>(properties.keySet()));
        }

        // remove invalid properties
        for (int i = 0; i < propertyCount; i++) {
            Set<String> candidates = validProperties.get(i);
            for (Iterator<String> it = candidates.iterator(); it.hasNext(); ) {
                TicketProperty prop = properties.get(it.next());
                final int propIndex = i;
                if (ticketsToConsider.stream()
                        .mapToInt(t -> t.getProperty(propIndex))
                        .anyMatch(propValue -> !prop.matches(propValue))
                ) {
                    it.remove();
                }
            }
        }

        // remove unique properties from all other candidate sets
        boolean work;
        do {
            work = false;
            for (Set<String> candidates : validProperties) {
                if (candidates.size() == 1) {
                    for (Set<String> otherCandidates : validProperties) {
                        if (otherCandidates != candidates) {
                            work |= otherCandidates.removeAll(candidates);
                        }
                    }
                }
            }
        } while (work);

        // result multiplication and final check
        long result = 1;
        for (int i = 0; i < propertyCount; i++) {
            Set<String> candidates = validProperties.get(i);
            if (candidates.size() != 1) {
                throw new RuntimeException();
            }

            String propName = candidates.iterator().next();
            if (resultFilter.test(propName)) {
                result *= myTicket.getProperty(i);
            }
        }

        return result;
    }

    private enum Phase {
        PROPERTIES, MY_TICKET, NEARBY_TICKETS
    }

    private static class TicketReportAccumulator {

        private final List<TicketProperty> properties = new ArrayList<>();
        private final List<Ticket> nearbyTickets = new ArrayList<>();
        private Ticket myTicket;

        private int expectedProperties;
        private Pattern ticketPattern;
        private boolean seenTitle, seenContent;
        private Phase phase = Phase.PROPERTIES;

        public void accumulate(String line) {
            switch (phase) {
                case PROPERTIES -> {
                    if (line.isBlank()) {
                        expectedProperties = properties.size();
                        if (expectedProperties == 0) {
                            throw new RuntimeException();
                        }

                        ticketPattern = Pattern.compile(
                                range(0, expectedProperties)
                                        .mapToObj(i -> "(?<property" + i + ">\\d+)")
                                        .collect(Collectors.joining(",", "^", "$"))
                        );

                        seenTitle = seenContent = false;
                        phase = Phase.MY_TICKET;
                    } else {
                        Matcher m = TicketProperty.P.matcher(line);
                        if (!m.matches()) {
                            throw new RuntimeException();
                        }

                        properties.add(TicketProperty.of(m));
                    }
                }
                case MY_TICKET -> {
                    if (!seenTitle) {
                        if (!"your ticket:".equals(line)) {
                            throw new RuntimeException();
                        }

                        seenTitle = true;
                    } else if (!seenContent) {
                        Matcher m = ticketPattern.matcher(line);
                        if (!m.matches()) {
                            throw new RuntimeException();
                        }

                        myTicket = Ticket.of(expectedProperties, m);
                        seenContent = true;
                    } else if (!line.isBlank()) {
                        throw new RuntimeException();
                    } else {
                        seenTitle = seenContent = false;
                        phase = Phase.NEARBY_TICKETS;
                    }
                }
                case NEARBY_TICKETS -> {
                    if (!seenTitle) {
                        if (!"nearby tickets:".equals(line)) {
                            throw new RuntimeException();
                        }

                        seenTitle = true;
                    } else {
                        Matcher m = ticketPattern.matcher(line);
                        if (!m.matches()) {
                            throw new RuntimeException();
                        }

                        nearbyTickets.add(Ticket.of(expectedProperties, m));
                        seenContent = true;
                    }
                }
            }
        }

        public TicketReportAccumulator combine(TicketReportAccumulator other) {
            throw new UnsupportedOperationException();
        }

        public TicketReport finish() {
            if (phase != Phase.NEARBY_TICKETS || !seenTitle || !seenContent) {
                throw new RuntimeException();
            }

            return new TicketReport(
                    properties.stream().collect(toMap(TicketProperty::getName, Function.identity())),
                    myTicket,
                    nearbyTickets
            );
        }
    }
}
