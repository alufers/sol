package me.alufers.sol

class CompileError(message: String, val location: CodeLocation) : Throwable(message) {
}