package me.alufers.sol

import java.util.ArrayList



class Interpreter(val errorReporter: ErrorReporter) : Expr.Visitor<Any?>, Stmt.Visitor<Unit> {
    var environment = Environment()

    fun interpret(statements: ArrayList<Stmt>): String {
        try {
            for (stmt in statements) {
                execute(stmt)
            }
        } catch (e: RuntimeError) {
            errorReporter.reportError(e.message ?: "Unknown error", e.location)
        }
        return "<errored>"
    }

    fun execute(stmt: Stmt) {
        stmt.accept(this)
    }

    override fun visitBlockStmt(stmt: Stmt.Block) {
        try {
            environment = Environment(environment) // create a new environment connected to the old one
            for (innerStmt in stmt.statements) {
                execute(innerStmt)
            }
        } finally {
            environment = environment.parent ?: throw IllegalStateException("Tried to exit global environment") // move upwards
        }
    }

    override fun visitClassStmt(stmt: Stmt.Class) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitExpressionStmt(stmt: Stmt.Expression) {
        evaluate(stmt.expression)
    }

    override fun visitFunctionStmt(stmt: Stmt.Function) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitIfStmt(stmt: Stmt.If) {
        val shouldGo = isTruthy(evaluate(stmt.condition))
        if (shouldGo) {
            execute(stmt.thenBranch)
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch)
        }
    }

    override fun visitPrintStmt(stmt: Stmt.Print) {
        println(stringify(evaluate(stmt.expression)))
    }

    override fun visitReturnStmt(stmt: Stmt.Return) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitMutDeclarationStmt(stmt: Stmt.MutDeclaration) {
        try {
            environment.define(stmt.name.literalValue as String, if (stmt.initializer != null) evaluate(stmt.initializer) else null)
        } catch (e: Environment.ValueAlreadyDefinedError) {
            throw RuntimeError(e.message ?: "ValueAlreadyDefinedError", stmt.name.location)
        }
    }

    override fun visitWhileStmt(stmt: Stmt.While) {
        while(isTruthy(evaluate(stmt.condition))) {
            execute(stmt.body)
        }
    }


    private fun evaluate(expr: Expr): Any? {
        return expr.accept(this)
    }

    private fun isTruthy(value: Any?): Boolean {
        if (value == null) return false
        if (value == false) return false
        return true
    }

    private fun stringify(value: Any?): String {
        return when (value) {
            null -> "nil"
            is Double -> if (value.toString().endsWith(".0")) value.toString().dropLast(2) else value.toString()
            else -> value.toString()
        }
    }

    private fun typeName(value: Any?): String {
        return when (value) {
            null -> "nil"
            is Double -> "number"
            is String -> "string"
            is Boolean -> "boolean"
            else -> "object"
        }
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Any? {
        return expr.value
    }

    override fun visitAssignExpr(expr: Expr.Assign): Any? {
        val value = evaluate(expr.value)
        try {
            environment.set(expr.name.literalValue as String, value)
        } catch (e: Environment.ValueNotDefinedError) {
            throw RuntimeError(e.message ?: "ValueNotDefinedError", expr.name.location)
        }
        return value
    }

    override fun visitBinaryExpr(expr: Expr.Binary): Any? {
        val right = evaluate(expr.right)
        val left = evaluate(expr.left)
        return when (expr.operator.type) {
            TokenType.EQUAL_EQUAL -> left == right
            TokenType.BANG_EQUAL -> left != right
            TokenType.PLUS -> if (left is Double && right is Double) left + right else stringify(left) + stringify(right)
            else -> if (right is Double && left is Double) {
                when (expr.operator.type) {
                    TokenType.LESS -> left < right
                    TokenType.LESS_EQUAL -> left <= right
                    TokenType.GREATER -> left > right
                    TokenType.GREATER_EQUAL -> left >= right
                    TokenType.MINUS -> left - right
                    TokenType.STAR -> left * right
                    TokenType.STAR_STAR -> Math.pow(left, right)
                    TokenType.SLASH -> left / right
                    TokenType.PERCENT -> left % right
                    else -> throw RuntimeError("Binary operator ${expr.operator.lexeme} for types ${typeName(left)} and ${typeName(right)} is not supported.", expr.operator.location)
                }
            } else {
                throw RuntimeError("Binary operator ${expr.operator.lexeme} for types ${typeName(left)} and ${typeName(right)} is not supported.", expr.operator.location)
            }

        }
    }

    override fun visitCallExpr(expr: Expr.Call): Any? {
        val callee = evaluate(expr.callee)
        val arguments = expr.arguments.map { evaluate(it)!! }
        val function = callee as SolCallable
        if (callee !is SolCallable) {
            throw RuntimeError("Can only call functions and classes.",expr.paren.location)
        }
        if (arguments.size !== function.arity()) {
            throw RuntimeError( "Expected " +
                    function.arity() + " arguments but got " +
                    arguments.size + ".",expr.paren.location)
        }
        return function.call(this, arguments)
    }

    override fun visitGetExpr(expr: Expr.Get): Any? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Any? {
        return evaluate(expr.expression)
    }


    override fun visitLogicalExpr(expr: Expr.Logical): Any? {
        val left = evaluate(expr.left)
        when {
            expr.operator.type === TokenType.OR -> if (isTruthy(left)) return left
            else -> if (!isTruthy(left)) return left
        }
        return evaluate(expr.right)
    }

    override fun visitSetExpr(expr: Expr.Set): Any? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitSuperExpr(expr: Expr.Super): Any? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitThisExpr(expr: Expr.This): Any? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitUnaryExpr(expr: Expr.Unary): Any? {
        val right = evaluate(expr.right)
        return when (expr.operator.type) {
            TokenType.BANG -> !isTruthy(expr.right)
            TokenType.MINUS -> if (right is Double) {
                -right
            } else {
                throw RuntimeError("Can't negate a non-number", expr.operator.location)
            }
            else -> throw RuntimeError("Unsupported unary operator ${expr.operator.type.lexeme}.", expr.operator.location)

        }
    }

    override fun visitVariableExpr(expr: Expr.Variable): Any? {
        try {
            return environment.get(expr.name.literalValue as String)
        } catch (e: Environment.ValueNotDefinedError) {
            throw RuntimeError(e.message ?: "ValueNotDefinedError", expr.name.location)
        }
    }
}
