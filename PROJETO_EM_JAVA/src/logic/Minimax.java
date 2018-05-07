package logic;

import java.util.ArrayList;
import java.util.Stack;

public class Minimax {

	private static final int maxint = Integer.MAX_VALUE;
	
	private static final boolean MINIMIZER = true;
	
	private static final boolean MAXIMIZER = false;
	
	
	public void applyMinimax(int depth, byte[][] board){
		int alpha = -maxint;
		int beta = maxint;
		
		int bminplay = 1;
		int bmaxplay = 1;
		
		maximizer(depth, alpha, beta, board, null, bmaxplay, bminplay);
	}
	
	private boolean validPlay(int x, int y, byte[][] board, byte peca){
		
		if(board[x][y] != Game.empty){
			return false;
		}
		
		int[] auxs = {-1,1};
		
		for(int i = 0; i < 2; i++){
			for(int e = 0; e < 2; e++){
				int newX = x + auxs[i];
				int newY = y + auxs[e];
				
				if(board[newX][newY] == peca){
					if(board[x][newY] != peca && board[newX][y] != peca){
						return false;
					}
				}
			}	
		}
		return true;
	}
	
	private int iterate(int depth, int alpha, int beta, byte[][] board, int bmaxplay, int bminplay, boolean level){
		
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
						
						
						if(!validPlay(X2, Y2, board, peca) ){
							continue;
						}

						board[X1][Y1] = peca;
								
						board[X2][Y2] = peca;
						
						int coords[] = {X1, Y1, X2, Y2}; 
						
						int value = (level ? 
										maximizer(depth - 1, alpha, beta, board, coords, bmaxplay, bminplay): 
										minimizer(depth - 1, alpha, beta, board, coords, bmaxplay, bminplay));
						
						board[X1][Y1] = Game.empty;
											
						board[X2][Y2] = Game.empty;
						
						if(level == MINIMIZER){ 
							if(value < beta){
								beta = value;  
							}
						}else
							if(level == MAXIMIZER)
								if(value > alpha){
									alpha = value;  
								}
							
						if(alpha >= beta)
							return (level?alpha:beta);
					}	
				}	
			}	
		}
		return alpha;
	}
	
	private int maximizer(int depth,int alpha,int beta, byte[][] board, int[] coords ,int bmaxplay,int bminplay){

		if(depth == 0){
			return bmaxplay - bminplay;	
		}
		
		int value = (coords==null ? bmaxplay : getLongestChain(board, coords, Game.whitepiece));

		if(value == Game.boardsize){
			return Game.boardsize;
		}
		
		//System.out.println("maximizer");
		
		if(bmaxplay < value){
			bmaxplay = value;
		}
		
		return iterate(depth, alpha, beta, board, bmaxplay, bminplay, MAXIMIZER);
		
	}
	
	public int minimizer(int depth,int alpha,int beta, byte[][] board, int[] coords ,int bmaxplay,int bminplay){
		
		if(depth == 0){
			return bmaxplay - bminplay;	
		}
		
		int value = (coords==null ? bminplay : getLongestChain(board, coords, Game.blackpiece));
		
		if(value == Game.boardsize){
			return -Game.boardsize;
		}
		
		if(bminplay< value){
			bminplay = value;
		}
		
		return iterate(depth, alpha, beta, board, bmaxplay, bminplay, MINIMIZER);
	}	

	private boolean listContainsCoords(ArrayList<Integer[]> list, Integer[] coordFind){
		for(Integer[] coord: list){
			if(coord[0]==coordFind[0] && coord[1]==coordFind[1]){
				return true;
			}
		}
		return false;
	}
	
	public int getLongestChain(byte[][] board, int[] coords, byte peca){

		
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
		
		//TODO: if(peca = preta) (returnar par com valor horizontal e valor vertical) else (returnar par com valor vertical e valor horizontal) 
		
		return 1;
	}
	
	
}
