package logic;

import java.util.Arrays;

public class Board {

	byte[][] board;

	public Board(int boardSize) {

		board = new byte[boardSize][boardSize];

		for (int rowIndex = 0; rowIndex < boardSize; rowIndex++)
			for (int columnIndex = 0; columnIndex < boardSize; columnIndex++)
				setPiece(columnIndex, rowIndex, Game.empty);

		initializeBoard();

	}

	public void printBoard() {
		for (int rowIndex = 0; rowIndex < board.length; rowIndex++) {
			for (int columnIndex = 0; columnIndex < board.length; columnIndex++)
				System.out.print((char) getPiece(columnIndex, rowIndex) + " ");
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Board board = new Board(12);
		board.printBoard();

	}

	public byte getPiece(int coordX, int coordY) {
		return board[coordY][coordX];
	}

	public void setPiece(int coordX, int coordY, byte piece) {
		board[coordY][coordX] = piece;
	}

	private void initializeBoard() {
		byte[] pieces = { Game.whitepiece, Game.blackpiece, Game.blackpiece, Game.whitepiece, Game.blackpiece };
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


}
