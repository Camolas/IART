package logic;

import java.awt.Point;
import java.util.Arrays;
import java.util.Stack;

/**
 * The Class Board.
 */
public class Board {

	/** The board. */
	public byte[][] board;

	/** The board size. */
	private int boardSize;

	/**
	 * Instantiates a new board.
	 *
	 * @param boardSize
	 *            the board size
	 */
	public Board(int boardSize) {

		board = new byte[boardSize][boardSize];
		this.boardSize = boardSize;

		initializeBoard();

	}

	/**
	 * Prints the board.
	 */
	public void printBoard() {
		for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
			for (int columnIndex = 0; columnIndex < board.length; columnIndex++)
				System.out.print((char) getPiece(columnIndex, rowIndex) + "|");
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * Gets a piece.
	 *
	 * @param coordX
	 *            the coord X
	 * @param coordY
	 *            the coord Y
	 * @return the piece
	 */
	public byte getPiece(int coordX, int coordY) {
		return board[coordY][coordX];
	}

	/**
	 * Sets a piece.
	 *
	 * @param coordX
	 *            the coord X
	 * @param coordY
	 *            the coord Y
	 * @param piece
	 *            the piece
	 */
	public void setPiece(int coordX, int coordY, byte piece) {
		board[coordY][coordX] = piece;
	}

	/**
	 * Gets a piece.
	 *
	 * @param coords
	 *            the coords
	 * @return the piece
	 */
	public byte getPiece(Point coords) {
		return board[coords.y][coords.x];
	}

	/**
	 * Sets a piece.
	 *
	 * @param coords
	 *            the coords
	 * @param piece
	 *            the piece
	 */
	public void setPiece(Point coords, byte piece) {
		board[coords.y][coords.x] = piece;
	}

	/**
	 * Initialize board with empty spaces.
	 */
	private void initializeBoardWithEmpty() {
		for (byte[] row : board)
			Arrays.fill(row, Game.empty);
	}

	/**
	 * Initialize board.
	 */
	private void initializeBoard() {
		initializeBoardWithEmpty();

		byte[] pieces = { Game.blackpiece, Game.whitepiece, Game.whitepiece, Game.blackpiece, Game.whitepiece };
		int[] positions = { 1, 3, 0, 2, 4 };

		int index = 0;
		for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
			initializeRow(rowIndex, positions[index], pieces[index]);
			pieces[index] = getOppositePiece(pieces[index]);

			index++;
			if (index == 5)
				index = 0;

		}
	}

	/**
	 * Initialize row.
	 *
	 * @param rowIndex
	 *            the row index
	 * @param position
	 *            the position
	 * @param piece
	 *            the piece
	 */
	private void initializeRow(int rowIndex, int position, byte piece) {

		byte pieceToPlace = piece;
		for (int columnIndex = position; columnIndex < board.length; columnIndex = columnIndex + 5) {
			setPiece(columnIndex, rowIndex, pieceToPlace);
			pieceToPlace = getOppositePiece(pieceToPlace);
		}

	}

	/**
	 * Gets the opposite piece.
	 *
	 * @param piece
	 *            the piece
	 * @return the opposite piece
	 */
	public static byte getOppositePiece(byte piece) {
		if (piece == Game.whitepiece)
			return Game.blackpiece;
		else
			return Game.whitepiece;
	}

	/**
	 * Gets the board size.
	 *
	 * @return the board size
	 */
	public int getBoardSize() {
		return boardSize;
	}

	/**
	 * Gets the longest chain.
	 *
	 * @param coords1
	 *            the first piece coords
	 * @param coords2
	 *            the second piece coords
	 * @param peca
	 *            the piece
	 * @return the longest chain
	 */
	public int getLongestChain(Point coords1, Point coords2, byte peca) {

		return Math.max(getLongestChain(coords1, peca), getLongestChain(coords2, peca));
	}

	/**
	 * Gets the longest chain.
	 *
	 * @param coords
	 *            the coords
	 * @param peca
	 *            the piece
	 * @return the longest chain
	 */
	public int getLongestChain(Point coords, byte peca) {

		boolean[][] matrixSteppedOn = new boolean[boardSize][boardSize];

		for (boolean[] row : matrixSteppedOn)
			Arrays.fill(row, false);

		Stack<Point> explore = new Stack<Point>();

		explore.push(coords);

		int hMax = -1;
		int hMin = Game.boardsize + 1;

		int vMax = -1;
		int vMin = Game.boardsize + 1;

		while (explore.size() != 0) {

			Point current = explore.pop();

			int x = (int) current.getX();
			int y = (int) current.getY();

			matrixSteppedOn[y][x] = true;

			if (x > hMax)
				hMax = x;

			if (x < hMin)
				hMin = x;

			if (y > vMax)
				vMax = y;

			if (y < vMin)
				vMin = y;

			int newY, newX;

			if (y > 0) {
				newY = y - 1;
				newX = x;
				if (!matrixSteppedOn[newY][newX] && getPiece(newX, newY) == peca) {
					explore.push(new Point(newX, newY));
				}

			}

			if (x > 0) {

				newY = y;
				newX = x - 1;
				if (!matrixSteppedOn[newY][newX] && getPiece(newX, newY) == peca) {
					explore.push(new Point(newX, newY));
				}

			}

			if (y < (Game.boardsize - 1)) {

				newY = y + 1;
				newX = x;
				if (!matrixSteppedOn[newY][newX] && getPiece(newX, newY) == peca) {
					explore.push(new Point(newX, newY));
				}

			}

			if (x < (Game.boardsize - 1)) {

				newY = y;
				newX = x + 1;
				if (!matrixSteppedOn[newY][newX] && getPiece(newX, newY) == peca) {
					explore.push(new Point(newX, newY));
				}

			}
		}

		int hdis = (hMax - hMin) + 1;
		int vdis = (vMax - vMin) + 1;

		return (Game.blackpiece == peca) ? hdis : vdis;
	}

	/**
	 * Checks if it is an empty space.
	 *
	 * @param coords
	 *            the coords
	 * @return true, if is empty space
	 */
	public boolean isEmptySpace(Point coords) {
		if (getPiece(coords) == Game.empty)
			return true;
		else
			return false;
	}

	/**
	 * Checks if it is an empty space.
	 *
	 * @param coordX
	 *            the coord X
	 * @param coordY
	 *            the coord Y
	 * @return true, if is empty space
	 */
	public boolean isEmptySpace(int coordX, int coordY) {
		if (getPiece(coordX, coordY) == Game.empty)
			return true;
		else
			return false;
	}

	/**
	 * Check if it is a valid play.
	 *
	 * @param coords
	 *            the coords
	 * @param peca
	 *            the piece
	 * @return true, if successful
	 */
	public boolean validPlay(Point coords, byte peca) {
		int[] auxs = { -1, 1 };

		for (int i = 0; i < 2; i++) {

			int newX = coords.x + auxs[i];
			if (newX < 0 || newX >= Game.boardsize)
				continue;

			for (int e = 0; e < 2; e++) {

				int newY = coords.y + auxs[e];
				if (newY < 0 || newY >= Game.boardsize)
					continue;

				if (getPiece(newX, newY) == peca) {
					if (getPiece(coords.x, newY) != peca && getPiece(newX, coords.y) != peca) {
						return false;
					}
				}
			}
		}
		return true;

	}

	/**
	 * Check if it is a valid play.
	 *
	 * @param coords1
	 *            the first piece coords
	 * @param coords2
	 *            the second piece coords
	 * @param peca
	 *            the piece
	 * @return true, if successful
	 */
	public boolean validPlay(Point coords1, Point coords2, byte peca) {

		if (!isEmptySpace(coords1) || !isEmptySpace(coords2)) {
			return false;
		}
		byte oldPiece1 = this.getPiece(coords1);
		byte oldPiece2 = this.getPiece(coords2);

		setPiece(coords1, peca);
		setPiece(coords2, peca);

		boolean isValidPlay;

		if (validPlay(coords1, peca) && validPlay(coords2, peca))
			isValidPlay = true;
		else
			isValidPlay = false;

		setPiece(coords1, oldPiece1);
		setPiece(coords2, oldPiece2);

		return isValidPlay;
	}

}
