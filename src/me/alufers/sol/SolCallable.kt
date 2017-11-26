package me.alufers.sol

interface SolCallable {
    fun arity(): Int
    fun call(interpreter: Interpreter, arguments: List<Any>): Any?
}