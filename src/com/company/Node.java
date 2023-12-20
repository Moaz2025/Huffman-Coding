package com.company;

import java.lang.Comparable;

public class Node implements Comparable<Node> {
	public byte character;
	public int weight;
	public Node rightNode;
	public Node leftNode;

	public Node(byte character, int weight, Node rightNode, Node leftNode) {
		this.character = character;
		this.weight = weight;
		this.rightNode = rightNode;
		this.leftNode = leftNode;
	}

	public int compareTo(Node node) {
		return Integer.compare(this.weight, node.weight);
	}

	public String toString() {
		String string = "";
		string += "character = " + (char)this.character + "\nweight = " + this.weight + "\n";
		if (this.rightNode != null)
			string += this.rightNode.toString();
		if (this.leftNode != null)
			string += this.leftNode.toString();
		return string;
	}
}