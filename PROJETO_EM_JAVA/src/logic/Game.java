package logic;

import java.awt.Point;
import java.util.Random;
import java.util.Stack;

public class Game {

	public static final byte empty = ' ';
	
	public static final byte blackpiece = 'X';
	
	public static final byte whitepiece = '0';
	
	public static int boardsize = 8;
	
	public static int depth = 3;

	private Minimax2 minimax = null;
	
	private Board board = null;
	
	public Game(){
		resetBoard();
	}
	
	public Board getBoard(){
		return board;
	}
	
	public void resetBoard(){
		board = new Board(Game.boardsize);
	}

	public Point[] getPlay(byte piece) {
		minimax = new Minimax2(board, piece);
		minimax.applyAlphaBeta();

		setPieces(piece, minimax.lastPlayPiece1, minimax.lastPlayPiece2);
		
		return new Point[]{minimax.lastPlayPiece1, minimax.lastPlayPiece2};
	}
	
	public void setPieces(byte piece, Point point1, Point point2){
		board.setPiece(point1, piece);
		board.setPiece(point2, piece);
	}
	
	public boolean checkEndGame(Point point1, Point point2, byte piece){
		return (boardsize == board.getLongestChain(point1, point2, piece));
	}

}
