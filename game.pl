:- use_module(library(random)).
:- use_module(library(lists)).
:- include('tui2.pl').
:- use_module(library(clpfd)).


pieceX('X').
pieceO('O').
player1('X').
player2('O').
emptySpace(' ').
boardSize(12).
firstPosition(2).
secondPosition(4).





peca_oposta(Peca, NewPeca):- player1(P1), player2(P2), ((Peca == P1, NewPeca = P2);(Peca == P2, NewPeca = P1)).

seeList([], Nc, _, _, _):- boardSize(Nc).
seeList([X| Resto], Nc, Peca, Counter ,InitialIdx):-  boardSize(BoardSize), Nc < BoardSize,
											((Nc < InitialIdx, Y = ' ', NewPeca = Peca); 
											(Nc == InitialIdx, Y = Peca, peca_oposta(Peca, NewPeca)); 
											(AuxNc is Nc - InitialIdx, 0 is (AuxNc mod 5), Y = Peca, peca_oposta(Peca, NewPeca));
											(Y = ' ', NewPeca = Peca)),
											NewCounter is Counter + 1, X = Y, NewNc is Nc + 1, 
											seeList(Resto, NewNc, NewPeca, NewCounter, InitialIdx).

getState([], 0, _, _, _, _).
getState([X | Resto] , Nl, Nc, I, E, Peca):- Nl > 0, write(I), write('-'), write(E),
										((0 is (Nl mod 2), NewE = E, AuxI is I -1, ((AuxI < 0, NewI = 4); NewI= AuxI)
											,((E < NewI, peca_oposta(Peca, NewPeca));(Peca = NewPeca))
											);
										(NewI = I, AuxE is E -1, ((AuxE < 0, NewE = 4); NewE = AuxE) 
											,((NewE > I, peca_oposta(Peca, NewPeca));(Peca = NewPeca))
											)),
										((0 is (Nl mod 2), seeList(X, Nc, NewPeca, NewI, NewI));(seeList(X, Nc, NewPeca, NewE, NewE))),
										NewNl is Nl - 1, 
										getState(Resto, NewNl, Nc, NewI, NewE, NewPeca).

firstState(Board):- boardSize(BoardSize), player1(NewPeca),firstPosition(I),  secondPosition(E), getState(Board, BoardSize, 0, I, E, NewPeca).

drawBoardAux([], _).
drawBoardAux([Y | Ys], X):-  NextX is X+1, write('| '), write(X), ((X < 10, write(' |')); write('|')),
						 drawLine(Y), nl , drawBoardAux(Ys, NextX).
drawLine([]):- nl,  write('|---+---+---+---+---+---+---+---+---+---+---+---+---|').
drawLine([X | Xs]):- write(' '), write(X) , write(' |'), drawLine(Xs).


drawBoard2(X):- drawTopIndexes(12), nl, write('|---|---|---|---|---|---|---|---|---|---|---|---|---|'), nl,  drawBoardAux(X, 1).

drawTopIndexes(0):- nl, write('| '), write(0), write(' | ').
drawTopIndexes(X):- NextX is X -1, drawTopIndexes(NextX), write(X), ((X < 10, write(' | ')); write('| ')).





start:- 
	mainMenu.
	
who_starts(Peca):- 
			 repeat,
			 write('Who\'s starting?(\'X\' or \'O\')'), nl, %'
			 write('1. \'X\''), nl,
			 write('2. \'O\''), nl,
			 getInt(Input),
			 (
				(Input == 1, pieceX(Peca));
				(Input == 2, pieceO(Peca));
				
				nl, write('Error: invalid input.'), nl, nl,
				fail
				%pressEnterToContinue, nl,
				%gameModeMenu
			),
			cls.
			
		
pvp:-   who_starts(Starter_Peca),
		firstState(Board), 
		drawBoard(Board),
		start_pvp(Board, Starter_Peca).
		
start_pvp(Board, Peca):- player_play(Peca, Board, RulesAppliedBoard),
						 \+stopIfEndGame(RulesAppliedBoard),
						 peca_oposta(Peca, Peca_oposta),
						 start_pvp(RulesAppliedBoard, Peca_oposta),
						 !.
						 
start_pvp(Board, _):-  endGame(Board).
				
stopIfEndGame([List|Board]):- checkStateRow(List), stopIfEndGame(Board).
stopIfEndGame([]).		
		
checkStateRow([X|List]):- emptySpace(Espaco), X \== Espaco, checkStateRow(List).
checkStateRow([]).
		
		
		
		
		
		
%endGame(Board):- clear, 
				 %drawBoard(Board), nl,
				 %getScores(SX,SO,Board),
				 %showScore(SX, SO).
				 
getAdjacentCoordsOfSameType([X,Y],Board,AdjCoords):-
	get_pecaXY(X, Y, Board, Peca),
	getAdjacentCheckValid(X, Y, 0, -1, Board, Peca, AdjCoord1),
	getAdjacentCheckValid(X, Y, 1, 0, Board, Peca, AdjCoord2),
	getAdjacentCheckValid(X, Y, 0, 1, Board, Peca, AdjCoord3),
	getAdjacentCheckValid(X, Y, -1, 0, Board, Peca, AdjCoord4),
	append([AdjCoord1,AdjCoord2,AdjCoord3,AdjCoord4],AdjCoords).
		

getAdjacentCheckValid(X, Y, SideX, SideY, Board, Peca,Coords):-
	NewX is X + SideX,
	NewY is Y + SideY,
	get_pecaXY(NewX, NewY, Board, Peca),!,
	Coords = [[NewX,NewY]].
getAdjacentCheckValid(_, _, _, _, _, _,[]).
	

	
				 
endGame(Board, Piece, [CoordX,CoordY]):- 
	endGameAux(Board,Piece,[[CoordX,CoordY]],[],0).
	


endGameAux(_,_,_,_,BoardSize):-
	boardSize(BoardSize).
	%BoardSize=4.


endGameAux(_,_,[],_, _):-!,fail.

endGameAux(Board,Piece,[CoordToCheck|CoordsToCheck],CoordsChecked, SizeChain):-
	checkSizeChain(CoordToCheck,CoordsChecked,Piece,SizeChain,NewSizeChain),
	TempCoordsChecked = [CoordToCheck|CoordsChecked],
	getAdjacentCoordsOfSameType(CoordToCheck,Board,AdjCoords),
	filterAdjacentCoords(AdjCoords,TempCoordsChecked,CoordsToCheck,NewCoordsChecked,NewCoordsToCheck),
	%write(Board:Piece:NewCoordsToCheck:NewCoordsChecked:NewSizeChain),nl,
	endGameAux(Board,Piece,NewCoordsToCheck,NewCoordsChecked,NewSizeChain).
	
	

filterAdjacentCoords([],CoordsChecked,CoordsToCheck,CoordsChecked,CoordsToCheck).	
filterAdjacentCoords([AdjCoord|AdjCoords],CoordsChecked,CoordsToCheck,NewCoordsChecked,NewCoordsToCheck):-
	member(AdjCoord,CoordsChecked),!,
	filterAdjacentCoords(AdjCoords,CoordsChecked,CoordsToCheck,NewCoordsChecked,NewCoordsToCheck).
	
filterAdjacentCoords([AdjCoord|AdjCoords],CoordsChecked,CoordsToCheck,NewCoordsChecked,NewCoordsToCheck):-
	member(AdjCoord,CoordsToCheck),!,
	filterAdjacentCoords(AdjCoords,CoordsChecked,CoordsToCheck,NewCoordsChecked,NewCoordsToCheck).
	
filterAdjacentCoords([AdjCoord|AdjCoords],CoordsChecked,CoordsToCheck,NewCoordsChecked,NewCoordsToCheck):-
	!,
	TempCoordsToCheck = [AdjCoord|CoordsToCheck],
	filterAdjacentCoords(AdjCoords,CoordsChecked,TempCoordsToCheck,NewCoordsChecked,NewCoordsToCheck).
	
checkSizeChain([X,_],CoordsChecked,Piece,SizeChain,SizeChain):-
	pieceX(Piece),
	member([X,_],CoordsChecked),!.
	
checkSizeChain([_,Y],CoordsChecked,Piece,SizeChain,SizeChain):-
	pieceO(Piece),
	member([_,Y],CoordsChecked),!.

checkSizeChain(_,_,_,SizeChain,NewSizeChain):-
	!,	
	NewSizeChain is SizeChain + 1.
	
 
				 
				 
				 
				 
				 
					  
showScore(UnFnSX, UnFnSO):- SX is UnFnSX, SO is UnFnSO,
					  (((SX > SO, player1(X));(SX < SO, player2(X))),  write('Player '), write(X), write(' wins!'));
					  (SX =:= SO, write('Players tied!')).

%player_play(Peca, Board, Board2):-ask_coordinates(X1,Y1, Peca),
%								  play_peca(X1, Y1, Peca, Board, Board1),
%								  ask_coordinates(X2,Y2, Peca),
%								  play_peca(X2, Y2, Peca, Board1, Board2),
%								  checkValid(X1, Y1, Board2, Peca),
%								  checkValid(X2, Y2, Board2, Peca).


player_play(Peca, Board, Board3):-%play_peca(X1, Y1, Peca, Board, Board1),
								  %play_peca(X2, Y2, Peca, Board1, Board2),
								  repeat,
								  boardSize(BoardSize),
								  domain([X1,Y1,X2,Y2], 1, BoardSize),
								  labeling([],[X1,Y1,X2,Y2]),
								  write(X1:Y1:X2:Y2:nl),
								  
								  checkValid(X1, Y1, Board, Peca),
								  play_peca(X1, Y1, Peca, Board, Board2),
								  checkValid(X2, Y2, Board2, Peca),
								  play_peca(X2, Y2, Peca, Board2, Board3),
								  %write(Board3),nl,
								  get_char(_),
								  drawBoard(Board3),
								  fail.
		
ask_coordinates(X,Y, Peca):-  nl, write('Turn: '), write(Peca), nl, write('Enter Coordinates:'), nl,  
				
						repeat,
						write('X:'), getInt(X), 
						write('Y:'), getInt(Y), 
						boardSize(Limit),
						LimitPlus is Limit +1,
						X < LimitPlus , X > 0,
						Y < LimitPlus , Y > 0.
		
play_peca(X, Y, Peca, Board, NewBoard):- setPeca(Board, Y, X, Peca, NewBoard), !.
						
checkValid(X, Y, NewBoard, Peca):-
						checkValid(X, Y, -1, -1, NewBoard, Peca),
						checkValid(X, Y, -1, 1, NewBoard, Peca),
						checkValid(X, Y, 1, -1, NewBoard, Peca),
						checkValid(X, Y, 1, 1, NewBoard, Peca), !.
						
checkValid(X, Y, SideX, SideY, Board, Peca):-   
												NewX is X + SideX,
												NewY is Y + SideY,
												\+ get_pecaXY(NewX, NewY, Board, Peca).
												
checkValid(X, Y, SideX, SideY, Board, Peca):-   (NewY is Y + SideY,get_pecaXY(X,NewY, Board, Peca));
												(NewX is X + SideX,get_pecaXY(NewX,Y, Board, Peca)).
											
												%(write(NewY),get_pecaXY(X,NewY, Board, Peca),write(X),write('-'),write(NewY),write('='),write(Y),write('+'),write(SideY),write(Peca));get_pecaXY(NewX,Y, Board, Peca).												
	
get_pecaXY(X, Y, Board, Peca):- nth1(Y,Board,Line),
								nth1(X,Line,Peca).
								
replace( [L|Ls] , 0 , Y , Z , [R|Ls] ) :- % once we find the desired row,
  replace_column(L,Y,Z,R)                 % - we replace specified column, and we're done.
  .                                       %
replace( [L|Ls] , X , Y , Z , [L|Rs] ) :- % if we haven't found the desired row yet
  X > 0 ,                                 % - and the row offset is positive,
  X1 is X-1 ,                             % - we decrement the row offset
  replace( Ls , X1 , Y , Z , Rs )         % - and recurse down
  .                                       %

replace_column( [_|Cs] , 0 , Z , [Z|Cs] ) .  % once we find the specified offset, just make the substitution and finish up.
replace_column( [C|Cs] , Y , Z , [C|Rs] ) :- % otherwise,
  Y > 0 ,                                    % - assuming that the column offset is positive,
  Y1 is Y-1 ,                                % - we decrement it
  replace_column( Cs , Y1 , Z , Rs )         % - and recurse down.
  .                						
	
get_peca(X, [_|Xs], Peca):- X > 1, NewX is X - 1, get_peca(NewX, Xs, Peca).
get_peca(1, [Peca|_], Peca).
								 
equal_pecas(X,X).

setPecaLinha([X| Resto] , [Insert | Resto], 1, Peca):- emptySpace(Espaco) , 
													  X == Espaco,
													  Insert = Peca.
				
setPecaLinha([X | Resto1], [X | Resto2], NCol, Peca):-
	NewNCol is NCol -1,
	setPecaLinha(Resto1, Resto2, NewNCol, Peca).
				
setPeca( [Head1 | Resto], 1, NCol, Peca, [Head2 | Resto]) :- 
	setPecaLinha(Head1, Head2, NCol, Peca).
				
setPeca([Head | Resto1], NLinha, NCol, Peca, [Head | Resto2]) :-
	NLinha > 1, NewNLinha is NLinha - 1,
	setPeca(Resto1, NewNLinha, NCol, Peca, Resto2).
	
clear :- 
    format('~c~s~c~s', [0x1b, "[H", 0x1b, "[2J"]). 