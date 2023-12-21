package com.company;

import java.util.Objects;

public class Main {

	public static void main(String[] args) {

		if(Objects.equals(args[0], "c")){
			long compressionStartTime = System.currentTimeMillis();
			HuffmanCoding.compress(args[1], Integer.parseInt(args[2]));
			long compressionEndTime = System.currentTimeMillis();
			float compressionTime = (compressionEndTime - compressionStartTime) / 1000F;
			System.out.println("Compression time = " + compressionTime + " sec");
		}

		else if(Objects.equals(args[0], "d")){
			long decompressionStartTime = System.currentTimeMillis();
			HuffmanCoding.decompress(args[1]);
			long decompressionEndTime = System.currentTimeMillis();
			float decompressionTime = (decompressionEndTime - decompressionStartTime) / 1000F;
			System.out.println("Decompression time = " + decompressionTime + " sec");
		}

		else
			System.out.println("Wrong input");

	}
}