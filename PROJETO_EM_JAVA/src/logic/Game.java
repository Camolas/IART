package logic;

import java.util.Random;
import java.util.Stack;

public class Game {

	public static final byte empty = ' ';
	
	public static final byte blackpiece = 'X';
	
	public static final byte whitepiece = '0';
	
	public static final int boardsize = 12;
	
	public final static int depth = 5;

	private Minimax minimax = null;
	
	private Byte[][] board = null;
	
	public Game(){
		minimax = new Minimax();
		
		board = generateBoard();
	}
	
	public Byte[][] getBoard(){
		return board;
	}
 	
	public Stack<Integer[]> startAIvsAI() {
		return this.minimax.applyMinimax(depth, this.board);
	}

	private static Byte[][] generateBoard() {
		Byte[][] ret1 = {{empty,blackpiece,empty,empty,empty,empty,whitepiece,empty,empty,empty,empty,blackpiece}
						,{empty,empty,empty,whitepiece,empty,empty,empty,empty,blackpiece,empty,empty,empty}
						,{whitepiece,empty,empty,empty,empty,blackpiece,empty,empty,empty,empty,whitepiece,empty}
						,{empty,empty,blackpiece,empty,empty,empty,empty,whitepiece,empty,empty,empty,empty}
						,{empty,empty,empty,empty,whitepiece,empty,empty,empty,empty,blackpiece,empty,empty}
						,{empty,whitepiece,empty,empty,empty,empty,blackpiece,empty,empty,empty,empty,whitepiece}
						,{empty,empty,empty,blackpiece,empty,empty,empty,empty,whitepiece,empty,empty,empty}
						,{blackpiece,empty,empty,empty,empty,whitepiece,empty,empty,empty,empty,blackpiece,empty}
						,{empty,empty,whitepiece,empty,empty,empty,empty,blackpiece,empty,empty,empty,empty}
						,{empty,empty,empty,empty,blackpiece,empty,empty,empty,empty,whitepiece,empty,empty}
						,{empty,blackpiece,empty,empty,empty,empty,whitepiece,empty,empty,empty,empty,blackpiece}
						,{empty,empty,empty,whitepiece,empty,empty,empty,empty,blackpiece,empty,empty,empty}};
		

		Byte[][] ret2 = {{empty,whitepiece,empty,empty,empty,empty,blackpiece,empty,empty,empty,empty,whitepiece}
						,{empty,empty,empty,blackpiece,empty,empty,empty,empty,whitepiece,empty,empty,empty}
						,{blackpiece,empty,empty,empty,empty,whitepiece,empty,empty,empty,empty,blackpiece,empty}
						,{empty,empty,whitepiece,empty,empty,empty,empty,blackpiece,empty,empty,empty,empty}
						,{empty,empty,empty,empty,blackpiece,empty,empty,empty,empty,whitepiece,empty,empty}
						,{empty,blackpiece,empty,empty,empty,empty,whitepiece,empty,empty,empty,empty,blackpiece}
						,{empty,empty,empty,whitepiece,empty,empty,empty,empty,blackpiece,empty,empty,empty}
						,{whitepiece,empty,empty,empty,empty,blackpiece,empty,empty,empty,empty,whitepiece,empty}
						,{empty,empty,blackpiece,empty,empty,empty,empty,whitepiece,empty,empty,empty,empty}
						,{empty,empty,empty,empty,whitepiece,empty,empty,empty,empty,blackpiece,empty,empty}
						,{empty,whitepiece,empty,empty,empty,empty,blackpiece,empty,empty,empty,empty,whitepiece}
						,{empty,empty,empty,blackpiece,empty,empty,empty,empty,whitepiece,empty,empty,empty}};

		
		Random rand = new Random(System.currentTimeMillis());
		
		return ((rand.nextInt() % 2)==0)?ret1:ret2;
	}
}
