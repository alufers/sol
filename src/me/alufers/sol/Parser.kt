package me.alufers.sol



class Parser(val tokens: ArrayList<Token>, val errorReporter: ErrorReporter) {
    private var currentToken: Int = 0
    fun expression(): Expr {
        return equality()
    }

    /**
     * equality → comparison ( ( "!=" | "==" ) comparison )* ;
     * Equality expression consists of one comparsion followed by zero or more comparsions joined by equality operators (==, !=)
     */
    fun equality(): Expr {
        var expr: Expr = comparsion()
        while (matchToken(TokenType.EQUAL, TokenType.BANG_EQUAL)) {
            val right = comparsion()
            val operator = previous()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    /**
     * comparison → addition ( ( ">" | ">=" | "<" | "<=" ) addition )* ;
     * Comparsion expressions consist of one addition followed by zerro or more additions joined by comparsion operators (>, <, >=, <=)
     */
    fun comparsion(): Expr {
        var expr: Expr = addition()
        while (matchToken(TokenType.LESS, TokenType.GREATER, TokenType.LESS_EQUAL, TokenType.GREATER_EQUAL)) {
            val operator = previous()
            val right = addition()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    fun addition(): Expr {
        var expr: Expr = multiplication()
        while (matchToken(TokenType.PLUS, TokenType.MINUS)) {
            val operator = previous()
            val right = multiplication()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    fun multiplication(): Expr {
        var expr: Expr = unary()
        while (matchToken(TokenType.STAR, TokenType.SLASH)) {
            val operator = previous()
            val right = unary()

            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    fun unary(): Expr {

        if (matchToken(TokenType.BANG, TokenType.MINUS)) {

            val operator = previous()
            val right = unary()
            return Expr.Unary(operator, right)
        }
        return primary()
    }

    fun primary(): Expr {
        return when {
            matchToken(TokenType.TRUE) -> Expr.Literal(true)
            matchToken(TokenType.FALSE) -> Expr.Literal(false)
            matchToken(TokenType.NIL) -> Expr.Literal(null)
            matchToken(TokenType.NUMBER, TokenType.STRING) -> Expr.Literal(previous().literalValue)
            matchToken(TokenType.LEFT_PAREN) -> {
                val expr = expression()
                consume(TokenType.RIGHT_PAREN, "Expected ')' after expression")
                return Expr.Grouping(expr)
            }
            else -> {
                throw IllegalStateException("Primary expression can't be matched")
            }
        }
    }


    fun consume(tt: TokenType, errorMessage: String): Token {
        if (checkToken(tt)) return advance()
        throw IllegalStateException(errorMessage)
    }

    fun matchToken(vararg toMatch: TokenType): Boolean {
        for (tt in toMatch) {
            if (checkToken(tt)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun checkToken(tt: TokenType): Boolean {
        if (isAtEnd()) return false;
        return peek().type == tt;
    }

    private fun advance(): Token {
        if (!isAtEnd()) currentToken++
        return previous()
    }

    private fun isAtEnd(): Boolean {
        return peek().type === TokenType.EOF
    }

    private fun peek(): Token {
        return tokens[currentToken]
    }

    private fun previous(): Token {
        return tokens[currentToken - 1]
    }
}