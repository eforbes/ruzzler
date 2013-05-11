package forbes.ruzzler;

import java.util.ArrayList;

public class RuzzleWord implements Comparable<RuzzleWord> {
	public ArrayList<RuzzleNode> nodes;

	public RuzzleWord(ArrayList<RuzzleNode> nodes) {
		this.nodes = nodes;
	}

	public RuzzleWord(RuzzleWord copy) {
		this.nodes = new ArrayList<RuzzleNode>();
		for (int i = 0; i < copy.nodes.size(); i++) {
			this.nodes.add(new RuzzleNode(copy.nodes.get(i)));
		}
	}

	public String getWord() {
		StringBuffer buffer = new StringBuffer();
		for (RuzzleNode node : nodes) {
			buffer.append(node.getLetter());
		}
		return buffer.toString();
	}

	public String toString() {
		return getWord();
	}

	public int getScore() {
		int value = 0;

		int multiplier = 1;
		for (RuzzleNode node : nodes) {
			value += node.getValue();
			if (node.getModifier().equals(Modifier.DOUBLE_WORD)) {
				multiplier *= 2;
			} else if (node.getModifier().equals(Modifier.TRIPLE_WORD)) {
				multiplier *= 3;
			}
		}

		value *= multiplier;

		int lengthModifier = (getLength() - 4) * 5;
		if (lengthModifier > 0) {
			value += lengthModifier;
		}

		return value;
	}

	public int getLength() {
		return nodes.size();
	}

	@Override
	public int compareTo(RuzzleWord o) {
		if (o.getScore() > this.getScore())
			return 1;
		if (o.getScore() < this.getScore())
			return -1;
		return 0;
	}

	public int hasNode(RuzzleNode node) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).equals(node)) {
				return i;
			}
		}
		return -1;
	}

}
