package logic;

import java.util.ArrayList;
import java.util.Stack;

public class Minimax {
	
	private static final int maxint = Integer.MAX_VALUE;
	
	private static final boolean MINIMIZER = true;
	
	private static final boolean MAXIMIZER = false;

	
	public Stack<Integer[]> applyMinimax(int depth, Byte[][] board, Stack<Integer> valueS){
		int alpha = -maxint;
		int beta = maxint;
		
		int bminplay = 1;
		int bmaxplay = 1;
		
		Stack<Integer[]> listOfPlays = new Stack<Integer[]>();
		
		valueS.push(maximizer(depth, alpha, beta, board, null, bmaxplay, bminplay, listOfPlays));
		
		System.out.println(valueS.get(0));
		
		return listOfPlays;
	}
	
	private boolean validPlay(int y, int x, Byte[][] board, byte peca){
		
		if(board[y][x] != Game.empty){
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
				
				if(board[newY][newX] == peca){
					if(board[newY][x] != peca && board[y][newX] != peca){
						return false;
					}
				}
			}	
		}
		return true;
	}
	
	private int iterate(int depth, int alpha, int beta, Byte[][] board, int bmaxplay, int bminplay, boolean level, Stack<Integer[]> plays1){
		
		Stack<Integer[]> gPlays = null;
		
		byte peca = (level?Game.blackpiece:Game.whitepiece);
		
		for(int X1 = 0; X1 < Game.boardsize; X1++){

			for(int Y1 = 0; Y1 < Game.boardsize; Y1++){
				
				if(!validPlay(Y1, X1, board, peca) ){
					continue;
				}
				
				for(int X2 = 0; X2 < Game.boardsize; X2++){

					for(int Y2 = 0; (Y2 < Game.boardsize); Y2++){
						
						if(X1 == X2 && Y1 == Y2)
							continue;
						
						
						if(!validPlay(Y2, X2, board, peca) )
							continue;

						board[Y1][X1] = peca;
								
						board[Y2][X2] = peca;
						
						Stack<Integer[]> plays = new Stack<Integer[]>();
						
						Integer coords[] = new Integer[]{Y1, X1, Y2, X2};

						int value = (level ? 
										maximizer(depth - 1, alpha, beta, board, coords, bmaxplay, bminplay, plays): 
										minimizer(depth - 1, alpha, beta, board, coords, bmaxplay, bminplay, plays));
						
						
						board[Y1][X1] = Game.empty;
											
						board[Y2][X2] = Game.empty;
						
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
	
	private int maximizer(int depth,int alpha,int beta, Byte[][] board, Integer[] coords ,int bmaxplay,int bminplay, Stack<Integer[]> plays){

		if(coords!=null)
			plays.push(coords.clone());
		//printBoard(board);
		
		
		//System.out.println("Max: " + depth);
		//System.out.println("|");
		//System.out.println("|");
		//System.out.println("|");
		//System.out.println("V");
		
		
		int value = (coords==null ? bmaxplay : getLongestChainAux(board, coords, Game.blackpiece));

		if(value == Game.boardsize){
			return Game.boardsize;
		}
		
		if(bmaxplay < value){
			bmaxplay = value;
		}

		if(depth == 0)
			return bmaxplay - bminplay;
		
		return iterate(depth, alpha, beta, board, bmaxplay, bminplay, MAXIMIZER, plays);
	}
	
	public int minimizer(int depth,int alpha,int beta, Byte[][] board, Integer[] coords ,int bmaxplay,int bminplay, Stack<Integer[]> plays){

		plays.push(coords.clone());
		//printBoard(board);
		
		
		//System.out.println("Min: " + depth);
		//System.out.println("|");
		//System.out.println("|");
		//System.out.println("|");
		//System.out.println("V");
		
		int value = (coords==null ? bminplay : getLongestChainAux(board, coords, Game.whitepiece));
		
		if(value == Game.boardsize){
			return -Game.boardsize;
		}
		
		if(bminplay< value){
			bminplay = value;
		}

		
		if(depth == 0){
			return bmaxplay - bminplay;
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
	
	private int getLongestChainAux(Byte[][] board, Integer[] coords, byte peca){
		
		int a1 = getLongestChain(board, new Integer[]{coords[0],coords[1]}, peca);
		
		int a2 = getLongestChain(board, new Integer[]{coords[2],coords[3]}, peca);
		
		return (a1>a2)?a1:a2;
		
	}
	
	private int getLongestChain(Byte[][] board, Integer[] coords, byte peca){

		
		Integer[] point = {coords[0], coords[1]};
		
		ArrayList<Integer[]> steppedOn = new ArrayList<Integer[]>();
		Stack<Integer[]> explore = new Stack<Integer[]>();
		
		explore.push(point);
		
		int hMax = -1;
		int hMin = Game.boardsize + 1;

		int vMax = -1;
		int vMin = Game.boardsize + 1;
		
		while(explore.size()!=0){
			
			Integer[] current = explore.pop();
			steppedOn.add(current);
			
			int Y = current[0];
			int X = current[1];
			
			if(X > hMax)
				hMax = X;

			if(X < hMin)
				hMin = X;
			
			if(Y > vMax)
				vMax = Y;
			
			if(Y < vMin)
				vMin = Y;
			
			//TODO: Change search to matrix of nodes with visited variable
			
			if(Y>0){
				Integer[] tryP = new Integer[]{Y-1,X};
				if(board[Y-1][X] == peca && !listContainsCoords(steppedOn, tryP) ){
					explore.push(tryP); 
				}
			}
			
			if(X>0){
				Integer[] tryP = new Integer[]{Y,X-1};
				if(board[Y][X-1] == peca && !listContainsCoords(steppedOn, tryP) ){
					explore.push(tryP);
				}
			}
			
			if(Y < (Game.boardsize-1)){
				Integer[] tryP = new Integer[]{Y+1,X};
				if(board[Y+1][X] == peca && !listContainsCoords(steppedOn, tryP) ){
					explore.push(tryP);
				}
			}

			if(X < (Game.boardsize-1)){
				Integer[] tryP = new Integer[]{Y,X+1};
				if(board[Y][X+1] == peca && !listContainsCoords(steppedOn, tryP) ){
					explore.push(tryP);
				}
			}
		}

		int hdis = (hMax - hMin) + 1;
		int vdis = (vMax - vMin) + 1;

		return (Game.blackpiece==peca)? hdis : vdis;
	}
	
	
}
