
maxint(X):- boardSize(BS), X is BS + 100.

dominio(V):- boardSize(BS)
			 between(1,BS, V).

sucessor(Board, (Board_Aux, Coords), Level):- 

			  dominio(X1),
			  dominio(X2),
			  dominio(Y1),
			  dominio(Y2),
			  
			  pieceX(X),
			  pieceO(O),
			  
			  ((Level == max, Peca = X); (Level == min, Peca = O)),
			  
			  checkValid(X1, Y1, Board, Peca),
			  play_peca(X1, Y1, Peca, Board, Board2),
			  checkValid(X2, Y2, Board2, Peca),
			  play_peca(X2, Y2, Peca, Board2, Board_Aux),
			  
			  Coords = [X1,Y1,X2,Y2].
			  

minimax(Depth, Board):- 

				maxint(Maxint), 
				Alpha is -Maxint, 
				Beta is Maxint,
				maximizador(Depth, Alpha, Beta, Board, [], _, - Maxint, - Maxint).

				
				
iterate_max([], Alpha, _, _ , Alpha).				

iterate_max(_, Alpha, Beta, _ , Beta):- Alpha >= Beta.
				
iterate_max([(Board, Coord)|Boards], Alpha, Beta, Depth, Return, BestMaxPlay, BestMinPlay):-
							
				Alpha < Beta,
				minimizador(Depth, Alpha, Beta, Board, Coord, MinReturn, BestMaxPlay, BestMinPlay),
				max_list([Alpha, MinReturn], NewAlpha),
				iterate(Boards, NewAlpha, Beta,  Depth, Return).
	
iterate_min([], _, Beta, _ , Beta).				

iterate_min(_, Alpha, Beta, _ , Alpha):- Alpha >= Beta.
				
iterate_min([(Board, Coord)|Boards], Alpha, Beta, Depth, Return, BestMaxPlay, BestMinPlay):-
				
				Alpha < Beta,
				maximizador(Depth, Alpha, Beta, Board, Coord, MaxReturn, BestMaxPlay, BestMinPlay),
				min_list([Beta, MaxReturn], NewBeta),
				iterate(Boards, Alpha, NewBeta, Depth, Return).


				
maximizador(0, _, _, _, _, Return, BestMaxPlay, BestMinPlay):- Return is BestMaxPlay - BestMinPlay.
				
maximizador(Depth, Alpha, Beta, Board, Coord, Return, BestMaxPlay, BestMinPlay):-

				Depth > 0,
				
				((Coord == [], Value = BestMaxPlay) ; getPlayValue(Board, Coord, Value)),
				
				boardSize(BS),
				
				(Value == BS, Return = BS);
				
				(Value < BS,
				
				max_list([BestMaxPlay, Value], NewBestMaxPlay),

				findall((Board_Aux, Coords), sucessor(Board, (Board_Aux, Coords), max), Boards),
				
				NewDepth is Depth - 1,
				
				iterate_max(Boards, Alpha, Beta, NewDepth, Return, NewBestMaxPlay, BestMinPlay)).
				
			
minimizador(0, _, _, _, _, Return, BestMaxPlay, BestMinPlay):- Return is BestMaxPlay - BestMinPlay.
			
minimizador(Depth, Alpha, Beta, Board, Coord, Return, BestMaxPlay, BestMinPlay):-

				Depth > 0,
				
				((Coord == [], Value = BestMinPlay) ; getPlayValue(Board, Coord, Value)),
				
				boardSize(BS),
				
				(Value == BS, Return = BS);
				
				(Value < BS,
				
				max_list([BestMinPlay, Value], NewBestMinPlay),

				findall((Board_Aux, Coords), sucessor(Board, (Board_Aux, Coords), min), Boards),
				
				NewDepth is Depth - 1,
				
				iterate_min(Boards, Alpha, Beta, NewDepth, Return, BestMaxPlay, NewBestMinPlay)).
				