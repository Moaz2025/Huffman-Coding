package com.company;

import java.lang.Comparable;

public class Node implements Comparable<Node> {
	public byte chunk;
	public int weight;
	public Node right;
	public Node left;

	public Node(byte chunk, int weight, Node rightNode, Node leftNode) {
		this.chunk = chunk;
		this.weight = weight;
		this.right = rightNode;
		this.left = leftNode;
	}
	
	public int compareTo(Node node) {
		return Integer.compare(this.weight, node.weight);
	}

	public String toString() {
		String string = "";
		string += "character = " + (char)this.chunk + "\nweight = " + this.weight + "\n";
		if (this.right != null)
			string += this.right.toString();
		if (this.left != null)
			string += this.left.toString();
		return string;
	}
}