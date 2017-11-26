package me.alufers.sol

enum class TokenType(val lexeme: String?, val isKeyword: Boolean = false) {
    // Single-character tokens.
    LEFT_PAREN("("),
    RIGHT_PAREN(")"),
    LEFT_BRACE("{"),
    RIGHT_BRACE("}"),
    COMMA(","),
    DOT("."),
    MINUS("-"),
    PLUS("+"),
    SEMICOLON(";"),
    SLASH("/"),
    STAR("*"),
    STAR_STAR("**"),
    PERCENT("%"),

    // One or two character tokens.
    BANG("!"),
    BANG_EQUAL("!="),
    EQUAL("="), EQUAL_EQUAL("=="),
    GREATER(">"), GREATER_EQUAL(">="),
    LESS("<"), LESS_EQUAL("<="),

    // Literals.
    IDENTIFIER(null),
    STRING(null), NUMBER(null),

    // Keywords.
    AND("and", true),
    CLASS("class", true),
    ELSE("else", true),
    FALSE("false", true),
    FUN("fun", true),
    FOR("for", true),
    IF("if", true),
    NIL("nil", true),
    OR("or", true),
    PRINT("print", true),
    RETURN("return", true),
    BREAK("break", true),
    SUPER("super", true),
    THIS("this", true),
    TRUE("true", true),
    MUT("mut", true),
    WHILE("while", true),

    EOF(null);

    override fun toString(): String {
        if (isKeyword) {
            return "KEYWORD $lexeme"
        }
        if (lexeme != null) {
            return "'$lexeme'"
        }
        return name
    }
}