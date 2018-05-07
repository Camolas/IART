package logic;

import java.util.Random;

public class Game {

	public static final byte empty = ' ';
	
	public static final byte blackpiece = 'X';
	
	public static final byte whitepiece = '0';
	
	public static final int boardsize = 12;
	
	public final static int depth = 3;

	public static void main(String[] args) {
		//Minimax minimax = new Minimax();
		
		byte[][] board = null;
		
		board = generateBoard();
		
		Integer[] list1 = {1,2};
		Integer[] list2 = {1,2};
		
		System.out.println(list1.equals(list2));
		
		//minimax.applyMinimax(depth, board);
	}

	private static byte[][] generateBoard() {
		byte[][] ret1 = {{empty,blackpiece,empty,empty,empty,empty,whitepiece,empty,empty,empty,empty,blackpiece}
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
		

		byte[][] ret2 = {{empty,blackpiece,empty,empty,empty,empty,whitepiece,empty,empty,empty,empty,blackpiece}
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

		
		Random rand = new Random(System.currentTimeMillis());
		
		return ((rand.nextInt() % 2)==0)?ret1:ret2;
	}
}
