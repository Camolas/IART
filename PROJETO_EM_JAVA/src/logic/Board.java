package logic;

import java.awt.Point;
import java.util.Arrays;
import java.util.Stack;

public class Board {

	public byte[][] board;
	private int boardSize;

	public Board(int boardSize) {

		board = new byte[boardSize][boardSize];
		this.boardSize = boardSize;

		initializeBoard();

	}

	public void printBoard() {
		for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
			for (int columnIndex = 0; columnIndex < board.length; columnIndex++)
				System.out.print((char) getPiece(columnIndex, rowIndex) + "|");
			System.out.println();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		// just for testing
		Board board = new Board(12);

		board.printBoard();

		byte[][] board2 = {
				{ Game.empty, Game.blackpiece, Game.blackpiece, Game.empty, Game.empty, Game.empty, Game.whitepiece,
						Game.empty, Game.empty, Game.empty, Game.empty, Game.blackpiece },
				{ Game.empty, Game.blackpiece, Game.empty, Game.whitepiece, Game.empty, Game.empty, Game.empty,
						Game.empty, Game.blackpiece, Game.empty, Game.empty, Game.empty },
				{ Game.whitepiece, Game.blackpiece, Game.blackpiece, Game.empty, Game.empty, Game.blackpiece,
						Game.empty, Game.empty, Game.empty, Game.empty, Game.whitepiece, Game.empty },
				{ Game.empty, Game.empty, Game.blackpiece, Game.empty, Game.empty, Game.empty, Game.empty,
						Game.whitepiece, Game.empty, Game.empty, Game.empty, Game.empty },
				{ Game.empty, Game.empty, Game.empty, Game.empty, Game.whitepiece, Game.empty, Game.empty, Game.empty,
						Game.empty, Game.blackpiece, Game.empty, Game.empty },
				{ Game.empty, Game.whitepiece, Game.empty, Game.empty, Game.empty, Game.empty, Game.blackpiece,
						Game.empty, Game.empty, Game.empty, Game.empty, Game.whitepiece },
				{ Game.empty, Game.empty, Game.empty, Game.blackpiece, Game.empty, Game.empty, Game.empty, Game.empty,
						Game.whitepiece, Game.empty, Game.empty, Game.empty },
				{ Game.blackpiece, Game.empty, Game.empty, Game.empty, Game.empty, Game.whitepiece, Game.empty,
						Game.empty, Game.empty, Game.empty, Game.blackpiece, Game.empty },
				{ Game.empty, Game.empty, Game.whitepiece, Game.empty, Game.empty, Game.empty, Game.empty,
						Game.blackpiece, Game.empty, Game.empty, Game.empty, Game.empty },
				{ Game.empty, Game.empty, Game.empty, Game.empty, Game.blackpiece, Game.empty, Game.empty, Game.empty,
						Game.empty, Game.whitepiece, Game.empty, Game.empty },
				{ Game.empty, Game.blackpiece, Game.empty, Game.empty, Game.empty, Game.empty, Game.whitepiece,
						Game.empty, Game.empty, Game.empty, Game.empty, Game.blackpiece },
				{ Game.empty, Game.empty, Game.empty, Game.whitepiece, Game.empty, Game.empty, Game.empty, Game.empty,
						Game.blackpiece, Game.empty, Game.empty, Game.empty } };

		// byte[][] board2 = {
		// { Game.empty, Game.blackpiece, Game.empty, Game.empty, Game.empty,
		// Game.empty, Game.whitepiece,
		// Game.empty, Game.empty, Game.empty, Game.empty, Game.blackpiece },
		// { Game.empty, Game.empty, Game.empty, Game.whitepiece, Game.empty,
		// Game.empty, Game.empty, Game.empty,
		// Game.blackpiece, Game.empty, Game.empty, Game.empty },
		// { Game.whitepiece, Game.empty, Game.empty, Game.empty, Game.empty,
		// Game.blackpiece, Game.empty,
		// Game.empty, Game.empty, Game.empty, Game.whitepiece, Game.empty },
		// { Game.empty, Game.empty, Game.blackpiece, Game.empty, Game.empty,
		// Game.empty, Game.empty,
		// Game.whitepiece, Game.empty, Game.empty, Game.empty, Game.empty },
		// { Game.empty, Game.empty, Game.empty, Game.empty, Game.whitepiece,
		// Game.empty, Game.empty, Game.empty,
		// Game.empty, Game.blackpiece, Game.empty, Game.empty },
		// { Game.empty, Game.whitepiece, Game.empty, Game.empty, Game.empty,
		// Game.empty, Game.blackpiece,
		// Game.empty, Game.empty, Game.empty, Game.empty, Game.whitepiece },
		// { Game.empty, Game.empty, Game.empty, Game.blackpiece, Game.empty,
		// Game.empty, Game.empty, Game.empty,
		// Game.whitepiece, Game.empty, Game.empty, Game.empty },
		// { Game.blackpiece, Game.empty, Game.empty, Game.empty, Game.empty,
		// Game.whitepiece, Game.empty,
		// Game.empty, Game.empty, Game.empty, Game.blackpiece, Game.empty },
		// { Game.empty, Game.empty, Game.whitepiece, Game.empty, Game.empty,
		// Game.empty, Game.empty,
		// Game.blackpiece, Game.empty, Game.empty, Game.empty, Game.empty },
		// { Game.empty, Game.empty, Game.empty, Game.empty, Game.blackpiece,
		// Game.empty, Game.empty, Game.empty,
		// Game.empty, Game.whitepiece, Game.empty, Game.empty },
		// { Game.empty, Game.blackpiece, Game.empty, Game.empty, Game.empty,
		// Game.empty, Game.whitepiece,
		// Game.empty, Game.empty, Game.empty, Game.empty, Game.blackpiece },
		// { Game.empty, Game.empty, Game.empty, Game.whitepiece, Game.empty,
		// Game.empty, Game.empty, Game.empty,
		// Game.blackpiece, Game.empty, Game.empty, Game.empty } };

		board.board = board2;

		// Board board = new Board(12);
		board.printBoard();

		System.out.println("longest chain = " + board.getLongestChain(new Point(0, 0), Game.blackpiece));

		// board.setPiece(1, 2, Game.blackpiece);
		// board.printBoard();

		// for (int y = 0; y < board.getBoardSize(); y++)
		// for (int x = 0; x < board.getBoardSize(); x++) {
		// board.setPiece(x, y, Game.blackpiece);
		// board.printBoard();
		// }

	}

	public byte getPiece(int coordX, int coordY) {
		return board[coordY][coordX];
	}

	public void setPiece(int coordX, int coordY, byte piece) {
		board[coordY][coordX] = piece;
	}

	public byte getPiece(Point coords) {
		return board[coords.y][coords.x];
	}

	public void setPiece(Point coords, byte piece) {
		board[coords.y][coords.x] = piece;
	}

	private void initializeBoardWithEmpty() {
		for (byte[] row : board)
			Arrays.fill(row, Game.empty);
	}

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

	private void initializeRow(int rowIndex, int position, byte piece) {

		byte pieceToPlace = piece;
		for (int columnIndex = position; columnIndex < board.length; columnIndex = columnIndex + 5) {
			setPiece(columnIndex, rowIndex, pieceToPlace);
			pieceToPlace = getOppositePiece(pieceToPlace);
		}

	}

	public static byte getOppositePiece(byte piece) {
		if (piece == Game.whitepiece)
			return Game.blackpiece;
		else
			return Game.whitepiece;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public int getLongestChain(Point coords1, Point coords2, byte peca) {
		
		return Math.max(getLongestChain(coords1, peca), getLongestChain(coords2, peca));
	}

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

	public boolean isEmptySpace(Point coords) {
		if (getPiece(coords) == Game.empty)
			return true;
		else
			return false;
	}

	public boolean isEmptySpace(int coordX, int coordY) {
		if (getPiece(coordX, coordY) == Game.empty)
			return true;
		else
			return false;
	}

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

	public boolean gameOver() {
		// TODO Auto-generated method stub
		return false;
	}

	public int evaluate(byte maximizingPiece) {
		// TODO Auto-generated method stub
		return 0;
	}

}
