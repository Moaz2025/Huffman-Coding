package com.company;

import java.util.Map;

public class Main {

    public static void main(String[] args) {

        String filePath = "C:\\Users\\Moaz\\Desktop\\Huffman-Coding\\input.txt";

        int n = 1; // number of bytes

        CalculateFrequency calculateFrequency = new CalculateFrequency();

        Map<String, Integer> frequencies = calculateFrequency.calculateFrequencies(filePath, n);
        calculateFrequency.displayFrequencies(frequencies);

    }
}
