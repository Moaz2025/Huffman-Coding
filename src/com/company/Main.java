package com.company;

public class Main {

	public static void main(String[] args) {

		long compressionStartTime = System.currentTimeMillis();
		HuffmanCoding.encode("C:\\Users\\Moaz\\Desktop\\Tests\\input.pdf", 5);
		long compressionEndTime = System.currentTimeMillis();
		float compressionTime = (compressionEndTime - compressionStartTime) / 1000F;
		System.out.println("Compression time = " + compressionTime + " sec");

		long decompressionStartTime = System.currentTimeMillis();
		HuffmanCoding.decode("C:\\Users\\Moaz\\Desktop\\Tests\\20011969.5.input.pdf.hc");
		long decompressionEndTime = System.currentTimeMillis();
		float decompressionTime = (decompressionEndTime - decompressionStartTime) / 1000F;
		System.out.println("Decompression time = " + decompressionTime + " sec");
	}
}