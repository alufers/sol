package me.alufers.sol

class RuntimeError(message: String, val location: CodeLocation) : Throwable(message) {
}