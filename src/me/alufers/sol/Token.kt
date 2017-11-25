package me.alufers.sol

data class Token(val location: CodeLocation, val type: TokenType, val lexeme: String, val literalValue: Any? = null) {
    override fun toString(): String {
        if(type == TokenType.STRING) {
            return "STRING \"$literalValue\""
        }
        if(type == TokenType.NUMBER) {
            return "NUMBER $literalValue"
        }
        if(type == TokenType.IDENTIFIER) {
            return "IDENTIFIER $literalValue"
        }
        return type.toString()
    }
}