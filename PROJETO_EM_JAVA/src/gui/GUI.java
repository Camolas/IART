package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import logic.Board;
import logic.Game;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.BorderLayout;

/**
 * The Class GUI.
 */
public class GUI {

	/** The previous board size. */
	private static int prevBoardSize;

	/** The table model. */
	private static DefaultTableModel tabelModel = null;

	/** The table. */
	private static JTable tabel = null;

	/** The board frame. */
	private static JFrame boardFrame = null;

	/** The board panel. */
	private static JPanel boardPanel = null;

	/** The game. */
	private static Game game = null;

	/** The board. */
	private static String[][] board = null;

	/** The click count. */
	private static int clickCount = 2;

	/** The play coords. */
	private static Integer[][] playPos = new Integer[2][];

	/** The slider size board. */
	private static JSlider sliderSizeBoard;

	/** The slider depth. */
	private static JSlider sliderDepth;

	/** The column width. */
	private static int COLWIDTH = 35;

	/** The game thread. */
	private static Thread gameThread = null;

	/** The blue piece. */
	private static byte bluePiece = '?';

	/** The rendered. */
	private static DefaultTableCellRenderer rendered = null;

	/**
	 * Gets the renderer.
	 *
	 * @return the renderer
	 */
	private static DefaultTableCellRenderer getRenderer() {
		if (rendered == null) {
			rendered = new DefaultTableCellRenderer() {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					Component tableCellRendererComponent = super.getTableCellRendererComponent(table, value, isSelected,
							hasFocus, row, column);
					if ("X".equals(value)) {
						tableCellRendererComponent.setBackground(Color.WHITE);
						tableCellRendererComponent.setForeground(Color.WHITE);
					} else if ("0".equals(value)) {
						tableCellRendererComponent.setBackground(Color.BLACK);
						tableCellRendererComponent.setForeground(Color.BLACK);
					} else if ("?".equals(value)) {
						tableCellRendererComponent.setBackground(Color.LIGHT_GRAY);
						tableCellRendererComponent.setForeground(Color.LIGHT_GRAY);
					} else {
						tableCellRendererComponent
								.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
					}
					return tableCellRendererComponent;
				}
			};
		}
		return rendered;
	}

	/**
	 * Player vs player.
	 */
	private static void pvp() {
		restartGame();

		int i = 0;
		byte playPiece;
		Point[] play = null;
		do {

			if ((i++ % 2) == 0)
				playPiece = Game.blackpiece;
			else
				playPiece = Game.whitepiece;

			boolean cont;
			do {
				System.out.println("Try a valid play!");

				play = getCoords();
				cont = !game.getBoard().validPlay(play[0], play[1], playPiece);
				if (cont) {
					board[play[0].y][play[0].x] = "";
					board[play[1].y][play[1].x] = "";
					updateTableModel();
				}
			} while (cont);
			System.out.flush();

			game.setPieces(playPiece, play[0], play[1]);

			board[play[0].y][play[0].x] = "" + (char) playPiece;
			board[play[1].y][play[1].x] = "" + (char) playPiece;

			updateTableModel();
		} while (!game.checkEndGame(play[0], play[1], playPiece));
	}

	/**
	 * Player vs AI.
	 */
	private static void pvsai() {
		restartGame();

		int i = 0;
		byte playPiece;
		Point[] play = null;
		do {

			if ((i++ % 2) == 0) {
				playPiece = Game.blackpiece;
				boolean cont;
				do {
					System.out.println("Try a valid play!");

					play = getCoords();
					cont = !game.getBoard().validPlay(play[0], play[1], playPiece);
					if (cont) {
						board[play[0].y][play[0].x] = "";
						board[play[1].y][play[1].x] = "";
						updateTableModel();
					}
				} while (cont);

				game.setPieces(playPiece, play[0], play[1]);
			} else {
				playPiece = Game.whitepiece;
				play = game.getPlay(playPiece);
			}

			final Point[] fplay = play;
			final byte fplayPiece = playPiece;

			board[fplay[0].y][fplay[0].x] = "" + (char) fplayPiece;
			board[fplay[1].y][fplay[1].x] = "" + (char) fplayPiece;

			updateTableModel();
		} while (!game.checkEndGame(play[0], play[1], playPiece));
	}

	/**
	 * Gets the player coords.
	 *
	 * @return the coords
	 */
	private static Point[] getCoords() {
		clickCount = 0;
		while (clickCount != 2) {
			System.out.flush();
		}

		return new Point[] { new Point(playPos[0][0], playPos[0][1]), new Point(playPos[1][0], playPos[1][1]) };
	}

	/**
	 * AI vs AI.
	 */
	private static void aivsai() {
		restartGame();

		int i = 0;
		byte playPiece;
		Point[] play = null;
		do {

			playPiece = ((i++ % 2) == 0) ? Game.blackpiece : Game.whitepiece;

			play = game.getPlay(playPiece);

			board[play[0].y][play[0].x] = "" + (char) playPiece;
			board[play[1].y][play[1].x] = "" + (char) playPiece;

			updateTableModel();
		} while (!game.checkEndGame(play[0], play[1], playPiece));
	}

	/**
	 * Show menu.
	 */
	private static void showMenu() {
		// Create and set up the window.
		JFrame menuFrame = new JFrame("MenuFrame");
		menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuFrame.setSize(514, 510);
		menuFrame.setLocationRelativeTo(null);
		menuFrame.pack();
		menuFrame.setSize(514, 510);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new MigLayout("", "[250px][]", "[100px][100px][100px][][][]"));

		JLabel lblNewLabel = new JLabel("WhirldWind");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 26));
		buttonPanel.add(lblNewLabel, "cell 0 0 2 1,alignx center");
		JButton playervsplayerButton = new JButton("Player vs Player");
		playervsplayerButton.setMinimumSize(new Dimension(250, 100));
		playervsplayerButton.setPreferredSize(new Dimension(250, 100));

		buttonPanel.add(playervsplayerButton, "flowx,cell 0 1 2 1,alignx center,growy");

		playervsplayerButton.setSize(new Dimension(540, 540));

		playervsplayerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cleanBoard();
				menuFrame.setVisible(false);
				boardFrame.setVisible(true);
				gameThread = new Thread(new Runnable() {
					public void run() {
						pvp();
					}
				});
				gameThread.start();
			}
		});

		boardFrame = new JFrame("BoardFrame");
		boardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		boardFrame.setLocationRelativeTo(null);

		boardPanel = new JPanel();
		boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.PAGE_AXIS));
		boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		boardPanel.setBackground(new Color(245, 222, 179));

		String[] columns = new String[Game.boardsize];

		for (int i = 0; i < Game.boardsize; i++) {
			columns[i] = i + "";
		}

		tabelModel = new DefaultTableModel(board, columns);
		tabel = new JTable(tabelModel);
		tabel.setBackground(new Color(222, 184, 135));
		tabel.setRowHeight(COLWIDTH);
		tabel.setRowSelectionAllowed(false);
		tabel.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		JButton backToMenu = new JButton("Back to Menu");
		backToMenu.setSize(new Dimension(250, 100));

		boardPanel.add(tabel);
		boardPanel.add(Box.createRigidArea(new Dimension(0, 25)));
		boardPanel.add(backToMenu);

		tabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();

					if (clickCount < 2) {
						if (!board[row][column].equals("" + (char) Game.empty) && !board[row][column].equals("")) {
							System.out.println("Returned ITS: '" + board[row][column] + "'");
							return;
						}

						System.out.println(column + ":" + row);
						playPos[clickCount++] = new Integer[] { column, row };
						board[row][column] = "" + (char) bluePiece;
						updateTableModel();
					}

				}
			}
		});

		menuFrame.getContentPane().add(buttonPanel, BorderLayout.CENTER);
		JButton playervsaiButton = new JButton("Player vs AI");
		playervsaiButton.setMinimumSize(new Dimension(250, 100));
		playervsaiButton.setPreferredSize(new Dimension(250, 100));
		buttonPanel.add(playervsaiButton, "cell 0 2 2 1,alignx center,growy");

		playervsaiButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cleanBoard();
				menuFrame.setVisible(false);
				boardFrame.setVisible(true);
				gameThread = new Thread(new Runnable() {
					public void run() {
						pvsai();
					}
				});
				gameThread.start();
			}
		});

		JLabel lblBoardSize = new JLabel("Board Size");
		lblBoardSize.setFont(new Font("Tahoma", Font.PLAIN, 16));
		buttonPanel.add(lblBoardSize, "cell 0 4,alignx center");

		sliderSizeBoard = new JSlider();
		sliderSizeBoard.setMaximum(20);
		sliderSizeBoard.setMinimum(4);
		sliderSizeBoard.setPaintTicks(true);
		sliderSizeBoard.setPaintLabels(true);
		sliderSizeBoard.setMajorTickSpacing(2);
		sliderSizeBoard.setMinorTickSpacing(1);
		sliderSizeBoard.setValue(Game.boardsize);
		buttonPanel.add(sliderSizeBoard, "cell 1 4,grow");

		JLabel lblDepth = new JLabel("Depth");
		lblDepth.setFont(new Font("Tahoma", Font.PLAIN, 16));
		buttonPanel.add(lblDepth, "cell 0 5,alignx center");

		sliderDepth = new JSlider();
		sliderDepth.setMaximum(6);
		sliderDepth.setMinimum(1);
		sliderDepth.setPaintTicks(true);
		sliderDepth.setPaintLabels(true);
		sliderDepth.setMajorTickSpacing(1);
		sliderDepth.setMinorTickSpacing(1);
		sliderDepth.setValue(Game.depth);
		buttonPanel.add(sliderDepth, "cell 1 5,grow");

		// Add the ubiquitous "Hello World" label.
		JButton aivsaiButton = new JButton("AI vs AI");

		aivsaiButton.setMinimumSize(new Dimension(250, 100));

		aivsaiButton.setPreferredSize(new Dimension(250, 100));
		buttonPanel.add(aivsaiButton, "cell 0 3 2 1,alignx center,growy");

		aivsaiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cleanBoard();
				menuFrame.setVisible(false);
				boardFrame.setVisible(true);
				gameThread = new Thread(new Runnable() {
					public void run() {
						aivsai();
					}
				});
				gameThread.start();
			}
		});
		boardFrame.getContentPane().add(boardPanel);

		// Display the window.
		menuFrame.setVisible(true);
		boardFrame.setVisible(false);

		backToMenu.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					gameThread.stop(); // Not safe but must be used otherwise we
										// would have to code logic inside
										// minimax algorithm to check if an
										// interrupt was called.
				} catch (Exception ee) {
					System.out.println("Closed game");
				}
				menuFrame.setVisible(true);
				boardFrame.setVisible(false);
			}

		});
	}

	/**
	 * Gets the board frame size.
	 *
	 * @return the board frame size
	 */
	private static int getBoardFrameSize() {
		return COLWIDTH * Game.boardsize;
	}

	/** The columns. */
	private static String[] columns;

	/**
	 * Clean board.
	 */
	private static void cleanBoard() {
		game.resetBoard();
		Board boardOri = game.getBoard();

		if (Game.boardsize != prevBoardSize) {
			prevBoardSize = Game.boardsize;
			createTablePar();
		}

		for (int i = 0; i < Game.boardsize; i++) {
			board[i] = new String[Game.boardsize];
			for (int j = 0; j < Game.boardsize; j++) {
				char ch = (char) (boardOri.getPiece(j, i));
				board[i][j] = "" + ch;
			}
		}
		updateTableModel();
		boardFrame.setSize(getBoardFrameSize(), 600);
		tabel.updateUI();
		tabel.repaint();
	}

	/**
	 * Creates the table par.
	 */
	private static void createTablePar() {

		board = new String[Game.boardsize][Game.boardsize];

		columns = new String[Game.boardsize];

		for (int i = 0; i < Game.boardsize; i++) {
			columns[i] = i + "";
		}
	}

	/**
	 * Update table model.
	 */
	private static void updateTableModel() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				tabelModel.setDataVector(board, columns);

				for (TableColumn cl : Collections.list(tabel.getColumnModel().getColumns())) {
					cl.setPreferredWidth(COLWIDTH);
					cl.setCellRenderer(getRenderer());
				}

				for (int c = 0; c < tabel.getColumnCount(); c++) {
					Class<?> col_class = tabel.getColumnClass(c);
					tabel.setDefaultEditor(col_class, null); // remove editor
				}
			}
		});
	}

	/**
	 * The main method.
	 *
	 */
	public static void main(String[] args) {

		game = new Game();

		prevBoardSize = -1;

		board = new String[Game.boardsize][Game.boardsize];

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showMenu();
			}
		});

	}

	/**
	 * Restart game.
	 */
	public static void restartGame() {
		setGameBoardSize();
		setGameDepth();

		cleanBoard();
	}

	/**
	 * Sets the game board size.
	 */
	public static void setGameBoardSize() {

		Game.boardsize = sliderSizeBoard.getValue();
	}

	/**
	 * Sets the game depth.
	 */
	public static void setGameDepth() {
		Game.depth = sliderDepth.getValue();
	}
}
