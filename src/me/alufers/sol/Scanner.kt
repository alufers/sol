package me.alufers.sol


class Scanner(val source: String, val errorReporter: ErrorReporter) {
    val tokens = ArrayList<Token>()

    var line: Int = 1 // current line
    var col: Int = 1 // current column
    var currentChar: Int = 0 // char offset from the start of the source
    var start: Int = 0 // start of the current lexeme (from start of the source)

    private var keywordTokens =  HashMap<String, TokenType>()

    fun prepare() {
        for (tt in TokenType.values().filter { it.isKeyword }) {
            val k = tt.lexeme ?: continue
            keywordTokens[k] = tt
        }
    }

    fun scan(): List<Token> {
        prepare()
        while (!isAtEnd()) {
            start = currentChar
            scanToken()
            if (errorReporter.hadError) {
                break
            }
        }
        tokens.add(Token(getLocation(), TokenType.EOF, ""))
        return tokens
    }


    fun scanToken() {
        val c = advance()
        when (c) {
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            '.' -> addToken(TokenType.DOT)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            ';' -> addToken(TokenType.SEMICOLON)
            '/' -> {
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) advance()
                } else {
                    addToken(TokenType.SLASH)
                }
            }
            '*' -> addToken(if (match('*')) TokenType.STAR_STAR else TokenType.STAR)
            '%' -> addToken(TokenType.PERCENT)
            '!' -> addToken(if (match('=')) TokenType.BANG_EQUAL else TokenType.BANG)
            '>' -> addToken(if (match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)
            '<' -> addToken(if (match('=')) TokenType.LESS_EQUAL else TokenType.LESS)
            '=' -> addToken(if (match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL)
            '"' -> consumeStringLiteral()
            '\n', '\r', '\t', ' ' -> {
            }
            else -> {
                if (c in '0'..'9') {
                    consumeNumberLiteral()
                } else if (isAlpha(c)) {
                    consumeIdentifierOrKeyword()
                } else {
                    errorReporter.reportError("Unexpected character $c", getLocation())
                }

            }
        }
    }

    fun consumeIdentifierOrKeyword() {

        while (isAlphaNumeric(peek()) && !isAtEnd()) {
            advance()
        }
        val value = source.substring(start, currentChar)
        if(keywordTokens.contains(value)) {
            addToken(keywordTokens[value] ?: TokenType.IDENTIFIER)
            return
        }
        addToken(TokenType.IDENTIFIER, value)
    }

    fun consumeNumberLiteral() {
        while (peek() in '0'..'9' || (peek() == '.' && peekNext() in '0'..'9')) {
            advance()
        }
        addToken(TokenType.NUMBER, source.substring(start, currentChar).toDouble())
    }

    fun consumeStringLiteral() {
        val sb = StringBuilder()
        while (peek() != '"' && !isAtEnd()) {
            val c = advance()
            if (c == '\\') { // escape sequence
                when {
                    match('"') -> sb.append('"')
                    match('\\') -> sb.append('\\')
                    match('n') -> sb.append('\n')
                    match('t') -> sb.append('\t')
                    else -> {
                        errorReporter.reportError("Illegal escape sequence '\\$c'", getLocation())
                        return
                    }
                }
            } else {
                sb.append(c)
            }
        }
        if (isAtEnd()) {
            errorReporter.reportError("Unterminated string", getLocation())
            return
        }
        advance()
        addToken(TokenType.STRING, sb.toString())
    }

    fun isAlpha(c: Char): Boolean {
        return c in 'A'..'Z' || c in 'a'..'z' || c == '_'
    }

    fun isAlphaNumeric(c: Char): Boolean {
        return isAlpha(c) || c in '0'..'9'
    }

    fun addToken(type: TokenType, literal: Any? = null) {
        tokens.add(Token(getLocation(), type, source.substring(start, currentChar), literal))
    }

    fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[currentChar] != expected) return false
        advance()
        return true
    }

    fun advance(): Char {
        currentChar++
        col++
        val c = source[currentChar - 1]
        if (c == '\n') {
            col = 1
            line++
        }
        return c
    }

    fun peek(): Char {
        if (isAtEnd()) return '\u0000'
        return source[currentChar]
    }

    fun peekNext(): Char {
        if (currentChar + 1 >= source.length) return '\u0000'
        return source[currentChar + 1]
    }

    fun getLocation(): CodeLocation {
        return CodeLocation(line, col, currentChar)
    }

    private fun isAtEnd(): Boolean {
        return currentChar >= source.length
    }
}