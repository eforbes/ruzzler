package forbes.ruzzler;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class overrides the getWords() method from the normal RuzzleBoard class and
 * makes it roughly 10x more efficient by iterating through the dictionary and
 * trying to find the words on the board, rather that recursing through the
 * board and checking the dictionary.
 * 
 * @author Evan Forbes
 * 
 */
public class RuzzleBoardReverse extends RuzzleBoard {

	ArrayList<Character> letters;

	public RuzzleBoardReverse(ArrayList<RuzzleNode> board, Dictionary dict) {
		super(board, dict);
		letters = new ArrayList<Character>();

		for (int i = 0; i < board.size(); i++) {
			letters.add(board.get(i).getLetter());
		}

	}

	@Override
	public ArrayList<RuzzleWord> getWords() {
		String[] dictionaryWords = dictionary.getAllWordsWith(letters);

		ArrayList<RuzzleWord> foundWords = new ArrayList<RuzzleWord>();

		for (int i = 0; i < dictionaryWords.length; i++) {
			String currentWord = dictionaryWords[i];
			RuzzleWord currentRuzzleWord = findWordOnBoard(currentWord);
			if (currentRuzzleWord != null) {
				foundWords.add(currentRuzzleWord);
			}
		}

		//Sort by most points first
		Collections.sort(foundWords);

		System.out.println("found this many: " + foundWords.size() + " / "
				+ dictionaryWords.length);

		return foundWords;
	}

	private RuzzleWord findWordOnBoard(String word) {
		ArrayList<RuzzleWord> possible = new ArrayList<RuzzleWord>();

		for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++) {
			RuzzleNode currentNode = board.get(i);
			if (currentNode.getLetter()==word.charAt(0)) {
				ArrayList<RuzzleNode> soFar = new ArrayList<RuzzleNode>();
				soFar.add(currentNode);
				possible.addAll(findWordOnBoardStartingWith(soFar, word));
			}
		}

		int maxIndex = -1;
		int maxScore = 0;
		for (int i = 0; i < possible.size(); i++) {
			int curScore = possible.get(i).getScore();
			if (curScore > maxScore) {
				maxIndex = i;
				maxScore = curScore;
			}
		}

		if (maxIndex >= 0) {
			return possible.get(maxIndex);
		} else
			return null;
	}

	private ArrayList<RuzzleWord> findWordOnBoardStartingWith(
			ArrayList<RuzzleNode> soFar, String word) {

		ArrayList<RuzzleWord> found = new ArrayList<RuzzleWord>();

		String wordSoFar = "";
		for (RuzzleNode e : soFar) {
			wordSoFar += e.getLetter();
		}
		if (wordSoFar.equals(word)) {
			@SuppressWarnings("unchecked")
			ArrayList<RuzzleNode> soFarCopy = (ArrayList<RuzzleNode>) soFar
					.clone();
			found.add(new RuzzleWord(soFarCopy));
		} else if (wordSoFar.length() < word.length()) {
			// check around the last node soFar for the next letter

			RuzzleNode lastNode = soFar.get(soFar.size() - 1);

			ArrayList<RuzzleNode> adjacent = getAdjacentNodes(lastNode);

			// remove from adjacents that are already in the word
			int iMax = adjacent.size();
			for (int i = 0; i < iMax; i++) {
				RuzzleNode currentAdjacent = adjacent.get(i);
				for (int j = 0; j < soFar.size(); j++) {
					if (currentAdjacent.equals(soFar.get(j))) {
						adjacent.remove(i);
						iMax--;
						i--;
						break;
					}
				}
			}

			int lettersSoFar = soFar.size();

			char letterToLookFor = word.charAt(lettersSoFar);

			for (int i = 0; i < adjacent.size(); i++) {
				RuzzleNode currentAdjacent = adjacent.get(i);
				if (currentAdjacent.getLetter()==letterToLookFor) {
					soFar.add(currentAdjacent);
					found.addAll(findWordOnBoardStartingWith(soFar, word));
					soFar.remove(currentAdjacent);
				}
			}
		}
		return found;
	}
}
