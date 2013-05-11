package forbes.ruzzler;

import java.util.HashMap;

public class LetterValues {
	public static HashMap<Character, Integer> letterValues = new HashMap<Character, Integer>();
	
	public static void init() {
		letterValues.put('A', 1);
		letterValues.put('B', 4);
		letterValues.put('C', 4);
		letterValues.put('D', 2);
		letterValues.put('E', 1);
		letterValues.put('F', 4);
		letterValues.put('G', 3);
		letterValues.put('H', 4);
		letterValues.put('I', 1);
		letterValues.put('J', 10);
		letterValues.put('K', 5);
		letterValues.put('L', 1);
		letterValues.put('M', 3);
		letterValues.put('N', 1);
		letterValues.put('O', 1);
		letterValues.put('P', 4);
		letterValues.put('Q', 10);
		letterValues.put('R', 1);
		letterValues.put('S', 1);
		letterValues.put('T', 1);
		letterValues.put('U', 2);
		letterValues.put('V', 4);
		letterValues.put('W', 4);
		letterValues.put('X', 8);
		letterValues.put('Y', 4);
		letterValues.put('Z', 10);
	}
	
	public static int getValue(Character letter){
		return letterValues.get(letter);
	}
}
