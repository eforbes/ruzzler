package forbes.ruzzler;

import java.util.ArrayList;
import java.util.Collections;

/*
 * TESTING ONLY
 */
public class RuzzlerTester {

	public static void main(String[] args) {
		LetterValues.init();

		Dictionary dict = new Dictionary();

		ArrayList<RuzzleNode> nodes = new ArrayList<RuzzleNode>();
		
		String letters = "hasedaswtoettril".toUpperCase();
		
		nodes.add(new RuzzleNode(letters.charAt(0), 0, 0, Modifier.NONE));
		nodes.add(new RuzzleNode(letters.charAt(1), 0, 1, Modifier.NONE));
		nodes.add(new RuzzleNode(letters.charAt(2), 0, 2, Modifier.TRIPLE_LETTER));
		nodes.add(new RuzzleNode(letters.charAt(3), 0, 3, Modifier.NONE));

		nodes.add(new RuzzleNode(letters.charAt(4), 1, 0, Modifier.DOUBLE_LETTER));
		nodes.add(new RuzzleNode(letters.charAt(5), 1, 1, Modifier.NONE));
		nodes.add(new RuzzleNode(letters.charAt(6), 1, 2, Modifier.NONE));
		nodes.add(new RuzzleNode(letters.charAt(7), 1, 3, Modifier.NONE));

		nodes.add(new RuzzleNode(letters.charAt(8), 2, 0, Modifier.NONE));
		nodes.add(new RuzzleNode(letters.charAt(9), 2, 1, Modifier.NONE));
		nodes.add(new RuzzleNode(letters.charAt(10), 2, 2, Modifier.NONE));
		nodes.add(new RuzzleNode(letters.charAt(11), 2, 3, Modifier.NONE));

		nodes.add(new RuzzleNode(letters.charAt(12), 3, 0, Modifier.NONE));
		nodes.add(new RuzzleNode(letters.charAt(13), 3, 1, Modifier.NONE));
		nodes.add(new RuzzleNode(letters.charAt(14), 3, 2, Modifier.NONE));
		nodes.add(new RuzzleNode(letters.charAt(15), 3, 3, Modifier.NONE));

		RuzzleBoard board = new RuzzleBoard(nodes, dict);

		System.out.println("Words:");
		ArrayList<RuzzleWord> words = board.getWords();
		Collections.sort(words);
		for (RuzzleWord e: words){
			System.out.println(e.getScore()+" "+ e.toString());
		}

	}

}
