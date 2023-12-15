package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CalculateFrequency {

    public Map<String, Integer> calculateFrequencies(String filePath, int chunkSize) {
        Map<String, Integer> frequencies = new HashMap<>();

        try {
            Path path = Paths.get(filePath);
            byte[] data = Files.readAllBytes(path);

            for (int i = 0; i <= data.length - chunkSize; i += chunkSize) {
                byte[] chunk = new byte[chunkSize];
                System.arraycopy(data, i, chunk, 0, chunkSize);

                String chunkKey = bytesToHex(chunk);
                frequencies.put(chunkKey, frequencies.getOrDefault(chunkKey, 0) + 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return frequencies;
    }

    public void displayFrequencies(Map<String, Integer> frequencies) {
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            System.out.println("Chunk: " + entry.getKey() + ", Frequency: " + entry.getValue());
        }
    }

    public String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }
}

