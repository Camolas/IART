package logic;

import java.awt.Point;
import java.util.Stack;

public class Minimax {

	private static final int MAX_INT = Integer.MAX_VALUE;

	private Board board = null;
	
	private byte maximizingPiece;
	private byte minimizingPiece;

	private int biggestChainMax;
	private int biggestChainMin;
	
	private boolean endgame = false;

	public Point lastPlayPiece1 = null;
	public Point lastPlayPiece2 = null;

	@Deprecated
	public static Stack<Integer[]> applyMinimax() {
		// just for testing
		Board boardTest = new Board(Game.boardsize);
		int biggestChainMaxTest = 1;
		int biggestChainMinTest = 1;

		int i = 0;
		
		byte piece;
		Minimax minMaxTest = null;
		int lgchain = 0;
		while (lgchain != Game.boardsize) {
			
			piece = ((i++ % 2) ==0) ?Game.blackpiece:Game.whitepiece;
			
			minMaxTest = new Minimax(boardTest, piece);
			Node a = minMaxTest.applyAlphaBeta();
			
			boardTest.setPiece(minMaxTest.lastPlayPiece1, piece);
			boardTest.setPiece(minMaxTest.lastPlayPiece2, piece);
			boardTest.printBoard();

			lgchain = boardTest.getLongestChain(minMaxTest.lastPlayPiece1, minMaxTest.lastPlayPiece2, piece);
		}
		return null;
		

	}

	public Minimax(Board board, byte maximizingPiece) {
		this.board = board;
		this.maximizingPiece = maximizingPiece;

		if (this.maximizingPiece == Game.blackpiece)
			minimizingPiece = Game.whitepiece;
		else
			minimizingPiece = Game.blackpiece;

	}

	public Node applyAlphaBeta() {
		return alphabeta(board, Game.depth, -MAX_INT, MAX_INT, true, 1, 1);

	}

	public Node alphabeta(Board board, int depth, int alfa, int beta, boolean maximizingPlayer, int biggestChainMax,
			int biggestChainMin) {
		if ((depth == 0) || (biggestChainMax == board.getBoardSize()) || (biggestChainMin == board.getBoardSize())) {
			// board.printBoard();
			return new Node(evaluate(board, biggestChainMax, biggestChainMin), biggestChainMax, biggestChainMin);
		}

		Node value;
		if (maximizingPlayer) {
			value = maximize(board, depth, alfa, beta, biggestChainMax, biggestChainMin);

		} else {
			value = minimize(board, depth, alfa, beta, biggestChainMax, biggestChainMin);

		}
		
		return value;
	}

	private int evaluate(Board board, int biggestChainMax, int biggestChainMin) {
		if (biggestChainMax == board.getBoardSize())
			return biggestChainMax* 10000;

		if (biggestChainMin == board.getBoardSize())
			return -biggestChainMin* 10000;

		return biggestChainMax * 10000 + (biggestChainMax - biggestChainMin + board.getBoardSize()) * 100
				+ (-biggestChainMin + board.getBoardSize());
		// return biggestChainMax;

	}

	private Node minimize(Board board, int depth, int alfa, int beta, int biggestChainMax, int biggestChainMin) {
		Node node = new Node(+MAX_INT, biggestChainMax, biggestChainMin);

		searchChilds: for (int x1 = 0; x1 < Game.boardsize; x1++)
			for (int y1 = 0; y1 < Game.boardsize; y1++) {
				if (!board.isEmptySpace(x1, y1))
					continue;

				innerloop: for (int x2 = 0; x2 < Game.boardsize; x2++)
					for (int y2 = y1+1; y2 < Game.boardsize; y2++) {

						if(x1 == x2 && y2 < y1)
							continue;
						
						if(x2<x1)
							continue innerloop;

						if (!board.validPlay(new Point(x1, y1), new Point(x2, y2), minimizingPiece))
							continue;

						board.setPiece(x1, y1, minimizingPiece);
						board.setPiece(x2, y2, minimizingPiece);

						node.biggestChainMin = Math.max(biggestChainMin,
								board.getLongestChain(new Point(x1, y1), new Point(x2, y2), minimizingPiece));

						node.value = Math.min(node.value, alphabeta(board, depth - 1, alfa, beta, true, biggestChainMax,
								node.biggestChainMin).value);

						board.setPiece(x1, y1, Game.empty);
						board.setPiece(x2, y2, Game.empty);

						beta = Math.min(beta, node.value);

						if (beta <= alfa)
							break searchChilds;

					}

			}

		return node;

	}

	private Node maximize(Board board, int depth, int alfa, int beta, int biggestChainMax, int biggestChainMin) {
		Node node = new Node(-MAX_INT, biggestChainMax, biggestChainMin);
		// Point[] lastPlayAux = new Point[2];

		searchChilds: for (int x1 = 0; x1 < Game.boardsize; x1++)
			for (int y1 = 0; y1 < Game.boardsize; y1++) {
				if (!board.isEmptySpace(x1, y1))
					continue;

				innerloop: for (int x2 = 0; x2 < Game.boardsize; x2++)
					for (int y2 = 0; y2 < Game.boardsize; y2++) {


						if(x1 == x2 && y2 < y1)
							continue;
						
						if(x2<x1)
							continue innerloop;

						if (!board.validPlay(new Point(x1, y1), new Point(x2, y2), maximizingPiece))
							continue;

						board.setPiece(x1, y1, maximizingPiece);
						board.setPiece(x2, y2, maximizingPiece);

						node.biggestChainMax = Math.max(biggestChainMax,
								board.getLongestChain(new Point(x1, y1), new Point(x2, y2), maximizingPiece));
						
						//Temporary but perfect
						if(depth == Game.depth && board.getLongestChain(new Point(x1, y1), new Point(x2, y2), maximizingPiece) == board.getBoardSize()){
							lastPlayPiece1 = new Point(x1, y1);
							lastPlayPiece2 = new Point(x2, y2);
							this.endgame = true;
							return node;
						}
							
						
					

						int currValNode = alphabeta(board, depth - 1, alfa, beta, false, node.biggestChainMax,
								biggestChainMin).value;
						
						if (currValNode > node.value) {
							node.value = currValNode;
							if(depth == Game.depth){
								lastPlayPiece1 = new Point(x1, y1);
								lastPlayPiece2 = new Point(x2, y2);
								int boardsize = board.getBoardSize();
								if(node.value == boardsize)
									this.endgame = true;
									
							}
						}

						// node.value = Math.max(node.value, alphabeta(board,
						// depth - 1, alfa, beta, false,
						// node.biggestChainMax, biggestChainMin).value);

						board.setPiece(x1, y1, Game.empty);
						board.setPiece(x2, y2, Game.empty);

						alfa = Math.max(alfa, node.value);

						if (beta <= alfa)
							break searchChilds;

					}

			}
		
		//if(depth == 3)
		//board.printBoard();

		return node;

	}

}
