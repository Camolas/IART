%!	Print Board

getCellSymbol(sp, ' ').
getCellSymbol(ct, '#').
getCellSymbol(h3, 'X').
getCellSymbol(pc, 'O').
getCellSymbol(pw, 'O').
getCellSymbol(pl, 'O').
getCellSymbol(pt, 'O').

lineIdentifiers([' 1 ', ' 2 ', ' 3 ', ' 4 ', ' 5 ', ' 6 ', ' 7 ', ' 8 ', ' 9 ', '10 ', '11 ', '12 ']).

cls:-
	write('\33\[2J').

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
	BoardSizePlus1 is BoardSize + 1,
	 nl.
	
printColumnsIdentifiersAux(Identifier):-
	IdentifierPlus1 is Identifier + 1,
	Identifier < 10, write('  '),
	write(Identifier),
	write(' '),
	printColumnsIdentifiersAux(IdentifierPlus1).
	
printColumnsIdentifiersAux(Identifier):-
	IdentifierPlus1 is Identifier + 1,
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
	write(Elem),
	write(' |').

printBoardLine([]).	
printBoardLine([LineHead|LineTail]):-
	printBoardCell(LineHead),
	printBoardLine(LineTail).

	
printLine(BoardLine,LineIdentifier):-
	LineIdentifier < 10, write('  '),
	write(LineIdentifier),
	write(' |'),
	printBoardLine(BoardLine),
	nl.
	
	
printLine(BoardLine,LineIdentifier):-
	write(' '),
	write(LineIdentifier),
	write(' |'),
	printBoardLine(BoardLine),
	nl.

printBoardAux([],_).
printBoardAux([BoardHead | BoardTail], Identifier):-
	printLine(BoardHead,Identifier),
	%printSeparator,
	IdentifierPlus1 is Identifier + 1,
	printBoardAux(BoardTail,IdentifierPlus1).

%drawBoard(Board):-
	%nl,
	%printSeparator,
	%printBoardAux(Board,1).
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
	
drawBoard(B):-
	nl,
	drawBoardAux(B).
	
drawBoardAux([]).	
drawBoardAux([LineHead|LineTail]):-
	write(LineHead),nl,
	drawBoardAux(LineTail).




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
	write('Pressione Enter para continuar.'),
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
	
	( SecDigTemp = 10 ->
		(
			Input = FirstDig
		);
		(
			SecDig is SecDigTemp - 48,
			Input is FirstDig*10+SecDig,
			get_char(_)		
		)	
	).

	
printMainMenu:-
	printLogo,
	write('                                          1: Player vs Player                                     '), nl,
	write('                                          2: Player vs Computer                                   '), nl,
	write('                                          3: Computer vs Computer                                  '), nl,
	write('                                          4: Exit                                                 '), nl,
	write('                                    Please Choose (1-4): ').


printLogo:-
	write('################################################################################################# '), nl,
	write('           ___  ___                  _   _                           _                            '), nl,
	write('           |  \\/  |                 | | | |                         | |                          '), nl,
	write('           | .  . | ___   ___  _ __ | |_| | __ _ _ ____   _____  ___| |_ ___ _ __ ___             '), nl,
	write('           | |\\/| |/ _ \\ / _ \\| \'_ \\|  _  |/ _` | \'__\\ \\ / / _ \\/ __| __/ _ \\ \'__/ __| '), nl,
	write('           | |  | | (_) | (_) | | | | | | | (_| | |   \\ V /  __/\\__ \\ ||  __/ |  \\__ \\       '), nl,
	write('           \\_|  |_/\\___/ \\___/|_| |_\\_| |_/\\__,_|_|    \\_/ \\___||___/\\__\\___|_|  |___/   '), nl, nl,
	write('################################################################################################# '), nl, nl.