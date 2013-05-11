package forbes.ruzzler;

import java.awt.Color;

import forbes.ruzzler.gui.ImageGenerator;

public enum Modifier {
	NONE(Color.BLACK, "None"), DOUBLE_LETTER(ImageGenerator.GREEN, "Double Letter"), TRIPLE_LETTER(
			Color.BLUE, "Triple Letter"), DOUBLE_WORD(Color.YELLOW,
			"Double Word"), TRIPLE_WORD(Color.RED, "Triple Word");

	public Color color;
	public String name;

	private Modifier(Color color, String name) {
		this.color = color;
		this.name = name;
	}

	public String toString() {
		return name;
	}
}