package ee.aoc.day12;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang3.tuple.Triple;

public class Part1 {

    /* Input
<x=-4, y=3, z=15>
<x=-11, y=-10, z=13>
<x=2, y=2, z=18>
<x=7, y=-1, z=0>
     */
    public static void main(String[] args) {
        List<Planet> planets = Arrays.asList(
                new Planet(Triple.of(-4, 3, 15)),
                new Planet(Triple.of(-11, -10, 13)),
                new Planet(Triple.of(2, 2, 18)),
                new Planet(Triple.of(7, -1, 0))
        );

        List<Integer> initialX = planets.stream().map(x -> x.getPosition().getLeft()).collect(Collectors.toList());
        List<Integer> initialY = planets.stream().map(x -> x.getPosition().getMiddle()).collect(Collectors.toList());
        List<Integer> initialZ = planets.stream().map(x -> x.getPosition().getRight()).collect(Collectors.toList());

        long steps = 0;
        long xCycleSteps = -1;
        long yCycleSteps = -1;
        long zCycleSteps = -1;

        while (true) {
            planets.forEach(planet -> planet.applyGravity(planets));
            planets.forEach(Planet::applyVelocity);
            steps++;

            if (xCycleSteps == -1) {
                boolean posEqual = planets.stream().map(x -> x.getPosition().getLeft()).collect(Collectors.toList()).equals(initialX);
                boolean dxEqual = planets.stream().allMatch(x -> x.getVelocity().getLeft() == 0);
                if (posEqual && dxEqual) xCycleSteps = steps;
            }

            if (yCycleSteps == -1) {
                boolean posEqual = planets.stream().map(x -> x.getPosition().getMiddle()).collect(Collectors.toList()).equals(initialY);
                boolean dyEqual = planets.stream().allMatch(x -> x.getVelocity().getMiddle() == 0);
                if (posEqual && dyEqual) yCycleSteps = steps;
            }

            if (zCycleSteps == -1) {
                boolean posEqual = planets.stream().map(x -> x.getPosition().getRight()).collect(Collectors.toList()).equals(initialZ);
                boolean dzEqual = planets.stream().allMatch(x -> x.getVelocity().getRight() == 0);
                if (posEqual && dzEqual) zCycleSteps = steps;
            }

            if (xCycleSteps != -1 && yCycleSteps != -1 && zCycleSteps != -1) break;
        }

        System.out.println(xCycleSteps);
        System.out.println(yCycleSteps);
        System.out.println(zCycleSteps);
        System.out.println(lcm(xCycleSteps, yCycleSteps, zCycleSteps));
    }


//    https://stackoverflow.com/a/40531215/3178428
    private static long gcd(long x, long y) {
        return (y == 0) ? x : gcd(y, x % y);
    }

    public static long gcd(long... numbers) {
        return Arrays.stream(numbers).reduce(0, (x, y) -> gcd(x, y));
    }

    public static long lcm(long... numbers) {
        return Arrays.stream(numbers).reduce(1, (x, y) -> x * (y / gcd(x, y)));
    }

    private static int getPlanetsEnergySum(List<Planet> planets) {
        return planets.stream()
                .mapToInt(Planet::calculateEnergy)
                .sum();
    }

    @Getter
    @Setter
    private static class Planet {
        private Triple<Integer, Integer, Integer> position;
        private Triple<Integer, Integer, Integer> velocity = Triple.of(0, 0, 0);

        public Planet(Triple<Integer, Integer, Integer> initialPos) {
            position = initialPos;
        }

        public int calculateEnergy() {
            return absSumCoords(position) * absSumCoords(velocity);
        }

        public void applyVelocity() {
            position = sumCoords(position, velocity);
        }

        public void applyGravity(List<Planet> planets) {
            int dx = 0;
            int dy = 0;
            int dz = 0;
            for (Planet planet : planets) {
                if (position.getLeft() > planet.getPosition().getLeft()) {
                    dx--;
                } else if (position.getLeft() < planet.getPosition().getLeft()) {
                    dx++;
                }

                if (position.getMiddle() > planet.getPosition().getMiddle()) {
                    dy--;
                } else if (position.getMiddle() < planet.getPosition().getMiddle()) {
                    dy++;
                }

                if (position.getRight() > planet.getPosition().getRight()) {
                    dz--;
                } else if (position.getRight() < planet.getPosition().getRight()) {
                    dz++;
                }
            }
            Triple<Integer, Integer, Integer> dVelocity = Triple.of(dx, dy, dz);
            velocity = sumCoords(dVelocity, velocity);
        }

        @Override
        public String toString() {
            return String.format("pos=<x= %d, y= %d, z= %d>, vel=<x= %d, y= %d, z= %d>",
                    position.getLeft(), position.getMiddle(), position.getRight(),
                    velocity.getLeft(), velocity.getMiddle(), velocity.getRight());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Planet planet = (Planet)o;
            return Objects.equals(position, planet.position) &&
                    Objects.equals(velocity, planet.velocity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, velocity);
        }
    }

    private static int absSumCoords(Triple<Integer, Integer, Integer> coords) {
        return Math.abs(coords.getLeft()) + Math.abs(coords.getMiddle()) + Math.abs(coords.getRight());
    }

    private static Triple<Integer, Integer, Integer> sumCoords(Triple<Integer, Integer, Integer> first, Triple<Integer, Integer, Integer> second) {
        return Triple.of(first.getLeft() + second.getLeft(), first.getMiddle() + second.getMiddle(), first.getRight() + second.getRight());
    }
}
