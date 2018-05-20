package logic;

import java.awt.Point;

/**
 * The Class Game.
 */
public class Game {

	/** The Constant empty space. */
	public static final byte empty = ' ';

	/** The Constant black piece. */
	public static final byte blackpiece = 'X';

	/** The Constant white piece. */
	public static final byte whitepiece = '0';

	/** The size of the board. */
	public static int boardsize = 8;

	/** The depth of minimax tree. */
	public static int depth = 2;

	/** The minimax. */
	private Minimax minimax = null;

	/** The board. */
	private Board board = null;

	/**
	 * Instantiates a new game.
	 */
	public Game() {
		resetBoard();
	}

	/**
	 * Gets the board.
	 *
	 * @return the board
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * Reset board.
	 */
	public void resetBoard() {
		board = new Board(Game.boardsize);
	}

	/**
	 * Gets the play of minimax algorithm.
	 *
	 * @param piece
	 *            the piece
	 * @return the play
	 */
	public Point[] getPlay(byte piece) {
		minimax = new Minimax(board, piece);
		minimax.applyAlphaBeta();

		setPieces(piece, minimax.lastPlayPiece1, minimax.lastPlayPiece2);

		return new Point[] { minimax.lastPlayPiece1, minimax.lastPlayPiece2 };
	}

	/**
	 * Sets the pieces.
	 *
	 * @param piece
	 *            the piece
	 * @param point1
	 *            the coords of the first piece
	 * @param point2
	 *            the coords of the second piece
	 */
	public void setPieces(byte piece, Point point1, Point point2) {
		board.setPiece(point1, piece);
		board.setPiece(point2, piece);
	}

	/**
	 * Check if the game is over.
	 *
	 * @param point1
	 *            the coords of the first piece
	 * @param point2
	 *            the coords of the second piece
	 * @param piece
	 *            the piece
	 * @return true, if successful
	 */
	public boolean checkEndGame(Point point1, Point point2, byte piece) {
		return (boardsize == board.getLongestChain(point1, point2, piece));
	}

}
