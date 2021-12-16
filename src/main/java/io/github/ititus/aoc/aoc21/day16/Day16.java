package io.github.ititus.aoc.aoc21.day16;

import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;

@Aoc(year = 2021, day = 16)
public class Day16 implements AocSolution {

    Packet p;

    @Override
    public void executeTests() {
        AocInput input;

        input = new AocStringInput("D2FE28");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("38006F45291200");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("EE00D40C823060");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("8A004A801A8002F478");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("620080001611562C8802118E34");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("C0015000016115A2E0802F182340");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("A0016C880162017C3686B18A3D4780");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("C200B40A82");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("04005AC33890");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("880086C3E88112");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("CE00C43D881120");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("D8005AC2A8F0");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("F600BC2D8F");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("9C005AC2F8F0");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());

        System.out.println();
        input = new AocStringInput("9C0141080250320F1802104A08");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        p = Packet.read(new BitReader(input.readString().strip()));
    }

    @Override
    public Object part1() {
        return p.sumOfAllVersions();
    }

    @Override
    public Object part2() {
        return p.value();
    }
}
