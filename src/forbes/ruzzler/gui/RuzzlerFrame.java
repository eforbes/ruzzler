package forbes.ruzzler.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.AbstractTableModel;

import forbes.ruzzler.Dictionary;
import forbes.ruzzler.Modifier;
import forbes.ruzzler.RuzzleBoard;
import forbes.ruzzler.RuzzleBoardReverse;
import forbes.ruzzler.RuzzleNode;
import forbes.ruzzler.LetterValues;
import forbes.ruzzler.RuzzleWord;

public class RuzzlerFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JTextField textField_Input;
	private JScrollPane scrollPane_Table;
	private JComboBox<String> comboBox_Round;
	private JScrollPane scrollPane_Images;
	private JPanel panel_Board;
	private JButton[] btn_Board;
	private JLabel lbl_ModifierType;
	private JButton btn_Reset;
	private JLabel lbl_ClickToPlace;
	private JLabel lbl_Solving;
	private JLabel lbl_Status;

	private static final String[] roundOptions = { "Round 1", "Round 2",
			"Round 3" };

	private static final Modifier[][] modifierSequences = {
			{ Modifier.DOUBLE_LETTER, Modifier.TRIPLE_LETTER },
			{ Modifier.DOUBLE_LETTER, Modifier.DOUBLE_LETTER,
					Modifier.TRIPLE_LETTER, Modifier.DOUBLE_WORD },
			{ Modifier.DOUBLE_LETTER, Modifier.TRIPLE_LETTER,
					Modifier.TRIPLE_LETTER, Modifier.DOUBLE_WORD,
					Modifier.DOUBLE_WORD, Modifier.TRIPLE_WORD } };
	private int modifierStage;
	private int round;
	private boolean modifierMode;

	private Modifier[] modifiers;

	private RuzzleBoard board;
	private JTable table;

	private Dictionary dict;
	private ImageGenerator imageGenerator;

	private ArrayList<RuzzleWord> words;

	private Object[][] defaultTableData = {{"","",""}};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RuzzlerFrame frame = new RuzzlerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RuzzlerFrame() {
		LetterValues.init();

		setTitle("Ruzzler v2 By Evan Forbes");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 677);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		scrollPane_Images = new JScrollPane();
		scrollPane_Images
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane_Images
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_Images.setBounds(10, 400, 774, 218);

		contentPane.add(scrollPane_Images);

		JLabel lblRuzzler = new JLabel("Ruzzler");
		lblRuzzler.setFont(new Font("Tahoma", Font.PLAIN, 48));
		lblRuzzler.setBounds(10, 11, 157, 65);
		contentPane.add(lblRuzzler);

		comboBox_Round = new JComboBox<String>();
		comboBox_Round.setModel(new DefaultComboBoxModel<String>(roundOptions));
		comboBox_Round.setBounds(177, 37, 133, 20);
		contentPane.add(comboBox_Round);

		panel_Board = new JPanel();
		panel_Board.setBounds(10, 89, 300, 300);
		contentPane.add(panel_Board);
		panel_Board.setLayout(new GridLayout(4, 4, 4, 4));

		btn_Board = new JButton[16];
		for (int i = 0; i < btn_Board.length; i++) {
			btn_Board[i] = new JButton();
			btn_Board[i].addActionListener(new BoardClickListener(i));
			btn_Board[i].setFont(new Font("Tahoma", Font.BOLD, 32));
			panel_Board.add(btn_Board[i]);
		}

		scrollPane_Table = new JScrollPane();
		scrollPane_Table
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_Table
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane_Table.setBounds(568, 11, 216, 378);
		contentPane.add(scrollPane_Table);

		table = new JTable(new RuzzleTableModel(defaultTableData));
		scrollPane_Table.setViewportView(table);

		textField_Input = new JTextField();
		textField_Input.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				updateBoardLetters();
			}
		});
		textField_Input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textField_Input.getText().length() == 16) {
					chooseModifiers();
				}
			}
		});
		textField_Input.setBounds(320, 89, 238, 20);
		contentPane.add(textField_Input);
		textField_Input.setColumns(16);

		lbl_ClickToPlace = new JLabel("Click to Place Modifier:");
		lbl_ClickToPlace.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lbl_ClickToPlace.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_ClickToPlace.setBounds(320, 201, 238, 14);
		lbl_ClickToPlace.setVisible(false);
		contentPane.add(lbl_ClickToPlace);

		lbl_ModifierType = new JLabel("Double Letter");
		lbl_ModifierType.setForeground(Color.BLUE);
		lbl_ModifierType.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lbl_ModifierType.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_ModifierType.setBounds(320, 238, 238, 29);
		lbl_ModifierType.setVisible(false);
		contentPane.add(lbl_ModifierType);

		btn_Reset = new JButton("Reset");
		btn_Reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
			}
		});
		btn_Reset.setBounds(469, 36, 89, 23);
		contentPane.add(btn_Reset);

		lbl_Solving = new JLabel("Solving...");
		lbl_Solving.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lbl_Solving.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_Solving.setBounds(320, 346, 238, 43);
		lbl_Solving.setVisible(false);
		contentPane.add(lbl_Solving);

		lbl_Status = new JLabel("");
		lbl_Status.setForeground(Color.GRAY);
		lbl_Status.setHorizontalAlignment(SwingConstants.TRAILING);
		lbl_Status.setBounds(10, 629, 774, 14);
		contentPane.add(lbl_Status);

		clearModifiers();

		dict = new Dictionary();

	}

	private void clearModifiers() {
		modifiers = new Modifier[16];
		for (int i = 0; i < modifiers.length; i++) {
			modifiers[i] = Modifier.NONE;
		}

	}

	private void chooseModifier(int i) {

		if (modifiers[i] == Modifier.NONE) {
			modifiers[i] = modifierSequences[round][modifierStage];
			modifierStage++;
			btn_Board[i].setForeground(modifiers[i].color);
		}
		if (modifierStage >= modifierSequences[round].length) {
			modifierMode = false;

			lbl_Solving.setVisible(true);
			lbl_ClickToPlace.setVisible(false);
			lbl_ModifierType.setVisible(false);

			EventQueue.invokeLater(new Runnable() {
				public void run() {
					solve();
				}
			});

		} else {
			updateModifierSelection();
		}
	}

	private void solve() {

		ArrayList<RuzzleNode> nodes = new ArrayList<RuzzleNode>();
		String letters = textField_Input.getText().toUpperCase();
		for (int i = 0; i < 16; i++) {
			nodes.add(new RuzzleNode(letters.charAt(i), i / 4, i % 4,
					modifiers[i]));
		}

		board = new RuzzleBoardReverse(nodes, dict);

		long beforeTime = System.nanoTime();

		words = board.getWords();

		long afterTime = System.nanoTime();

		lbl_Solving.setVisible(false);

		Object[][] data = new Object[words.size()][3];

		for (int i = 0; i < data.length; i++) {
			data[i][0] = words.get(i).getWord();
			data[i][1] = words.get(i).getScore();
			data[i][2] = words.get(i).getLength();
		}

		table.setModel(new RuzzleTableModel(data));

		double totalTime = ((double) ((afterTime - beforeTime) / 10000000L)) / 100.0;
		lbl_Status.setText("Found " + words.size() + " words in " + totalTime
				+ " seconds and generated images in ");

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				setUpImages();
			}
		});

	}

	private void setUpImages() {
		long beforeTime = System.nanoTime();
		imageGenerator = new ImageGenerator(board.board);
		scrollPane_Images.setViewportView(imageGenerator.getImagePanel(words));
		long afterTime = System.nanoTime();
		double totalTime = ((double) ((afterTime - beforeTime) / 10000000L)) / 100.0;
		lbl_Status.setText(lbl_Status.getText() + totalTime + " seconds.");
	}

	private void reset() {
		comboBox_Round.setEnabled(true);
		comboBox_Round.setSelectedIndex(0);

		textField_Input.setEnabled(true);
		textField_Input.setText("");

		lbl_ClickToPlace.setVisible(false);
		lbl_ModifierType.setVisible(false);

		lbl_Solving.setVisible(false);

		table.setModel(new RuzzleTableModel(defaultTableData));

		scrollPane_Images.setViewportView(null);

		clearModifiers();

		lbl_Status.setText("");

		for (int i = 0; i < btn_Board.length; i++) {
			btn_Board[i].setForeground(Color.BLACK);
		}

		modifierMode = false;
	}

	private void chooseModifiers() {
		textField_Input.setEnabled(false);
		comboBox_Round.setEnabled(false);
		lbl_ClickToPlace.setVisible(true);
		lbl_ModifierType.setVisible(true);
		round = comboBox_Round.getSelectedIndex();
		modifierStage = 0;
		modifierMode = true;

		updateModifierSelection();

	}

	private void updateModifierSelection() {
		Modifier current = modifierSequences[round][modifierStage];
		lbl_ModifierType.setText(current.toString());
		lbl_ModifierType.setForeground(current.color);
	}

	private void updateBoardLetters() {
		String letters = textField_Input.getText();
		int count = 16;
		int i = 0;
		if (letters.length() <= count) {
			for (; i < letters.length(); i++) {
				btn_Board[i].setText((letters.charAt(i) + "").toUpperCase());
			}
			for (; i < count; i++) {
				btn_Board[i].setText("");
			}
		} else {
			// Weird, but needed
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					textField_Input.setText(textField_Input.getText()
							.substring(0, 16));
				}
			});
		}

	}

	class BoardClickListener implements ActionListener {
		private int i;

		public BoardClickListener(int i) {
			this.i = i;
		}

		public void actionPerformed(ActionEvent e) {
			if (modifierMode) {
				chooseModifier(i);
			}

		}
	}
}

class RuzzleTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private String[] columnNames = { "Word", "Points", "Length" };
	private Object[][] data;

	public RuzzleTableModel(Object[][] data) {
		this.data = data;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.length;
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

}
