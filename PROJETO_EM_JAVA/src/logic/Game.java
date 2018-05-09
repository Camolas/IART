package logic;

import java.util.Random;
import java.util.Stack;

public class Game {

	public static final byte empty = ' ';
	
	public static final byte blackpiece = 'X';
	
	public static final byte whitepiece = '0';
	
	public static final int boardsize = 12;
	
	public final static int depth = 1;

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
		
		Stack<Integer[]> playsRet = new Stack<Integer[]> ();
		
		Byte[][] boardAux = new Byte[boardsize][]; 
		
		int i = 0;
		for(Byte[] row: board){
			boardAux[i++] = row.clone();
		}
		i = 0;
		
		int value = 0;

		Stack<Integer> valueS = new Stack<Integer>();
		
		while(Math.abs(value)!=boardsize){
			Byte playPiece = ((i%2)==0)?blackpiece:whitepiece;
			i++;
			
			Stack<Integer[]> plays = this.minimax.applyMinimax(depth, boardAux, valueS);

			value = valueS.pop();
			
			Integer[] firstPlay = plays.elementAt(0);
			boardAux[firstPlay[0]][firstPlay[1]] = playPiece;
			boardAux[firstPlay[2]][firstPlay[3]] = playPiece;
			
			playsRet.push(firstPlay);
			
			for(int e = 0; e < boardsize; e++){
				for(int j = 0; j < boardsize; j++){
					System.out.print((char)(byte)boardAux[e][j] + "|");
				}	
				System.out.println();
			}

			System.out.println();
		}
		
		return playsRet; 
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
		return ret1;
		//return ((rand.nextInt() % 2)==0)?ret1:ret2;
	}
}
