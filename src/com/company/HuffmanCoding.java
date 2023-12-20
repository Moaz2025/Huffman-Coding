package com.company;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class HuffmanCoding {

	private static List<Node> calculateFrequency(File file, int chunkSize) {
		int[] frequencyArray = new int[256 * chunkSize];

		try (BufferedInputStream bufferedFileR = new BufferedInputStream(new FileInputStream(file))) {
			byte[] buffer = new byte[4096];
			int bytesRead;
			while ((bytesRead = bufferedFileR.read(buffer)) != -1) {
				for (int i = 0; i < bytesRead; i++) {
					frequencyArray[Byte.toUnsignedInt(buffer[i])]++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<Node> nodeList = new ArrayList<>();
		for (int i = 0; i < frequencyArray.length; i++) {
			if (frequencyArray[i] > 0) {
				nodeList.add(new Node((byte) i, frequencyArray[i], null, null));
			}
		}

		nodeList.sort(Comparator.comparingInt(node -> node.weight));

		return nodeList;
	}

	private static Node createNode(File file, int chunkSize) {
		List<Node> nodeList = calculateFrequency(file, chunkSize);

		while (nodeList.size() >= 2) {
			Node rightNode = nodeList.remove(0);
			Node leftNode = nodeList.remove(0);
			Node node = new Node((byte) 0, rightNode.weight + leftNode.weight, rightNode, leftNode);
			nodeList.add(node);

			// Re-sort the list after adding a new node
			nodeList.sort(Comparator.comparingInt(n -> n.weight));
		}

		return nodeList.get(0);
	}

	private static HashMap<Byte, boolean[]> getCoding(Node tree) {
		HashMap<Byte, boolean[]> hashMap = new HashMap<>();
		HuffmanCoding.getCodingRec(tree.rightNode, hashMap, new boolean[0], true);
		HuffmanCoding.getCodingRec(tree.leftNode, hashMap, new boolean[0], false);
		return hashMap;
	}

	private static void getCodingRec(Node tree, HashMap<Byte, boolean[]> hashMap, boolean[] tab, boolean bool) {
		boolean[] tmp = new boolean[tab.length+1];
		System.arraycopy(tab, 0, tmp, 0, tab.length);
		tmp[tab.length] = bool;
		if (tree.leftNode == null && tree.rightNode == null)
			hashMap.put(tree.character, tmp);
		else {
			HuffmanCoding.getCodingRec(tree.rightNode, hashMap, tmp, true);
			HuffmanCoding.getCodingRec(tree.leftNode, hashMap, tmp, false);
		}
	}

	private static void write(File file, HashMap<Byte, boolean[]> hashMap) {
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

	private static HashMap<Byte, boolean[]> read(File file) {
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

	public static void encode(String inputFile, int chunkSize) {
		File file = new File(inputFile);
		Path inputFilePath = Paths.get(inputFile);
		String inputFileName = inputFilePath.getFileName().toString();
		int lastDotIndex = inputFileName.lastIndexOf('.');
		String fileExtension = inputFileName.substring(lastDotIndex + 1);
		String outputFileName = "20011969." + inputFileName.substring(0, lastDotIndex) + "." + fileExtension +  ".hc";
		File outputFile = new File(outputFileName);
		Node node = HuffmanCoding.createNode(file, chunkSize);

		HashMap<Byte, boolean[]> hashMap = HuffmanCoding.getCoding(node);
		HuffmanCoding.write(outputFile, hashMap);
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

	private static void getTreeRec(byte character, boolean[] bool, Node node) {
		for (int i = 0; i < bool.length-1; i += 1) {
			if (bool[i]) {
				if (node.rightNode == null)
					node.rightNode = new Node((byte)0, 0, null, null);
				node = node.rightNode;
			}
			else {
				if (node.leftNode == null)
					node.leftNode = new Node((byte)0, 0, null, null);
				node = node.leftNode;
			}
		}
		if (bool[bool.length-1])
			node.rightNode = new Node(character, 0, null, null);
		else 
			node.leftNode = new Node(character, 0, null, null);
	}

	private static Node getTree(HashMap<Byte, boolean[]> hashMap) {
		Node node = new Node((byte)0, 0, null, null);
		for (Map.Entry<Byte, boolean[]> entry : hashMap.entrySet()) {
			HuffmanCoding.getTreeRec(entry.getKey(), entry.getValue(), node);
		}
		return node;
	}

	public static void decode(String inputFile) {
		File file = new File(inputFile);
		Path inputFilePath = Paths.get(inputFile);
		String inputFileName = inputFilePath.getFileName().toString();
		int lastDotIndex = inputFileName.lastIndexOf('.');
		String outputFileName = "extracted." + inputFileName.substring(0, lastDotIndex);
		File outputFile = new File(outputFileName);

		HashMap<Byte, boolean[]> hashMap = read(file);
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
				if (node.rightNode != null && node.leftNode != null) {
					if (list.get(i))
						node = node.rightNode;
					else
						node = node.leftNode;
				}
				else {
					fileW.write(node.character);
					fileLength -= 1;
					node = list.get(i) ? tree.rightNode : tree.leftNode;
				}
			}
			fileW.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
