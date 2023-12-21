package com.company;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.io.DataOutputStream;
import java.io.DataInputStream;

public class HuffmanCoding {

	private static SortedLinkedList<Node> calculateFrequency(File file) {
		SortedLinkedList<Node> list = new SortedLinkedList<>();
		HashMap<Byte, Integer> hashMap = new HashMap<>();

		FileInputStream fileR;
		try {
			fileR = new FileInputStream(file);
			long length = file.length();
			long it = 0;
			int character;
			while (length > it) {
				it += 1;
				character = fileR.read();
				byte currentByte = (byte) character;
				hashMap.merge(currentByte, 1, Integer::sum);
			}
			fileR.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (Map.Entry<Byte, Integer> entry : hashMap.entrySet()) {
			list.sortedAdd(new Node (entry.getKey(), entry.getValue(), null, null));
		}

		return list;
	}

	private static Node createNode(File file) {
		SortedLinkedList<Node> list = HuffmanCoding.calculateFrequency(file);

		while (list.size() >= 2) {
			Node right = list.remove();
			Node left = list.remove();
			Node node = new Node((byte)0, right.weight + left.weight, right, left);
			list.sortedAdd(node);
		}
		return list.getFirst();	
	}
	
	private static HashMap<Byte, boolean[]> getCoding(Node tree) {
		HashMap<Byte, boolean[]> hashMap = new HashMap<>();
		HuffmanCoding.getCodingNode(tree.right, hashMap, new boolean[0], true);
		HuffmanCoding.getCodingNode(tree.left, hashMap, new boolean[0], false);
		return hashMap;
	}

	private static void getCodingNode(Node tree, HashMap<Byte, boolean[]> hashMap, boolean[] tab, boolean bool) {
		boolean[] tmp = new boolean[tab.length+1];
		System.arraycopy(tab, 0, tmp, 0, tab.length);
		tmp[tab.length] = bool;
		if (tree.left == null && tree.right == null)
			hashMap.put(tree.chunk, tmp);
		else {
			HuffmanCoding.getCodingNode(tree.right, hashMap, tmp, true);
			HuffmanCoding.getCodingNode(tree.left, hashMap, tmp, false);
		}
	}

	private static void writeFile(File file, HashMap<Byte, boolean[]> hashMap) {
		try {
			FileOutputStream fileW = new FileOutputStream(file);
			DataOutputStream dataW = new DataOutputStream(fileW);
			dataW.writeInt(hashMap.size());
			for (Map.Entry<Byte, boolean[]> entry : hashMap.entrySet()) {
				fileW.write(entry.getKey());
				for (boolean bool : entry.getValue()) {
				 	if (bool)
				 		fileW.write('1');
				 	else 
				 		fileW.write('0');
				}
				fileW.write(' ');
			}
			fileW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static HashMap<Byte, boolean[]> readFile(File file) {
		HashMap<Byte, boolean[]> hashMap = new HashMap<>();
		try {
			FileInputStream fileR = new FileInputStream(file);
			DataInputStream dataR = new DataInputStream(fileR);
			int length = dataR.readInt();
			byte currentByte = (byte)fileR.read();
			while (length != 0) {
				byte character = currentByte;
				ArrayList<Byte> binary = new ArrayList<>();
				currentByte = (byte)fileR.read();
				while (currentByte != ' ') {
					binary.add(currentByte);
					currentByte = (byte)fileR.read();
				}
				boolean[] bool = new boolean[binary.size()];
				for (int i = 0; i < binary.size(); i += 1)
					bool[i] = binary.get(i) == '1';
				hashMap.put(character, bool);
				currentByte = (byte)fileR.read();
				length -= 1;
			}
			fileR.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hashMap;
	}

	public static void compress(String inputFile, int chunkSize) {
		File file = new File(inputFile);
		String parentDirectory = file.getParent();
		Path inputFilePath = Paths.get(inputFile);
		String inputFileName = inputFilePath.getFileName().toString();
		int lastDotIndex = inputFileName.lastIndexOf('.');
		String fileExtension = inputFileName.substring(lastDotIndex + 1);
		String outputFileName = "20011969." + chunkSize + "." + inputFileName.substring(0, lastDotIndex) + "." + fileExtension +  ".hc";
		File outputFile = new File(parentDirectory, outputFileName);
		Node node = HuffmanCoding.createNode(file);

		HashMap<Byte, boolean[]> hashMap = HuffmanCoding.getCoding(node);
		HuffmanCoding.writeFile(outputFile, hashMap);
		try {
			FileInputStream fileR = new FileInputStream(file);

			ArrayList<boolean[]> list = new ArrayList<>();
			byte[] buffer = new byte[10];
			int n = fileR.read(buffer);
			while (n != -1) {
				for (int k = 0; k < n; k += 1){
					list.add(hashMap.get(buffer[k]));
				}
				buffer = new byte[10];
				n = fileR.read(buffer);
			}
			fileR.close();

			int length = 0;
			for (boolean[] bool : list)
				length += bool.length;
			if (length % 8 != 0) {
				length += 8 - length % 8;
			}

			boolean[] group = new boolean[length];
			int i = 0;
			for (boolean[] booleans : list) {
				for (boolean aBoolean : booleans) {
					group[i] = aBoolean;
					i += 1;
				}
			}
			while (i < length) {
				group[i] = false;
				i += 1;
			}

			FileOutputStream fileW = new FileOutputStream(outputFile, true); // to write after EOF

			DataOutputStream dataW = new DataOutputStream(fileW);
			dataW.writeLong(file.length());
			fileW.write(' ');

			for (int j = 0; j < group.length / 8; j += 1) {
				byte b = 0;
				for (int k = 0; k < 8; k += 1) {
					if (group[j * 8 + k])
						b = (byte)(b + (byte)(1 << k));
				}
				fileW.write(b);
			}
			fileW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void getTreeNode(byte character, boolean[] bool, Node node) {
		for (int i = 0; i < bool.length-1; i += 1) {
			if (bool[i]) {
				if (node.right == null)
					node.right = new Node((byte)0, 0, null, null);
				node = node.right;
			}
			else {
				if (node.left == null)
					node.left = new Node((byte)0, 0, null, null);
				node = node.left;
			}
		}
		if (bool[bool.length-1])
			node.right = new Node(character, 0, null, null);
		else 
			node.left = new Node(character, 0, null, null);
	}

	private static Node getTree(HashMap<Byte, boolean[]> hashMap) {
		Node node = new Node((byte)0, 0, null, null);
		for (Map.Entry<Byte, boolean[]> entry : hashMap.entrySet()) {
			HuffmanCoding.getTreeNode(entry.getKey(), entry.getValue(), node);
		}
		return node;
	}

	public static void decompress(String inputFile) {
		File file = new File(inputFile);
		String parentDirectory = file.getParent();
		Path inputFilePath = Paths.get(inputFile);
		String inputFileName = inputFilePath.getFileName().toString();
		int lastDotIndex = inputFileName.lastIndexOf('.');
		String outputFileName = "extracted." + inputFileName.substring(0, lastDotIndex);
		File outputFile = new File(parentDirectory, outputFileName);

		HashMap<Byte, boolean[]> hashMap = readFile(file);
		Node tree = getTree(hashMap);
		ArrayList<Boolean> list = new ArrayList<>();
		long fileLength = 0L;

		try {
			FileInputStream fileR = new FileInputStream(file);
			DataInputStream dataR = new DataInputStream(fileR);
			int length = dataR.readInt();
			byte currentByte = (byte)fileR.read();
			while (length != 0) {
				currentByte = (byte)fileR.read();
				while (currentByte != ' ') {
					currentByte = (byte)fileR.read();
				}
				if (length == 1)
					fileLength = dataR.readLong();
				else
					currentByte = (byte)fileR.read();
				length -= 1;
			}

			currentByte = (byte)fileR.read();
			currentByte = (byte)fileR.read();
			
			for (int i = 0; i < 8; i += 1) {
				if ((byte)((currentByte >> i) & 0x1) == 0)
					list.add(false);
				else 
					list.add(true);
			}
			byte[] buffer = new byte[10];
			int n = fileR.read(buffer);
			while (n != -1) {
				for (int k = 0; k < n; k += 1) {
					for (int i = 0; i < 8; i += 1) {
						if ((byte)((buffer[k] >> i) & 0x1) == 0)
							list.add(false);
						else 
							list.add(true);
					}
				}
				buffer = new byte[10];
				n = fileR.read(buffer);
			}
			fileR.close();

			FileOutputStream fileW = new FileOutputStream(outputFile);
			Node node = tree;
			for (int i = 0; i < list.size() && fileLength > 0; i += 1) {
				if (node.right != null && node.left != null) {
					if (list.get(i))
						node = node.right;
					else 
						node = node.left;
				}
				else {
					fileW.write(node.chunk);
					fileLength -= 1;
					node = list.get(i) ? tree.right : tree.left;
				}
			}
			fileW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
