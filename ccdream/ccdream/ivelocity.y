%{
	#include <stdio.h>
	#include "TransLayer.h"
	
	extern int yylex(void);

	void yyerror(char *s)
	{
		fprintf(stderr, "Error:%s\n", s);
	}
	
	int yywrap (void)
	{
		return 1;
	}
%}

%token NAME NUMBER SET TEXT IF ELSE END FOREACH IN 

%%

entry: statement				{ $$ = createEntry($1); }

statement: TEXT					{ $$ = createStatement(createTextBlock($1));}		
	|	NUMBER					{ $$ = createStatement(createNumberBlock($1)); }
	|	showvalue				{ $$ = createStatement($1); }
	|	TEXT statement			{ $$ = mergeTextStatement($1, $2); }
	|	NUMBER statement		{ $$ = mergeNumberStatement($1, $2); }
	|	command statement		{ $$ = mergeCommandStatement($1, $2); }
	|	showvalue statement		{ $$ = mergeShowValueStatement($1, $2); }
	;

command:	SET '(' NAME '=' expression ')'					{ $$ = createSetCommand($3, $5);}
	|	IF '(' expression ')' statement END					{ $$ = createIfCommand($3, $5, 0); }
	| 	IF '(' expression ')' statement ELSE statement END	{ $$ = createIfCommand($3, $5, $7); }
	|	FOREACH '(' NAME IN NAME ')' statement END			{ $$ = createForeachCommand($3, $5, $7); }
	;
	
showvalue:	NAME { $$ = createNameBlock($1); }
	;

expression:	expression '+' NUMBER	{ $$ = createExpAddBlock($1, $3); }
	|	expression '-' NUMBER		{ $$ = createExpSubBlock($1, $3); }
	|	NUMBER						{ $$ = createNumberBlock($1); }
	|	NAME						{ $$ = createNameBlock($1); }
	;

%%









