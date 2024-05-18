grammar JDQL;

statement : select_statement | update_statement | delete_statement;

select_statement : select_clause? from_clause? where_clause? orderby_clause?;
update_statement : UPDATE entity_name set_clause where_clause?;
delete_statement : DELETE from_clause where_clause?;

from_clause : FROM entity_name;

where_clause : WHERE conditional_expression;

set_clause : SET update_item (',' update_item)*;
update_item : state_field_path_expression '=' (scalar_expression | 'NULL');

select_clause : SELECT select_list;
select_list
    : state_field_path_expression (',' state_field_path_expression)*
    | aggregate_expression
    ;
aggregate_expression : COUNT '(' THIS ')';

orderby_clause : ORDER BY orderby_item (',' orderby_item)*;
orderby_item : state_field_path_expression (ASC | DESC);

conditional_expression
    // highest to lowest precedence
    : '(' conditional_expression ')'
    | null_comparison_expression
    | in_expression
    | between_expression
    | like_expression
    | comparison_expression
    | NOT conditional_expression
    | conditional_expression AND conditional_expression
    | conditional_expression OR conditional_expression
    ;

comparison_expression : scalar_expression ('=' | '>' | '>=' | '<' | '<=' | '<>') scalar_expression;
between_expression : scalar_expression NOT? BETWEEN scalar_expression AND scalar_expression;
like_expression : scalar_expression NOT? LIKE STRING;

in_expression : state_field_path_expression NOT? IN '(' in_item (',' in_item)* ')';
in_item : literal | enum_literal | input_parameter; // could simplify to just literal

null_comparison_expression : state_field_path_expression IS NOT? NULL;

scalar_expression
    // highest to lowest precedence
    : '(' scalar_expression ')'
    | primary_expression
    | ('+' | '-') scalar_expression
    | scalar_expression ('*' | '/') scalar_expression
    | scalar_expression ('+' | '-') scalar_expression
    | scalar_expression '||' scalar_expression
    ;

primary_expression
    : function_expression
    | special_expression
    | state_field_path_expression
    | enum_literal
    | input_parameter
    | literal
    ;

function_expression
    : 'ABS' '(' scalar_expression ')'
    | 'LENGTH' '(' scalar_expression ')'
    | 'LOWER' '(' scalar_expression ')'
    | 'UPPER' '(' scalar_expression ')'
    | 'LEFT' '(' scalar_expression ',' scalar_expression ')'
    | 'RIGHT' '(' scalar_expression ',' scalar_expression ')'
    ;

special_expression
    : 'LOCAL' 'DATE'
    | 'LOCAL' 'DATETIME'
    | 'LOCAL' 'TIME'
    | 'TRUE'
    | 'FALSE'
    ;

state_field_path_expression : IDENTIFIER ('.' IDENTIFIER)*;

entity_name : IDENTIFIER; // no ambiguity

enum_literal : IDENTIFIER ('.' IDENTIFIER)*; // ambiguity with state_field_path_expression resolvable semantically

input_parameter : ':' IDENTIFIER | '?' INTEGER;

literal : STRING | INTEGER | DOUBLE;

// Tokens defined to be case-insensitive using character classes
SELECT          : [sS][eE][lL][eE][cC][tT];
UPDATE          : [uU][pP][dD][aA][tT][eE];
DELETE          : [dD][eE][lL][eE][tT][eE];
FROM            : [fF][rR][oO][mM];
WHERE           : [wW][hH][eE][rR][eE];
SET             : [sS][eE][tT];
ORDER           : [oO][rR][dD][eE][rR];
BY              : [bB][yY];
NOT             : [nN][oO][tT];
IN              : [iI][nN];
IS              : [iI][sS];
NULL            : [nN][uU][lL][lL];
COUNT           : [cC][oO][uU][nN][tT];
TRUE            : [tT][rR][uU][eE];
FALSE           : [fF][aA][lL][sS][eE];
ASC             : [aA][sS][cC];
DESC            : [dD][eE][sS][cC];
AND             : [aA][nN][dD];
OR              : [oO][rR];
ABS             : [aA][bB][sS];
LENGTH          : [lL][eE][nN][gG][tT][hH];
LOWER           : [lL][oO][wW][eE][rR];
UPPER           : [uU][pP][pP][eE][rR];
LEFT            : [lL][eE][fF][tT];
RIGHT           : [rR][iI][gG][hH][tT];
LOCAL_DATE      : [lL][oO][cC][aA][lL] [dD][aA][tT][eE];
LOCAL_DATETIME  : [lL][oO][cC][aA][lL] [dD][aA][tT][eE][tT][iI][mM][eE];
LOCAL_TIME      : [lL][oO][cC][aA][lL] [tT][iI][mM][eE];
BETWEEN         : [bB][eE][tT][wW][eE][eE][nN];
LIKE            : [lL][iI][kK][eE];
THIS            : [tT][hH][iI][sS];

// Operators
EQ              : '=';
GT              : '>';
LT              : '<';
NEQ             : '<>';
GTEQ            : '>=';
LTEQ            : '<=';
PLUS            : '+';
MINUS           : '-';
MUL             : '*';
DIV             : '/';
CONCAT          : '||';

// Special Characters
COMMA           : ',';
DOT             : '.';
LPAREN          : '(';
RPAREN          : ')';
COLON           : ':';
QUESTION        : '?';

// Identifier and literals
IDENTIFIER      : [a-zA-Z_][a-zA-Z0-9_]*;
STRING : '"' ( ~["\\] | '\\' . )* '"'  // double quoted strings
       | '\'' ( ~['\\] | '\\' . )* '\'';  // single quoted strings
INTEGER         : '-'?[0-9]+;
DOUBLE          : '-'?[0-9]+'.'[0-9]* | '-'?'.'[0-9]+;

// Whitespace and Comments
WS              : [ \t\r\n]+ -> skip ;
LINE_COMMENT    : '//' ~[\r\n]* -> skip;
BLOCK_COMMENT   : '/*' .*? '*/' -> skip;
