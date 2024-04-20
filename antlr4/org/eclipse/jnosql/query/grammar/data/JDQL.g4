grammar JDQL;

statement : select_statement | update_statement | delete_statement;

select_statement : select_clause? from_clause? where_clause? orderby_clause?;
update_statement : 'UPDATE' entity_name set_clause where_clause?;
delete_statement : 'DELETE' from_clause where_clause?;

from_clause : 'FROM' entity_name;

where_clause : 'WHERE' conditional_expression;

set_clause : 'SET' update_item (',' update_item)*;
update_item : state_field_path_expression '=' (scalar_expression | 'NULL');

select_clause : 'SELECT' select_list;
select_list
    : state_field_path_expression
    | aggregate_expression
    ;
aggregate_expression : 'COUNT' '(' 'THIS' ')';

orderby_clause : 'ORDER' 'BY' orderby_item (',' orderby_item)*;
orderby_item : state_field_path_expression ('ASC' | 'DESC');

conditional_expression
    // highest to lowest precedence
    : '(' conditional_expression ')'
    | null_comparison_expression
    | in_expression
    | between_expression
    | like_expression
    | comparison_expression
    | 'NOT' conditional_expression
    | conditional_expression 'AND' conditional_expression
    | conditional_expression 'OR' conditional_expression
    ;

comparison_expression : scalar_expression ('=' | '>' | '>=' | '<' | '<=' | '<>') scalar_expression;
between_expression : scalar_expression 'NOT'? 'BETWEEN' scalar_expression 'AND' scalar_expression;
like_expression : scalar_expression 'NOT'? 'LIKE' STRING;

in_expression : state_field_path_expression 'NOT'? 'IN' '(' in_item (',' in_item)* ')';
in_item : literal | enum_literal | input_parameter; // could simplify to just literal

null_comparison_expression : state_field_path_expression 'IS' 'NOT'? 'NULL';

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

