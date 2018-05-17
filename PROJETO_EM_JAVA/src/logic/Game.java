package logic;

import java.awt.Point;
import java.util.Random;
import java.util.Stack;

public class Game {

	public static final byte empty = ' ';
	
	public static final byte blackpiece = 'X';
	
	public static final byte whitepiece = '0';
	
	public static final int boardsize = 8;
	
	public final static int depth = 3;

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
		
		board.setPiece(minimax.lastPlayPiece1, piece);
		board.setPiece(minimax.lastPlayPiece2, piece);
		
		board.printBoard();
		
		return new Point[]{minimax.lastPlayPiece1, minimax.lastPlayPiece2};
	}
	
	public boolean checkEndGame(Point point1, Point point2, byte piece){

		
		System.out.println(board.getLongestChain(point1, point2, piece));
		return (boardsize == board.getLongestChain(point1, point2, piece));
	}

}
