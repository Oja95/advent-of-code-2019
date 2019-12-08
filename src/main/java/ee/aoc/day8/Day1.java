package ee.aoc.day8;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ee.aoc.InputFileReaderUtil;


public class Day1 {
    public static void main(String[] args) {
        List<String> linesFromClassPathResourceFile = InputFileReaderUtil.getLinesFromClassPathResourceFile("/input_day8.txt");
        partOne(linesFromClassPathResourceFile);
    }

    private static void partOne(List<String> linesFromClassPathResourceFile) {
        int length = 25;
        int height = 6;

        List<int[][]> imageLayers = new ArrayList<>();

        List<Integer> inputList = new ArrayList<>();
        for (char input : linesFromClassPathResourceFile.get(0).toCharArray()) {
            inputList.add(Integer.parseInt(String.valueOf(input)));
        }

        Iterator<Integer> iterator = inputList.iterator();

        while (iterator.hasNext()) {
            int[][] imageLayer = new int[height][length];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < length; j++) {
                    imageLayer[i][j] = iterator.next();
                }
            }
            imageLayers.add(imageLayer);
        }

        int layerIndexWithLeastZeroes = Integer.MIN_VALUE;
        int leastZeroes = Integer.MAX_VALUE;
        for (int i = 0; i < imageLayers.size(); i++) {
            int[][] imageLayer = imageLayers.get(i);
            int zeroCount = getNumCount(length, height, imageLayer, 0);
            System.out.println(zeroCount);
            if (zeroCount < leastZeroes) {
                layerIndexWithLeastZeroes = i;
                leastZeroes = zeroCount;
            }
        }

        System.out.println(layerIndexWithLeastZeroes);
        int[][] imageLayerWithLeastZeroes = imageLayers.get(layerIndexWithLeastZeroes);

        System.out.println(getNumCount(length, height, imageLayerWithLeastZeroes, 1) * getNumCount(length, height, imageLayerWithLeastZeroes, 2));
    }

    private static int getNumCount(int length, int height, int[][] imageLayer, int num) {
        int numCount = 0;
        for (int j = 0; j < height; j++) {
            for (int k = 0; k < length; k++) {
                if (imageLayer[j][k] == num) numCount++;
            }
        }
        return numCount;
    }
}
