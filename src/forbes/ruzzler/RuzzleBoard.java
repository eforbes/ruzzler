package forbes.ruzzler;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * This class represents the RuzzleBoard and solves it using a straightforward
 * iterative algorithm.
 * 
 * @author Evan Forbes
 * 
 */
public class RuzzleBoard {

	public static final int BOARD_SIZE = 4;

	public ArrayList<RuzzleNode> board;
	public Dictionary dictionary;

	public int checkCounter = 0;

	public RuzzleBoard(ArrayList<RuzzleNode> board, Dictionary dictionary) {
		this.board = board;
		this.dictionary = dictionary;
	}

	public ArrayList<RuzzleWord> getWords() {
		ArrayList<RuzzleWord> words = new ArrayList<RuzzleWord>();

		// do this for all 16 possible starting letters
		for (int firstNodeNum = 0; firstNodeNum < board.size(); firstNodeNum++) {
			ArrayList<RuzzleNode> firstNode = new ArrayList<RuzzleNode>();
			firstNode.add(board.get(firstNodeNum));

			ArrayList<RuzzleWord> allWords = findWordsStartingWith(firstNode);
			for (RuzzleWord currentWord : allWords) {
				String currentString = currentWord.getWord();
				int iMax = words.size();
				boolean found = false;
				int comparingScore = 0;
				int matchIndex = 0;
				for (int i = 0; i < iMax; i++) {
					if (words.get(i).getWord().equals(currentString)) {
						found = true;
						comparingScore = words.get(i).getScore();
						matchIndex = i;
						break;
					}

				}
				if (!found) {
					words.add(currentWord);
					iMax++;
				} else if (comparingScore < currentWord.getScore()) {
					words.remove(matchIndex);
					words.add(currentWord);
				}
			}
		}

		Collections.sort(words);

		System.out.println("found this many: " + words.size() + " / "
				+ checkCounter);

		return words;
	}

	private ArrayList<RuzzleWord> findWordsStartingWith(
			ArrayList<RuzzleNode> soFar) {
		ArrayList<RuzzleWord> words = new ArrayList<RuzzleWord>();

		RuzzleWord currentWord = new RuzzleWord(soFar);
		checkCounter++;

		if (dictionary.checkWord(currentWord.getWord())) {
			if (currentWord.getLength() > 1) {
				words.add(new RuzzleWord(currentWord));
			}
		}

		RuzzleNode lastNode = soFar.get(soFar.size() - 1);

		ArrayList<RuzzleNode> possibleNext = getAdjacentNodes(lastNode);

		// remove duplicates
		int iMax = possibleNext.size();
		for (int i = 0; i < iMax; i++) {
			for (int j = 0; j < soFar.size(); j++) {
				if (possibleNext.get(i).equals(soFar.get(j))) {
					possibleNext.remove(i);
					iMax--;
					i--;
					break;
				}
			}
		}

		if (possibleNext.size() != 0) {
			for (RuzzleNode node : possibleNext) {
				soFar.add(node);
				words.addAll(findWordsStartingWith(soFar));
				soFar.remove(soFar.size() - 1);
			}
		}

		return words;
	}

	protected ArrayList<RuzzleNode> getAdjacentNodes(RuzzleNode node) {
		ArrayList<RuzzleNode> adjacentNodes = new ArrayList<RuzzleNode>();
		int mainRow = node.getRow();
		int mainCol = node.getColumn();
		for (int a = -1; a <= 1; a++) {
			int curRow = mainRow + a;
			if (curRow >= 0 && curRow < BOARD_SIZE) {
				for (int b = -1; b <= 1; b++) {
					int curCol = mainCol + b;
					if (curCol >= 0 && curCol < BOARD_SIZE) {
						if (curCol != 0 || curRow != 0) {
							adjacentNodes.add(getNodeByRowCol(curRow, curCol));
						}
					}
				}
			}
		}
		return adjacentNodes;
	}

	protected RuzzleNode getNodeByRowCol(int row, int col) {
		for (RuzzleNode node : board) {
			if (node.getRow() == row && node.getColumn() == col) {
				return node;
			}
		}
		System.out.println("invalid row col vals");
		return null;
	}

}
