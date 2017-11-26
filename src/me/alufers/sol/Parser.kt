package me.alufers.sol


class Parser(val tokens: ArrayList<Token>, val errorReporter: ErrorReporter) {
    private var currentToken: Int = 0

    fun parse(): ArrayList<Stmt>? {
        try {
            val statements = ArrayList<Stmt>()
            while (!isAtEnd()) {
                statements.add(declaration())
            }
            return statements
        } catch (e: ParseError) {
            errorReporter.reportError(e.message ?: "Unknown error", e.location)
            return null
        }
    }

    fun declaration(): Stmt {
        if (matchToken(TokenType.VAR)) return varDeclaration()
        return statement()
    }

    fun varDeclaration(): Stmt {
        val name = consume(TokenType.IDENTIFIER, "Expected variable name after 'var' keyword")
        var initializer: Expr? = null
        if (matchToken(TokenType.EQUAL)) { // initializer present
            initializer = expression()
        }
        consume(TokenType.SEMICOLON, "Expected ';' after variable declaration")
        return Stmt.Var(name, initializer)

    }

    fun statement(): Stmt {
        if (matchToken(TokenType.PRINT)) return printStatement()
        if (matchToken(TokenType.LEFT_BRACE)) return block()
        if (matchToken(TokenType.IF)) return ifStatement()
        return expressionStatement()
    }

    fun ifStatement(): Stmt {
        val condition = expression()
        val body = controlFlowBody()
        var elseBody: Stmt? = null
        if (matchToken(TokenType.ELSE)) {
            elseBody = controlFlowBody()
        }
        return Stmt.If(condition, body, elseBody)

    }

    fun controlFlowBody(): Stmt {
        return when {
            matchToken(TokenType.LEFT_BRACE) -> block()
            matchToken(TokenType.RETURN) -> throw ParseError("return not yet implemented", peek().location)
            else -> throw ParseError("Expected block or return as control flow body", peek().location)
        }
    }

    fun block(): Stmt {
        val innerStatements = ArrayList<Stmt>()
        while (!checkToken(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            innerStatements.add(declaration())
        }
        consume(TokenType.RIGHT_BRACE, "Expected '}' after block statement")
        return Stmt.Block(innerStatements)
    }

    fun printStatement(): Stmt {
        val value = expression()
        consume(TokenType.SEMICOLON, "Expected ';' after print statement")
        return Stmt.Print(value)
    }

    fun expressionStatement(): Stmt {
        val value = expression()
        consume(TokenType.SEMICOLON, "Expected ';' after expression statement")
        return Stmt.Expression(value)
    }

    /**
     * Parses an expression
     */
    fun expression(): Expr {
        return assignment()
    }

    fun assignment(): Expr {
        val expr = or()
        if (matchToken(TokenType.EQUAL)) {
            return when (expr) {
                is Expr.Variable -> Expr.Assign(expr.name, expression())
                else -> throw ParseError("Invalid assignment target", getLocation())
            }
        }
        return expr
    }

    fun or(): Expr {
        var expr = and()
        while (matchToken(TokenType.OR)) {
            val operator = previous()
            val right = and()
            expr = Expr.Logical(expr, operator, right)
        }
        return expr
    }

    fun and(): Expr {
        var expr = equality()
        while (matchToken(TokenType.AND)) {
            val operator = previous()
            val right = equality()
            expr = Expr.Logical(expr, operator, right)
        }
        return expr
    }

    /**
     * equality → comparison ( ( "!=" | "==" ) comparison )* ;
     * Equality expression consists of one comparsion followed by zero or more comparsions joined by equality operators (==, !=)
     */
    fun equality(): Expr {
        var expr: Expr = comparsion()
        while (matchToken(TokenType.EQUAL_EQUAL, TokenType.BANG_EQUAL)) {
            val operator = previous()
            val right = comparsion()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    /**
     * comparison → addition ( ( ">" | ">=" | "<" | "<=" ) addition )* ;
     * Comparsion expressions consist of one addition followed by zero or more additions joined by comparsion operators (>, <, >=, <=)
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
        var expr: Expr = exponentiation()
        while (matchToken(TokenType.STAR, TokenType.SLASH, TokenType.PERCENT)) {
            val operator = previous()
            val right = exponentiation()

            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    fun exponentiation(): Expr {
        var expr: Expr = unary()
        while (matchToken(TokenType.STAR_STAR)) {
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
            matchToken(TokenType.IDENTIFIER) -> Expr.Variable(previous())
            matchToken(TokenType.LEFT_PAREN) -> {
                val expr = expression()
                consume(TokenType.RIGHT_PAREN, "Expected ')' after expression")
                return Expr.Grouping(expr)
            }
            else -> {
                throw ParseError("Primary expression can't be matched", getLocation())
            }
        }
    }

    private fun synchronize() {
        advance()

        while (!isAtEnd()) {
            if (previous().type === TokenType.SEMICOLON) return

            when (peek().type) {
                TokenType.CLASS, TokenType.FUN, TokenType.VAR, TokenType.FOR, TokenType.IF, TokenType.WHILE, TokenType.PRINT, TokenType.RETURN -> return
            }

            advance()
        }
    }

    fun consume(tt: TokenType, errorMessage: String): Token {
        if (checkToken(tt)) return advance()
        throw ParseError(errorMessage, getLocation())
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
        if (isAtEnd()) return false
        return peek().type == tt
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

    private fun getLocation(): CodeLocation {
        return tokens[currentToken].location
    }

    private fun previous(): Token {
        return tokens[currentToken - 1]
    }
}