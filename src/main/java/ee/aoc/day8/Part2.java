package ee.aoc.day8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import ee.aoc.InputFileReaderUtil;


public class Part2 {
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


        int[][] decodedImage = Arrays.stream(imageLayers.get(0))
                .map(int[]::clone)
                .toArray(int[][]::new);

        for (int i = 1; i < imageLayers.size(); i++) {
            int[][] newLayer = imageLayers.get(i);
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < length; k++) {
                    if (decodedImage[j][k] == 2) decodedImage[j][k] = newLayer[j][k];
                }
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                System.out.print(decodedImage[i][j] == 0 ? "-" : decodedImage[i][j]);
            }
            System.out.println();
        }
    }
}
