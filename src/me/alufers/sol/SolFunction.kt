package me.alufers.sol

class SolFunction(val declaration: Stmt.Function) : SolCallable {
    override fun arity(): Int {
        return declaration.parameters.size
    }

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any? {
        try {
            interpreter.visitFunctionBlockStmt(declaration.body as Stmt.Block) { parent ->
                val newEnv = Environment(parent)
                declaration.parameters.forEachIndexed({ index, param ->
                    newEnv.define(param.literalValue as String, arguments[index])
                })
                newEnv
            }
        } catch(e: Interpreter.ReturnExeption) {
            return e.value
        }

        return null
    }
}