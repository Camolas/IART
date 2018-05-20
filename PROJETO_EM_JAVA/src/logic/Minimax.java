package logic;

import java.awt.Point;

/**
 * The Class Minimax.
 */
public class Minimax {

	/** The Constant MAX_INT. */
	private static final int MAX_INT = Integer.MAX_VALUE;

	/** The board. */
	private Board board = null;

	/** The maximizing piece. */
	private byte maximizingPiece;

	/** The minimizing piece. */
	private byte minimizingPiece;

	/** The first piece the algorithm choose. */
	public Point lastPlayPiece1 = null;

	/** The first piece the algorithm choose. */
	public Point lastPlayPiece2 = null;

	/**
	 * Instantiates a new minimax.
	 *
	 * @param board
	 *            the board
	 * @param maximizingPiece
	 *            the maximizing piece
	 */
	public Minimax(Board board, byte maximizingPiece) {
		this.board = board;
		this.maximizingPiece = maximizingPiece;

		if (this.maximizingPiece == Game.blackpiece)
			minimizingPiece = Game.whitepiece;
		else
			minimizingPiece = Game.blackpiece;

	}

	/**
	 * Apply alpha beta minimax algorithm auxiliary function.
	 *
	 * @return the node
	 */
	public Node applyAlphaBeta() {
		return alphabeta(board, Game.depth, -MAX_INT, MAX_INT, true, 1, 1);

	}

	/**
	 * Apply alpha beta minimax algorithm.
	 *
	 * @param board
	 *            the board
	 * @param depth
	 *            the depth
	 * @param alfa
	 *            the alfa
	 * @param beta
	 *            the beta
	 * @param maximizingPlayer
	 *            the maximizing player
	 * @param biggestChainMax
	 *            the maximizer biggest chain
	 * @param biggestChainMin
	 *            the minimizer biggest chain
	 * @return the node
	 */
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

	/**
	 * Evaluate Node.
	 *
	 * @param board
	 *            the board
	 * @param biggestChainMax
	 *            the maximizer biggest chain
	 * @param biggestChainMin
	 *            the minimizer biggest chain
	 * @return the value of the node
	 */
	private int evaluate(Board board, int biggestChainMax, int biggestChainMin) {
		if (biggestChainMax == board.getBoardSize())
			return biggestChainMax * 10000;

		if (biggestChainMin == board.getBoardSize())
			return -biggestChainMin * 10000;

		return biggestChainMax * 10000 + (biggestChainMax - biggestChainMin + board.getBoardSize()) * 100
				+ (-biggestChainMin + board.getBoardSize());
		// return biggestChainMax;

	}

	/**
	 * Minimize.
	 *
	 * @param board
	 *            the board
	 * @param depth
	 *            the depth
	 * @param alfa
	 *            the alfa
	 * @param beta
	 *            the beta
	 * @param biggestChainMax
	 *            the maximizer biggest chain
	 * @param biggestChainMin
	 *            the minimizer biggest chain
	 * @return the node
	 */
	private Node minimize(Board board, int depth, int alfa, int beta, int biggestChainMax, int biggestChainMin) {
		Node node = new Node(+MAX_INT, biggestChainMax, biggestChainMin);

		searchChilds: for (int x1 = 0; x1 < Game.boardsize; x1++)
			for (int y1 = 0; y1 < Game.boardsize; y1++) {
				if (!board.isEmptySpace(x1, y1))
					continue;

				innerloop: for (int x2 = 0; x2 < Game.boardsize; x2++)
					for (int y2 = y1 + 1; y2 < Game.boardsize; y2++) {

						if (x1 == x2 && y2 < y1)
							continue;

						if (x2 < x1)
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

	/**
	 * Maximize.
	 *
	 * @param board
	 *            the board
	 * @param depth
	 *            the depth
	 * @param alfa
	 *            the alfa
	 * @param beta
	 *            the beta
	 * @param biggestChainMax
	 *            the maximizer biggest chain
	 * @param biggestChainMin
	 *            the minimizer biggest chain
	 * @return the node
	 */
	private Node maximize(Board board, int depth, int alfa, int beta, int biggestChainMax, int biggestChainMin) {
		Node node = new Node(-MAX_INT, biggestChainMax, biggestChainMin);
		// Point[] lastPlayAux = new Point[2];

		searchChilds: for (int x1 = 0; x1 < Game.boardsize; x1++)
			for (int y1 = 0; y1 < Game.boardsize; y1++) {
				if (!board.isEmptySpace(x1, y1))
					continue;

				innerloop: for (int x2 = 0; x2 < Game.boardsize; x2++)
					for (int y2 = 0; y2 < Game.boardsize; y2++) {

						if (x1 == x2 && y2 < y1)
							continue;

						if (x2 < x1)
							continue innerloop;

						if (!board.validPlay(new Point(x1, y1), new Point(x2, y2), maximizingPiece))
							continue;

						board.setPiece(x1, y1, maximizingPiece);
						board.setPiece(x2, y2, maximizingPiece);

						node.biggestChainMax = Math.max(biggestChainMax,
								board.getLongestChain(new Point(x1, y1), new Point(x2, y2), maximizingPiece));

						// Temporary but perfect
						if (depth == Game.depth && board.getLongestChain(new Point(x1, y1), new Point(x2, y2),
								maximizingPiece) == board.getBoardSize()) {
							lastPlayPiece1 = new Point(x1, y1);
							lastPlayPiece2 = new Point(x2, y2);
							return node;
						}

						int currValNode = alphabeta(board, depth - 1, alfa, beta, false, node.biggestChainMax,
								biggestChainMin).value;

						if (currValNode > node.value) {
							node.value = currValNode;
							if (depth == Game.depth) {
								lastPlayPiece1 = new Point(x1, y1);
								lastPlayPiece2 = new Point(x2, y2);

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

		// if(depth == 3)
		// board.printBoard();

		return node;

	}

}
