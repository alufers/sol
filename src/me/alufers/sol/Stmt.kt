package me.alufers.sol

abstract class Stmt {
    interface Visitor<R> {
        fun visitBlockStmt(stmt: Block): R
        fun visitClassStmt(stmt: Class): R
        fun visitExpressionStmt(stmt: Expression): R
        fun visitFunctionStmt(stmt: Function): R
        fun visitIfStmt(stmt: If): R
        fun visitPrintStmt(stmt: Print): R
        fun visitReturnStmt(stmt: Return): R
        fun visitVarStmt(stmt: Var): R
        fun visitWhileStmt(stmt: While): R
    }

    data class Block(val statements: List<Stmt>) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBlockStmt(this)
        }

        override fun toString(): String {
            return statements.joinToString()
        }
    }

    data class Class(val name: Token, val superclass: Expr, val methods: List<Function>) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitClassStmt(this)
        }
    }

    data class Expression(val expression: Expr) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitExpressionStmt(this)
        }

        override fun toString(): String {
            return "Expression ($expression)"
        }
    }

    data class Function(val name: Token, val parameters: List<Token>, val body: List<Stmt>) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitFunctionStmt(this)
        }
    }

    data class If(val condition: Expr, val thenBranch: Stmt, val elseBranch: Stmt?) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitIfStmt(this)
        }

        override fun toString(): String {
            return "If C=($condition) T=($thenBranch) E=($elseBranch)"
        }
    }

    data class Print(val expression: Expr) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitPrintStmt(this)
        }

        override fun toString(): String {
            return "Print ($expression)"
        }
    }

    data class Return(val keyword: Token, val value: Expr) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitReturnStmt(this)
        }
    }

    data class Var(val name: Token, val initializer: Expr?) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVarStmt(this)
        }
    }

    data class While(val condition: Expr, val body: Stmt) : Stmt() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitWhileStmt(this)
        }
    }

    internal abstract fun <R> accept(visitor: Visitor<R>): R
}
