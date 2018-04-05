:- use_module(library(random)).
:- use_module(library(lists)).

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
drawLine([]):- nl,  write('|---|---|---|---|---|---|---|---|---|---|---|---|---|').
drawLine([X | Xs]):- write(' '), write(X) , write(' |'), drawLine(Xs).


drawBoard(X):- drawTopIndexes(12), nl, write('|---|---|---|---|---|---|---|---|---|---|---|---|---|'), nl,  drawBoardAux(X, 1).

drawTopIndexes(0):- nl, write('| '), write(0), write(' | ').
drawTopIndexes(X):- NextX is X -1, drawTopIndexes(NextX), write(X), ((X < 10, write(' | ')); write('| ')).

start:- repeat,
		clear,
		write('ALWAYS INSERT \'.\' AFTER EACH COMAND'),nl,nl,
		write('Choose game mode:'), nl,nl,
		write('1 - PvP (Player vs Player)'),nl,
		write('Enter Option: '),
		read(Option), get_char(_), write('asdasdasd'),
		((Option == 1, pvp);
		(write('Invalid option please try again.'), nl, nl)),
		fail.
	
who_starts(Peca):- nl, write('ALWAYS INSERT \'.\' AFTER EACH COMAND'), nl, nl, 
			 repeat,
			 write('Whos starting?(\'X\' or \'O\')'), nl, 
			 read(X), get_char(_),
			 
			 ((X == 'x', Peca = 'X'); (X == 'X', Peca = 'X');(X == 'o', Peca = 'O'); (X == 'O' , Peca = 'O'); 
			 (X \== 'x', X \== 'X', X \== 'o', X \== 'O', nl, write('Warning: Please enter \'X\' or \'O\'!'),nl,nl,fail)).
			
		
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
		
endGame(Board):- clear, 
				 drawBoard(Board), nl,
				 getScores(SX,SO,Board),
				 showScore(SX, SO).
					  
showScore(UnFnSX, UnFnSO):- SX is UnFnSX, SO is UnFnSO,
					  (((SX > SO, player1(X));(SX < SO, player2(X))),  write('Player '), write(X), write(' wins!'));
					  (SX =:= SO, write('Players tied!')).

player_play(Peca, Board, RulesAppliedBoard):- ask_coordinates(X,Y),
											  play_peca(X, Y, Peca, Board, RulesAppliedBoard).
		
ask_coordinates(X,Y):-  nl, write('Turn: '), nl, write('Enter Coordinates:'), nl,  
						repeat,
						write('X:'), read(X), get_char(_), 
						write('Y:'), read(Y), get_char(_), 
						boardSize(Limit),
						LimitPlus is Limit +1,
						X < LimitPlus , X > 0,
						Y < LimitPlus , Y > 0.
		
play_peca(X, Y, Peca, Board, NewBoard):- 
						setPeca(_, Board, Y, X, Peca, NewBoard),
						
						drawBoard(NewBoard).
						


get_peca(X, [_|Xs], Peca):- X > 1, NewX is X - 1, get_peca(NewX, Xs, Peca).
get_peca(1, [Peca|_], Peca).
								 
equal_pecas(X,X).

setPecaLinha(NewPeca, [X| Resto] , [Insert | Resto], 1, Peca):- emptySpace(Espaco) , 
																((X == Espaco, NewPeca = Peca, Insert = Peca); 
																(X \== Espaco, peca_oposta(Peca, NewPeca), Insert = X)).
				
setPecaLinha(NewPeca, [X | Resto1], [X | Resto2], NCol, Peca):-
	NewNCol is NCol -1,
	setPecaLinha(NewPeca, Resto1, Resto2, NewNCol, Peca).
				
setPeca(NewPeca, [Head1 | Resto], 1, NCol, Peca, [Head2 | Resto]) :-
	setPecaLinha(NewPeca, Head1, Head2, NCol, Peca).
				
setPeca(NewPeca, [Head | Resto1], NLinha, NCol, Peca, [Head | Resto2]) :-
	NLinha > 1, NewNLinha is NLinha - 1,
	setPeca(NewPeca, Resto1, NewNLinha, NCol, Peca, Resto2).
	
clear :- 
    format('~c~s~c~s', [0x1b, "[H", 0x1b, "[2J"]). 