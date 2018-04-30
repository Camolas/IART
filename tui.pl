%!	Print Board


getCellSymbol(sp, ' ').
getCellSymbol(whitePiece, 'O').
getCellSymbol(blackPiece, 'X').


lineIdentifiers([' 1 ', ' 2 ', ' 3 ', ' 4 ', ' 5 ', ' 6 ', ' 7 ', ' 8 ', ' 9 ', '10 ', '11 ', '12 ']).

cls:-
	write('\33\[2J').
	
clear :- 
    format('~c~s~c~s', [0x1b, "[H", 0x1b, "[2J"]).

printSeparator:-
	boardSize(BoardSize),
	write('    +'),
	printSeparatorAux(BoardSize), nl.

printSeparatorAux(0).	
printSeparatorAux(BoardSize):-
	write('---+'),
	BoardSizeMinus1 is BoardSize - 1,
	printSeparatorAux(BoardSizeMinus1).

	
printColumnsIdentifiers:-
	write('    '),
	printColumnsIdentifiersAux(1).
	
printColumnsIdentifiersAux(BoardSizePlus1):-
	boardSize(BoardSize),
	BoardSizePlus1 is BoardSize + 1, !,
	 nl.
	
printColumnsIdentifiersAux(Identifier):-
	IdentifierPlus1 is Identifier + 1,
	Identifier < 10, !, write('  '),
	write(Identifier),
	write(' '),
	printColumnsIdentifiersAux(IdentifierPlus1).
	
printColumnsIdentifiersAux(Identifier):-
	IdentifierPlus1 is Identifier + 1,
	Identifier >= 10, !,
	write(' '),
	write(Identifier),
	write(' '),
	printColumnsIdentifiersAux(IdentifierPlus1).

%printColumnsIdentifiersAux:-
%	write('     1   2   3   4   5   6   7   8   9   10  11  12'),
%	nl.

printBoardCell(Elem):-
	%getCellSymbol(Elem,ElemToPrint),
	write(' '),
	getCellSymbol(Elem,ElemToPrint),
	write(ElemToPrint),
	write(' |').

printBoardLine([]).	
printBoardLine([LineHead|LineTail]):-
	printBoardCell(LineHead),
	printBoardLine(LineTail).

	
printLine(BoardLine,LineIdentifier):-
	LineIdentifier < 10, !, write('  '),
	write(LineIdentifier),
	write(' |'),
	printBoardLine(BoardLine),
	nl.
	
	
printLine(BoardLine,LineIdentifier):-
	!,
	write(' '),
	write(LineIdentifier),
	write(' |'),
	printBoardLine(BoardLine),
	nl.

printBoardAux([],_).
printBoardAux([BoardHead | BoardTail], Identifier):-
	printLine(BoardHead,Identifier),
	printSeparator,
	IdentifierPlus1 is Identifier + 1,
	printBoardAux(BoardTail,IdentifierPlus1).

%drawBoard(Board):-
	%nl,
	%printSeparator,
	%printBoardAux(Board,1),
%
	%printColumnsIdentifiers.

%drawBoard([]).	
%drawBoard([LineHead|LineTail]):-
	%write(LineHead),nl,
	%drawBoard(LineTail).
	%nl,
	%printSeparator,
	%lineIdentifiers(Identifiers),
	%printBoardAux(Board,1),
	%printColumnsIdentifiers.
	
%drawBoardAux([], _).
%drawBoardAux([Y | Ys], X):-  NextX is X+1, write('| '), write(X), ((X < 10, write(' |')); write('|')),
%						 drawLine(Y), nl , drawBoardAux(Ys, NextX).
%drawLine([]):- nl,  write('|---+---+---+---+---+---+---+---+---+---+---+---+---|').
%drawLine([X | Xs]):- write(' '), write(X) , write(' |'), drawLine(Xs).


%drawBoard2(X):- drawTopIndexes(12), nl, write('|---|---|---|---|---|---|---|---|---|---|---|---|---|'), nl,  drawBoardAux(X, 1).

%drawTopIndexes(0):- nl, write('| '), write(0), write(' | ').
%drawTopIndexes(X):- NextX is X -1, drawTopIndexes(NextX), write(X), ((X < 10, write(' | ')); write('| ')).
	
drawBoard(B):-
	nl,
	drawBoardAux(B),nl.
	
drawBoardAux([]).	
drawBoardAux([BoardHead|BoardTail]):-
	drawLine(BoardHead),
	drawBoardAux(BoardTail).

drawLine(Line):-
	write('['),
	drawLineAux(Line),
	write(']'),
	nl.

drawLineAux([LastElem|[]]):-
	getCellSymbol(LastElem,Symbol),
	write(Symbol).
	
drawLineAux([LineHead|LineTail]):-
	getCellSymbol(LineHead,Symbol),
	write(Symbol),
	write(','),
	drawLineAux(LineTail).
	
	




%! Menu
mainMenu:-
	repeat,
	cls,
	printMainMenu,
	getInt(Input),
	(
		Input = 1 -> pvp;
		%Input = 2 -> startBvPGame;
		%Input = 3 -> startBvBGame;
		%Input = 4;
		Input = 4 -> closeGame;

		%nl,
		%write('Error: invalid input.'), nl,
		fail
		%pressEnterToContinue, nl,
		%gameModeMenu
	).
	
	
	 
closeGame:-write('Closing Game...').
	

pressEnterToContinue:-
	write('Press Enter to Continue.'),
	get_char(_),
	nl.

getChar(Input):-
	get_char(Input),
	get_char(_).

getCode(Input):-
	get_code(TempInput),
	get_code(_),
	Input is TempInput - 48.

getInt(Input):-
	get_code(FirstDigTemp),
	FirstDig is FirstDigTemp - 48,	
	get_code(SecDigTemp),	
	(SecDigTemp = 10 ->
		(Input = FirstDig);
		(SecDig is SecDigTemp - 48,
		 Input is FirstDig*10+SecDig,
		 get_char(_))	
	).

	
printMainMenu:-
	printLogo,
	write('                                        1: Player vs Player                                     '), nl,
	write('                                        2: Player vs Computer                                   '), nl,
	write('                                        3: Computer vs Computer                                 '), nl,
	write('                                        4: Exit                                                 '), nl,
	write('                                    Please Choose (1-4):                                        '), nl,nl,
	write('################################################################################################').


printLogo:-
	write('################################################################################################'), nl,
	write('                         __    __ _     _      _          _           _                         '), nl,
	write('                        / / /\\ \\ \\ |__ (_)_ __| |_      _(_)_ __   __| |                     '), nl,
	write('                        \\ \\/  \\/ / \'_ \\| | \'__| \\ \\ /\\ / / | \'_ \\ / _` |             '), nl, %'
	write('                         \\  /\\  /| | | | | |  | |\\ V  V /| | | | | (_| |                     '), nl, 
	write('                          \\/  \\/ |_| |_|_|_|  |_| \\_/\\_/ |_|_| |_|\\__,_|                   '), nl, nl,
	write('################################################################################################'), nl, nl.
	

