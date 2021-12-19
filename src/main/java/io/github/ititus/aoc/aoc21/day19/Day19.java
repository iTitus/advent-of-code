package io.github.ititus.aoc.aoc21.day19;

import com.google.common.collect.Sets;
import io.github.ititus.aoc.common.Aoc;
import io.github.ititus.aoc.common.AocInput;
import io.github.ititus.aoc.common.AocSolution;
import io.github.ititus.aoc.common.AocStringInput;
import io.github.ititus.data.pair.Pair;
import io.github.ititus.math.vector.Vec3i;

import java.util.*;
import java.util.stream.Collectors;

@Aoc(year = 2021, day = 19)
public class Day19 implements AocSolution {

    List<Pair<Vec3i, Set<Vec3i>>> matchedScanners;

    private static Pair<Vec3i, Set<Vec3i>> fit(Set<Vec3i> beacons, Set<Vec3i> beaconsToMatch) {
        for (Rotation r : Rotation.VALUES) {
            Set<Vec3i> rotated = r.rotate(beaconsToMatch);
            for (Vec3i b1 : beacons) {
                for (Vec3i b2 : rotated) {
                    Vec3i delta = b1.subtract(b2);
                    Set<Vec3i> translated = translate(delta, rotated);
                    if (Sets.intersection(translated, beacons).size() >= 12) {
                        return Pair.of(delta.negate(), translated);
                    }
                }
            }
        }

        return null;
    }

    private static Set<Vec3i> translate(Vec3i translate, Set<Vec3i> vs) {
        return vs.stream()
                .map(translate::add)
                .collect(Collectors.toSet());
    }

    @Override
    public void executeTests() {
        AocInput input = new AocStringInput("""
                --- scanner 0 ---
                404,-588,-901
                528,-643,409
                -838,591,734
                390,-675,-793
                -537,-823,-458
                -485,-357,347
                -345,-311,381
                -661,-816,-575
                -876,649,763
                -618,-824,-621
                553,345,-567
                474,580,667
                -447,-329,318
                -584,868,-557
                544,-627,-890
                564,392,-477
                455,729,728
                -892,524,684
                -689,845,-530
                423,-701,434
                7,-33,-71
                630,319,-379
                443,580,662
                -789,900,-551
                459,-707,401
                                
                --- scanner 1 ---
                686,422,578
                605,423,415
                515,917,-361
                -336,658,858
                95,138,22
                -476,619,847
                -340,-569,-846
                567,-361,727
                -460,603,-452
                669,-402,600
                729,430,532
                -500,-761,534
                -322,571,750
                -466,-666,-811
                -429,-592,574
                -355,545,-477
                703,-491,-529
                -328,-685,520
                413,935,-424
                -391,539,-444
                586,-435,557
                -364,-763,-893
                807,-499,-711
                755,-354,-619
                553,889,-390
                                
                --- scanner 2 ---
                649,640,665
                682,-795,504
                -784,533,-524
                -644,584,-595
                -588,-843,648
                -30,6,44
                -674,560,763
                500,723,-460
                609,671,-379
                -555,-800,653
                -675,-892,-343
                697,-426,-610
                578,704,681
                493,664,-388
                -671,-858,530
                -667,343,800
                571,-461,-707
                -138,-166,112
                -889,563,-600
                646,-828,498
                640,759,510
                -630,509,768
                -681,-892,-333
                673,-379,-804
                -742,-814,-386
                577,-820,562
                                
                --- scanner 3 ---
                -589,542,597
                605,-692,669
                -500,565,-823
                -660,373,557
                -458,-679,-417
                -488,449,543
                -626,468,-788
                338,-750,-386
                528,-832,-391
                562,-778,733
                -938,-730,414
                543,643,-506
                -524,371,-870
                407,773,750
                -104,29,83
                378,-903,-323
                -778,-728,485
                426,699,580
                -438,-605,-362
                -469,-447,-387
                509,732,623
                647,635,-688
                -868,-804,481
                614,-800,639
                595,780,-596
                                
                --- scanner 4 ---
                727,592,562
                -293,-554,779
                441,611,-461
                -714,465,-776
                -743,427,-804
                -660,-479,-426
                832,-632,460
                927,-485,-438
                408,393,-506
                466,436,-512
                110,16,151
                -258,-428,682
                -393,719,612
                -211,-452,876
                808,-476,-593
                -575,615,604
                -485,667,467
                -680,325,-822
                -627,-443,-432
                872,-547,-609
                833,512,582
                807,604,487
                839,-516,451
                891,-625,532
                -652,-548,-490
                30,-46,-14""");
        readInput(input);
        System.out.println(part1());
        System.out.println(part2());
    }

    @Override
    public void readInput(AocInput input) {
        List<String> lines = input.readAllLines();
        List<Set<Vec3i>> scanners = new ArrayList<>();

        Set<Vec3i> beacons = null;
        for (String line : lines) {
            if (line.startsWith("--- scanner ")) {
                if (beacons != null) {
                    scanners.add(beacons);
                }

                beacons = new HashSet<>();
            } else if (!line.isEmpty()) {
                if (beacons == null) {
                    throw new IllegalStateException();
                }

                beacons.add(new Vec3i(Arrays.stream(line.split(",", 3)).mapToInt(Integer::parseInt).toArray()));
            }
        }

        if (beacons != null) {
            scanners.add(beacons);
        }

        matchedScanners = new ArrayList<>(List.of(Pair.of(new Vec3i(0, 0, 0), scanners.get(0))));
        List<Set<Vec3i>> scannersLeft = new ArrayList<>(scanners.subList(1, scanners.size()));
        for (int i = 0; i < matchedScanners.size(); i++) {
            Pair<Vec3i, Set<Vec3i>> existing = matchedScanners.get(i);
            for (ListIterator<Set<Vec3i>> it = scannersLeft.listIterator(); it.hasNext(); ) {
                Set<Vec3i> toMatch = it.next();
                Pair<Vec3i, Set<Vec3i>> fitBeacons = fit(existing.b(), toMatch);
                if (fitBeacons != null) {
                    it.remove();
                    matchedScanners.add(fitBeacons);
                }
            }
        }

        if (!scannersLeft.isEmpty()) {
            throw new RuntimeException();
        }
    }

    @Override
    public Object part1() {
        return matchedScanners.stream()
                .map(Pair::b)
                .flatMap(Collection::stream)
                .distinct()
                .count();
    }

    @Override
    public Object part2() {
        return matchedScanners.stream()
                .map(Pair::a)
                .flatMapToInt(v -> matchedScanners.stream()
                        .map(Pair::a)
                        .filter(v_ -> v != v_)
                        .map(v::subtract)
                        .mapToInt(Vec3i::manhattanDistance)
                )
                .max().orElseThrow();
    }

    enum Rotation {

        R_PX_PY(new Vec3i(1, 0, 0), new Vec3i(0, 1, 0)),
        R_PX_NY(new Vec3i(1, 0, 0), new Vec3i(0, -1, 0)),
        R_PX_PZ(new Vec3i(1, 0, 0), new Vec3i(0, 0, 1)),
        R_PX_NZ(new Vec3i(1, 0, 0), new Vec3i(0, 0, -1)),

        R_NX_PY(new Vec3i(-1, 0, 0), new Vec3i(0, 1, 0)),
        R_NX_NY(new Vec3i(-1, 0, 0), new Vec3i(0, -1, 0)),
        R_NX_PZ(new Vec3i(-1, 0, 0), new Vec3i(0, 0, 1)),
        R_NX_NZ(new Vec3i(-1, 0, 0), new Vec3i(0, 0, -1)),

        R_PY_PX(new Vec3i(0, 1, 0), new Vec3i(1, 0, 0)),
        R_PY_NX(new Vec3i(0, 1, 0), new Vec3i(-1, 0, 0)),
        R_PY_PZ(new Vec3i(0, 1, 0), new Vec3i(0, 0, 1)),
        R_PY_NZ(new Vec3i(0, 1, 0), new Vec3i(0, 0, -1)),

        R_NY_PX(new Vec3i(0, -1, 0), new Vec3i(1, 0, 0)),
        R_NY_NX(new Vec3i(0, -1, 0), new Vec3i(-1, 0, 0)),
        R_NY_PZ(new Vec3i(0, -1, 0), new Vec3i(0, 0, 1)),
        R_NY_NZ(new Vec3i(0, -1, 0), new Vec3i(0, 0, -1)),

        R_PZ_PX(new Vec3i(0, 0, 1), new Vec3i(1, 0, 0)),
        R_PZ_NX(new Vec3i(0, 0, 1), new Vec3i(-1, 0, 0)),
        R_PZ_PY(new Vec3i(0, 0, 1), new Vec3i(0, 1, 0)),
        R_PZ_NY(new Vec3i(0, 0, 1), new Vec3i(0, -1, 0)),

        R_NZ_PX(new Vec3i(0, 0, -1), new Vec3i(1, 0, 0)),
        R_NZ_NX(new Vec3i(0, 0, -1), new Vec3i(-1, 0, 0)),
        R_NZ_PY(new Vec3i(0, 0, -1), new Vec3i(0, 1, 0)),
        R_NZ_NY(new Vec3i(0, 0, -1), new Vec3i(0, -1, 0));

        static final List<Rotation> VALUES = List.of(values());

        private final Vec3i front;
        private final Vec3i up;
        private final Vec3i right;

        Rotation(Vec3i front, Vec3i up) {
            this.front = front;
            this.up = up;
            this.right = front.cross(up);
        }

        Vec3i rotate(Vec3i v) {
            return new Vec3i(
                    front.x() * v.x() + up.x() * v.y() + right.x() * v.z(),
                    front.y() * v.x() + up.y() * v.y() + right.y() * v.z(),
                    front.z() * v.x() + up.z() * v.y() + right.z() * v.z()
            );
        }

        Set<Vec3i> rotate(Set<Vec3i> vs) {
            return vs.stream()
                    .map(this::rotate)
                    .collect(Collectors.toSet());
        }
    }
}
