package me.alufers.sol

class ParseError(message: String, val location: CodeLocation) : Throwable(message) {
}