package ee.aoc.day6;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;


public class Main {
    public static void main(String[] args) {
        List<String> linesFromClassPathResourceFile = InputFileReaderUtil.getLinesFromClassPathResourceFile("/input_day6.txt");
        partOne(linesFromClassPathResourceFile);
        partTwo(linesFromClassPathResourceFile);
        // Why do I call I planet?
    }

    private static void partTwo(List<String> linesFromClassPathResourceFile) {
        Map<String, String> planetToOrbitableParent = linesFromClassPathResourceFile.stream()
                .map(inputLine -> inputLine.split("\\)"))
                .collect(Collectors.toMap(x -> x[1], x -> x[0]));
        System.out.println(findLeastOrbitJumpsBetweenPlanets("YOU", "SAN", planetToOrbitableParent) - 2);
        // minus two because, needed is jumps between the objects YOU and SAN are orbiting - not between YOU and SAN
    }

    private static int findLeastOrbitJumpsBetweenPlanets(String first, String second, Map<String, String> planetToOrbitableParent) {
        List<String> firstAncestors = findAncestors(first, planetToOrbitableParent);
        System.out.println(firstAncestors);
        List<String> secondAncestors = findAncestors(second, planetToOrbitableParent);
        System.out.println(secondAncestors);

        int leastOrbitJumps = Integer.MAX_VALUE;
        for (int i = 0; i < firstAncestors.size(); i++) {
            String firstAncestor = firstAncestors.get(i);
            int indexOfFirstAncestorInSeconds = secondAncestors.indexOf(firstAncestor);
            if (indexOfFirstAncestorInSeconds == -1)
                continue;

            if (i + indexOfFirstAncestorInSeconds < leastOrbitJumps) {
                leastOrbitJumps = i + indexOfFirstAncestorInSeconds;
            }
        }
        return leastOrbitJumps;
    }

    private static List<String> findAncestors(String planet, Map<String, String> planetToOrbitableParent) {
        List<String> result = new ArrayList<>();
        String key = planet;
        while (key != null) {
            result.add(key);
            key = planetToOrbitableParent.get(key);
        }

        return result;
    }

    private static void partOne(List<String> linesFromClassPathResourceFile) {
        Map<String, String> planetToOrbitableParent = linesFromClassPathResourceFile.stream()
                .map(inputLine -> inputLine.split("\\)"))
                .collect(Collectors.toMap(x -> x[1], x -> x[0]));

        int orbitCount = planetToOrbitableParent.keySet().stream()
                .mapToInt(planet -> countOrbits(planet, planetToOrbitableParent))
                .sum();
        System.out.println(orbitCount);
    }

    private static int countOrbits(String planet, Map<String, String> planetToOrbitableParent) {
        String orbitParent = planetToOrbitableParent.get(planet);
        if (orbitParent == null) {
            return 0;
        }

        return 1 + countOrbits(orbitParent, planetToOrbitableParent);
    }
}
