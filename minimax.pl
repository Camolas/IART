:- use_module(library(statistics)).
:- use_module(library(between)).

maxint(X):- boardSize(BS), X is BS + 100.

dominio(V):- boardSize(BS),
			 between(1,BS, V).

sucessor(Board, (Board_Aux, Coords), Level):- 

			  dominio(X1),
			  dominio(X2),
			  dominio(Y1),
			  dominio(Y2),
			  
			  pieceX(X),
			  pieceO(O),
			  
			  ((Level == max_, Peca = X); (Level == min_, Peca = O)),
			  
			  checkValid(X1, Y1, Board, Peca),
			  play_peca(X1, Y1, Peca, Board, Board2),
			  checkValid(X2, Y2, Board2, Peca),
			  play_peca(X2, Y2, Peca, Board2, Board_Aux),
			  
			  Coords = [X1,Y1,X2,Y2].
		

test:- minimax(3, [[sp,blackPiece,sp,sp,sp,sp,whitePiece,sp,sp,sp,sp,blackPiece]
					,[sp,sp,sp,whitePiece,sp,sp,sp,sp,blackPiece,sp,sp,sp]
					,[whitePiece,sp,sp,sp,sp,blackPiece,sp,sp,sp,sp,whitePiece,sp]
					,[sp,sp,blackPiece,sp,sp,sp,sp,whitePiece,sp,sp,sp,sp]
					,[sp,sp,sp,sp,whitePiece,sp,sp,sp,sp,blackPiece,sp,sp]
					,[sp,whitePiece,sp,sp,sp,sp,blackPiece,sp,sp,sp,sp,whitePiece]
					,[sp,sp,sp,blackPiece,sp,sp,sp,sp,whitePiece,sp,sp,sp]
					,[blackPiece,sp,sp,sp,sp,whitePiece,sp,sp,sp,sp,blackPiece,sp]
					,[sp,sp,whitePiece,sp,sp,sp,sp,blackPiece,sp,sp,sp,sp]
					,[sp,sp,sp,sp,blackPiece,sp,sp,sp,sp,whitePiece,sp,sp]
					,[sp,blackPiece,sp,sp,sp,sp,whitePiece,sp,sp,sp,sp,blackPiece]
					,[sp,sp,sp,whitePiece,sp,sp,sp,sp,blackPiece,sp,sp,sp]]).
		

getPlayValue(Board, Coord, 1).
		
minimax(Depth, Board):- 

				maxint(Maxint), 
				Alpha is -Maxint, 
				Beta is Maxint,
				maximizador(Depth, Alpha, Beta, Board, [], _, Alpha , Alpha).

				
				
iterate_max([], Alpha, _, _ , Alpha, _, _).				

iterate_max(_, Alpha, Beta, _ , Beta, _, _):- Alpha >= Beta.
				
iterate_max([(Board, Coord)|Boards], Alpha, Beta, Depth, Return, BestMaxPlay, BestMinPlay):-
							
				Alpha < Beta,
				minimizador(Depth, Alpha, Beta, Board, Coord, MinReturn, BestMaxPlay, BestMinPlay),
				max([Alpha, MinReturn], NewAlpha),
				iterate_max(Boards, NewAlpha, Beta,  Depth, Return, BestMaxPlay, BestMinPlay).
	
iterate_min([], _, Beta, _ , Beta, _, _).				

iterate_min(_, Alpha, Beta, _ , Alpha, _, _):- Alpha >= Beta.
				
iterate_min([(Board, Coord)|Boards], Alpha, Beta, Depth, Return, BestMaxPlay, BestMinPlay):-
				
				Alpha < Beta,
				maximizador(Depth, Alpha, Beta, Board, Coord, MaxReturn, BestMaxPlay, BestMinPlay),
				min([Beta, MaxReturn], NewBeta),
				iterate_min(Boards, Alpha, NewBeta, Depth, Return, BestMaxPlay, BestMinPlay).


				
maximizador(0, _, _, _, _, Return, BestMaxPlay, BestMinPlay):- Return is BestMaxPlay - BestMinPlay.
				
maximizador(Depth, Alpha, Beta, Board, Coord, Return, BestMaxPlay, BestMinPlay):-

				Depth > 0,
				
				write('Maximizer: ':Coord:' Depth: ':Depth),nl,
				%drawBoard(Board),
				
				((Coord == [], Value = BestMaxPlay) ; getPlayValue(Board, Coord, Value)),
				
				boardSize(BS),
				
				((Value == BS, Return = BS);
				
				(Value < BS,
				
				max([BestMaxPlay, Value], NewBestMaxPlay),

				findall((Board_Aux, Coords), sucessor(Board, (Board_Aux, Coords), max_), Boards),
				
				NewDepth is Depth - 1,
				
				iterate_max(Boards, Alpha, Beta, NewDepth, Return, NewBestMaxPlay, BestMinPlay))).
				
			
minimizador(0, _, _, _, _, Return, BestMaxPlay, BestMinPlay):- Return is BestMaxPlay - BestMinPlay.
			
minimizador(Depth, Alpha, Beta, Board, Coord, Return, BestMaxPlay, BestMinPlay):-

				Depth > 0,
				
				write('Minimizer: ':Coord:' Depth: ':Depth),nl,
				%drawBoard(Board),
				
				
				((Coord == [], Value = BestMinPlay) ; getPlayValue(Board, Coord, Value)),
				
				boardSize(BS),
				
				((Value == BS, Return = BS);
				
				(Value < BS,
				
				max([BestMinPlay, Value], NewBestMinPlay),

				findall((Board_Aux, Coords), sucessor(Board, (Board_Aux, Coords), min_), Boards),
				
				NewDepth is Depth - 1,
				
				iterate_min(Boards, Alpha, Beta, NewDepth, Return, BestMaxPlay, NewBestMinPlay))).
				