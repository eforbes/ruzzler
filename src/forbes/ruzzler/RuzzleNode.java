package forbes.ruzzler;


public class RuzzleNode {
	public int row;
	public int column;
	public char letter;
	public Modifier modifier;
 
	public RuzzleNode(char letter, int row, int column, Modifier modifier) {
		this.row = row;
		this.column = column;
		this.letter = letter;
		this.modifier = modifier;
	}

	public RuzzleNode(RuzzleNode copy) {
		this.row = copy.row;
		this.column = copy.column;
		this.letter = copy.letter;
		this.modifier = copy.modifier;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public char getLetter() {
		return letter;
	}

	public Modifier getModifier() {
		return modifier;
	}

	public int getValue() {
		int multiplier = 1;
		if (modifier == Modifier.DOUBLE_LETTER) {
			multiplier = 2;
		} else if (modifier == Modifier.TRIPLE_LETTER) {
			multiplier = 3;
		}
		return LetterValues.getValue(letter) * multiplier;
	}

	public boolean equals(RuzzleNode other) {
		if (this.row == other.row && this.column == other.column)
			return true;
		return false;
	}

	public String toString() {
		return letter+"";
	}

}
