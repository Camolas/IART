package logic;


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
							
						//if(alpha >= beta)
						//	return (level?alpha:beta);
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
		
		int value = (coords==null ? bmaxplay : getLongestChain(board, coords));

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
		
		int value = (coords==null ? bminplay : getLongestChain(board, coords));
		
		if(value == Game.boardsize){
			return -Game.boardsize;
		}
		
		//System.out.println("minimizer X:"+coords[0] + " Y:" + coords[1]+ " X:" + coords[2]+ " Y:" +coords[3]);
		
		if(bminplay< value){
			bminplay = value;
		}
		
		return iterate(depth, alpha, beta, board, bmaxplay, bminplay, MINIMIZER);
	}	

	public int getLongestChain(byte[][] board, int[] coords){

		int X1 = coords[0];
		int Y1 = coords[1];
		int X2 = coords[2];
		int Y2 = coords[3];
		
		//Arraylist<int>
		return 1;
	}
	
	
}
