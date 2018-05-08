package logic;

import java.util.ArrayList;
import java.util.Stack;

public class Minimax {
	
	private static final int maxint = Integer.MAX_VALUE;
	
	private static final boolean MINIMIZER = true;
	
	private static final boolean MAXIMIZER = false;

	private void printBoard( byte[][] board) {

		for(int i = 0; i < Game.boardsize; i++){
			for(int j = 0; j < Game.boardsize; j++){
				System.out.print((char)board[i][j]+"|");
			}
			System.out.println();
		}
	}
	
	public void applyMinimax(int depth, byte[][] board){
		int alpha = -maxint;
		int beta = maxint;
		
		int bminplay = 1;
		int bmaxplay = 1;
		
		Stack<Integer[]> listOfPlays = new Stack<Integer[]>();
		
		maximizer(depth, alpha, beta, board, null, bmaxplay, bminplay, listOfPlays);
		
		int i = 0;
		for(Integer[] ie: listOfPlays) {
			System.out.println("X1: "+ ie[0] + " Y1: "+ ie[1]+ " | X2: "+ ie[2] +" Y2: "+ ie[3]);
			i++;
			board[ie[0]][ie[1]] = ((i%2)==0)?Game.blackpiece:Game.whitepiece;
			board[ie[2]][ie[3]] = ((i%2)==0)?Game.blackpiece:Game.whitepiece;
		}
		printBoard(board);
	}
	
	private boolean validPlay(int x, int y, byte[][] board, byte peca){
		
		if(board[x][y] != Game.empty){
			return false;
		}
		
		int[] auxs = {-1,1};
		
		for(int i = 0; i < 2; i++){
			
			int newX = x + auxs[i];
			if(newX<0 || newX>=Game.boardsize) 
				continue;
			
			for(int e = 0; e < 2; e++){
				
				int newY = y + auxs[e];
				if(newY<0 || newY>=Game.boardsize) 
					continue;
				
				if(board[newX][newY] == peca){
					if(board[x][newY] != peca && board[newX][y] != peca){
						return false;
					}
				}
			}	
		}
		return true;
	}
	
	private int iterate(int depth, int alpha, int beta, byte[][] board, int bmaxplay, int bminplay, boolean level, Stack<Integer[]> plays1){
		
		Stack<Integer[]> gPlays = null;
		
		byte peca = (level?Game.blackpiece:Game.whitepiece);
		
		for(int X1 = 0; X1 < Game.boardsize; X1++){

			for(int Y1 = 0; Y1 < Game.boardsize; Y1++){
				
				if(!validPlay(X1, Y1, board, peca) ){
					continue;
				}
				
				for(int X2 = 0; X2 < Game.boardsize; X2++){

					for(int Y2 = 0; (Y2 < Game.boardsize); Y2++){
						
						if(X1 == X2 && Y1 == Y2)
							continue;
						
						
						if(!validPlay(X2, Y2, board, peca) )
							continue;

						board[X1][Y1] = peca;
								
						board[X2][Y2] = peca;
						
						Stack<Integer[]> plays = new Stack<Integer[]>();
						
						Integer coords[] = new Integer[]{X1, Y1, X2, Y2};

						int value = (level ? 
										maximizer(depth - 1, alpha, beta, board, coords, bmaxplay, bminplay, plays): 
										minimizer(depth - 1, alpha, beta, board, coords, bmaxplay, bminplay, plays));
						
						board[X1][Y1] = Game.empty;
											
						board[X2][Y2] = Game.empty;
						
						if(level == MINIMIZER){ 
							if(value < beta){
								gPlays = plays;
								beta = value;  
							}
						}else
							if(level == MAXIMIZER)
								if(value > alpha){
									gPlays = plays;
									alpha = value;  
								}
							
						if(alpha >= beta) {
							
							return ((level==MINIMIZER)?alpha:beta);
						}
						
						//System.out.println("------->");
					}	
				}	
			}	
		}
		
		if(gPlays!=null)
			plays1.addAll(gPlays);
		
		return ((level==MINIMIZER)?beta:alpha);
	}
	
	private int maximizer(int depth,int alpha,int beta, byte[][] board, Integer[] coords ,int bmaxplay,int bminplay, Stack<Integer[]> plays){

		if(coords!=null)
		plays.push(coords.clone());
		//printBoard(board);
		
		if(depth == 0){
			return bmaxplay - bminplay;	
		}
		
		//System.out.println("Max: " + depth);
		//System.out.println("|");
		//System.out.println("|");
		//System.out.println("|");
		//System.out.println("V");
		
		
		int value = (coords==null ? bmaxplay : getLongestChain(board, coords, Game.whitepiece));

		if(value == Game.boardsize){
			return Game.boardsize;
		}
		
		if(bmaxplay < value){
			bmaxplay = value;
		}
		
		return iterate(depth, alpha, beta, board, bmaxplay, bminplay, MAXIMIZER, plays);
	}
	
	public int minimizer(int depth,int alpha,int beta, byte[][] board, Integer[] coords ,int bmaxplay,int bminplay, Stack<Integer[]> plays){

		plays.push(coords.clone());
		//printBoard(board);
		
		if(depth == 0){
			return bmaxplay - bminplay;	
		}
		
		//System.out.println("Min: " + depth);
		//System.out.println("|");
		//System.out.println("|");
		//System.out.println("|");
		//System.out.println("V");
		
		int value = (coords==null ? bminplay : getLongestChain(board, coords, Game.blackpiece));
		
		if(value == Game.boardsize){
			return -Game.boardsize;
		}
		
		if(bminplay< value){
			bminplay = value;
		}
		
		return iterate(depth, alpha, beta, board, bmaxplay, bminplay, MINIMIZER, plays);
	}	

	private boolean listContainsCoords(ArrayList<Integer[]> list, Integer[] coordFind){
		for(Integer[] coord: list){
			if(coord[0]==coordFind[0] && coord[1]==coordFind[1]){
				return true;
			}
		}
		return false;
	}
	
	public int getLongestChain(byte[][] board, Integer[] coords, byte peca){

		
		Integer[] firstPoint = {coords[0], coords[1]};
		
		Integer[] secondPoint = {coords[2], coords[3]};
		
		ArrayList<Integer[]> steppedOn = new ArrayList<Integer[]>();
		Stack<Integer[]> explore = new Stack<Integer[]>();
		
		explore.push(firstPoint);
		explore.push(secondPoint);
		
		int hMax = -1;
		int hMin = Game.boardsize + 1;

		int vMax = -1;
		int vMin = Game.boardsize + 1;
		
		while(explore.size()!=0){
			Integer[] current = explore.pop();
			steppedOn.add(current);
			
			int X = current[0];
			int Y = current[1];
			
			if(X > hMax)
				hMax = X;

			if(X < hMin)
				hMin = X;
			
			if(Y > vMax)
				vMax = Y;
			
			if(Y < vMin)
				vMin = Y;
			
			//TODO: Change search to matrix of nodes with visited variable
			
			if(X>0){
				Integer[] tryP = new Integer[]{X-1,Y};
				if(board[X-1][Y] == peca && listContainsCoords(steppedOn, tryP) ){
					explore.push(tryP); 
				}
			}
			
			if(Y>0){
				Integer[] tryP = new Integer[]{X,Y-1};
				if(board[X][Y-1] == peca && listContainsCoords(steppedOn, tryP) ){
					explore.push(tryP);
				}
			}
			
			if(X < (Game.boardsize-1)){
				Integer[] tryP = new Integer[]{X+1,Y};
				if(board[X+1][Y] == peca && listContainsCoords(steppedOn, tryP) ){
					explore.push(tryP);
				}
			}

			if(Y < (Game.boardsize-1)){
				Integer[] tryP = new Integer[]{X,Y+1};
				if(board[X][Y+1] == peca && listContainsCoords(steppedOn, tryP) ){
					explore.push(tryP);
				}
			}
		}

		int hdis = (hMax - hMin) + 1;
		int vdis = (vMax - vMin) + 1;
		
		return (Game.blackpiece==peca)? hdis : vdis;
	}
	
	
}
