package forbes.ruzzler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Dictionary {

	private final String PATH = "/resources/sowpods.txt";

	private String[] words;

	private HashMap<Character, Integer> jumpMap; // Ex: "A"-> (first index of a
												// word starting with B)

	public Dictionary() {
		loadWords();
	}

	private void loadWords() {
		words = new String[267752];
		jumpMap = new HashMap<Character, Integer>();
		try {
			Scanner file = new Scanner(new File(System.getProperty("user.dir")
					+ PATH));
			int index = 0;
			char lastFirstLetter = 'A';
			while (file.hasNext()) {
				String word = file.next();
				words[index] = word.toUpperCase();

				if (!(words[index].charAt(0)==lastFirstLetter)) {
					jumpMap.put(lastFirstLetter, index);
					lastFirstLetter = words[index].charAt(0);
				}
				index++;
			}
			jumpMap.put(lastFirstLetter, words.length);
			
			System.out.println(index + " words loaded");
			file.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public boolean checkWord(String word) {
		if (Arrays.binarySearch(words, word) < 0)
			return false;
		return true;
	}

	public String[] getAllWords() {
		return words;
	}

	public String[] getAllWordsWith(ArrayList<Character> letters) {
		Set<Character> letterSet = new HashSet<Character>(letters);

		ArrayList<String> output = new ArrayList<String>();
		for (int i = 0; i < words.length; i++) {
			String currentWord = words[i];
			boolean valid = true;
			for (int j = 0; j < currentWord.length(); j++) {
				boolean letterFound = false;
				for (Object e: letterSet.toArray()) {
					if (e.equals(currentWord.charAt(j))) {
						letterFound = true;
						break;
					}
				}
				if (!letterFound) {
					valid = false;
					if (j==0) {
						i = jumpMap.get(currentWord.charAt(0));
					}
					break;
				}
			}
			if (valid) {
				output.add(currentWord);
			}
		}

		String[] arrayOutput = new String[output.size()];
		output.toArray(arrayOutput);

		return arrayOutput;
	}
}
