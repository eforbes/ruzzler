package forbes.ruzzler.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import forbes.ruzzler.RuzzleNode;
import forbes.ruzzler.RuzzleWord;

public class ImageGenerator {

	private ArrayList<RuzzleNode> board;

	private final static boolean OVER_SCALE = false;
	private final static int OVER_SCALE_AMOUNT = 1;

	private final static int SIZE = 200 * OVER_SCALE_AMOUNT;
	private final static int OVAL_SIZE = 42 * OVER_SCALE_AMOUNT;
	private final static int OVAL_SPACING = 6 * OVER_SCALE_AMOUNT;

	public static final Color GREEN = new Color(34, 139, 34);

	public ImageGenerator(ArrayList<RuzzleNode> board) {
		this.board = board;
	}

	public JPanel getImagePanel(ArrayList<RuzzleWord> words) {
		JPanel panel = new JPanel();

		int numImages = words.size();

		panel.setLayout(new GridLayout(1, numImages, 10, 0));

		for (int i = 0; i < numImages; i++) {
			RuzzleWord currentWord = words.get(i);

			BufferedImage image = new BufferedImage(SIZE, SIZE,
					BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.getGraphics();

			g.setColor(Color.DARK_GRAY);
			g.drawRect(0, 0, SIZE - 1, SIZE - 1);
			g.drawRect(1, 1, SIZE - 3, SIZE - 3);
			g.setColor(Color.BLUE);
			g.fillRect(2, 2, SIZE - 4, SIZE - 4);

			g.setColor(Color.WHITE);

			for (int j = 0; j < (currentWord.nodes.size() - 1); j++) {
				RuzzleNode fromNode = currentWord.nodes.get(j);
				RuzzleNode toNode = currentWord.nodes.get(j + 1);

				int fromX = fromNode.column * (OVAL_SIZE + OVAL_SPACING)
						+ (OVAL_SIZE / 2) + OVAL_SPACING;
				int fromY = fromNode.row * (OVAL_SIZE + OVAL_SPACING)
						+ (OVAL_SIZE / 2) + OVAL_SPACING;

				int toX = toNode.column * (OVAL_SIZE + OVAL_SPACING)
						+ (OVAL_SIZE / 2) + OVAL_SPACING;
				int toY = toNode.row * (OVAL_SIZE + OVAL_SPACING)
						+ (OVAL_SIZE / 2) + OVAL_SPACING;

				drawThickLine(g, fromX, fromY, toX, toY);
			}

			for (int j = 0; j < board.size(); j++) {
				RuzzleNode currentNode = board.get(j);

				String labelNumber = "";

				if (currentNode.equals(currentWord.nodes.get(0))) {
					g.setColor(GREEN);
					labelNumber = "1";
				} else if (currentNode.equals(currentWord.nodes
						.get(currentWord.nodes.size() - 1))) { // last node
					g.setColor(Color.RED);
					labelNumber = "" + (currentWord.nodes.size());
				} else if (currentWord.hasNode(currentNode) > 0) {
					g.setColor(Color.YELLOW);
					labelNumber = "" + (currentWord.hasNode(currentNode) + 1);
				} else {
					g.setColor(Color.LIGHT_GRAY);
				}

				int row = currentNode.getRow();
				int col = currentNode.getColumn();

				g.fillOval(OVAL_SPACING * (col + 1) + OVAL_SIZE * (col),
						OVAL_SPACING * (row + 1) + OVAL_SIZE * (row),
						OVAL_SIZE, OVAL_SIZE);

				g.setColor(Color.BLACK);
				g.setFont(new Font("Tahoma", Font.BOLD, 24 * OVER_SCALE_AMOUNT));
				if (labelNumber.length() > 1) {
					g.drawString(labelNumber, OVAL_SPACING * (col + 1)
							+ OVAL_SIZE * (col) + (OVAL_SIZE / 3)
							- (OVAL_SPACING / 2), OVAL_SPACING * (row + 1)
							+ OVAL_SIZE * (row + (1)) - (OVAL_SPACING * 2));
				} else {
					g.drawString(labelNumber, OVAL_SPACING * (col + 1)
							+ OVAL_SIZE * (col) + (OVAL_SIZE / 3), OVAL_SPACING
							* (row + 1) + OVAL_SIZE * (row + (1))
							- (OVAL_SPACING * 2));
				}
			}

			ImageIcon imageIcon;
			if (OVER_SCALE) {
				imageIcon = new ImageIcon(image.getScaledInstance(SIZE
						/ OVER_SCALE_AMOUNT, SIZE / OVER_SCALE_AMOUNT,
						Image.SCALE_DEFAULT));
			} else {
				imageIcon = new ImageIcon(image);
			}

			JLabel label = new JLabel(imageIcon);
			label.addMouseListener(new ImageLabelAdapter(label, panel));
			label.setToolTipText(currentWord.getWord());
			panel.add(label);
		}

		return panel;
	}

	private void drawThickLine(Graphics g, int fromX, int fromY, int toX,
			int toY) {
		int lineWidth;
		if (fromX != toX && fromY != toY) {
			lineWidth = 5 * OVER_SCALE_AMOUNT; // if diagonal
		} else {
			lineWidth = 4 * OVER_SCALE_AMOUNT; // if not diagonal
		}
		for (int i = -lineWidth; i <= lineWidth; i++) {
			g.drawLine(fromX, fromY + i, toX, toY + i);
			g.drawLine(fromX + i, fromY, toX + i, toY);
		}
	}

	class ImageLabelAdapter extends MouseAdapter {
		private JLabel label;
		private JPanel panel;

		public ImageLabelAdapter(JLabel label, JPanel panel) {
			this.label = label;
			this.panel = panel;
		}

		public void mouseClicked(MouseEvent e) {
			panel.remove(label);
			panel.updateUI();
		}
	}
}
