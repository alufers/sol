package me.alufers.sol

abstract class Expr {
    interface Visitor<R> {
        fun visitAssignExpr(expr: Assign): R
        fun visitBinaryExpr(expr: Binary): R
        fun visitCallExpr(expr: Call): R
        fun visitGetExpr(expr: Get): R
        fun visitGroupingExpr(expr: Grouping): R
        fun visitLiteralExpr(expr: Literal): R
        fun visitLogicalExpr(expr: Logical): R
        fun visitSetExpr(expr: Set): R
        fun visitSuperExpr(expr: Super): R
        fun visitThisExpr(expr: This): R
        fun visitUnaryExpr(expr: Unary): R
        fun visitVariableExpr(expr: Variable): R
    }

    data class Assign(val name: Token, val value: Expr) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitAssignExpr(this)
        }
    }

    data class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBinaryExpr(this)
        }

        override fun toString(): String {
            return "Binary L=(${left.toString()}) OP=${operator.toString()} R=(${right.toString()}) "
        }
    }

    data class Call(val callee: Expr, val paren: Token, val arguments: List<Expr>) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitCallExpr(this)
        }
    }

    data class Get(val obj: Expr, val name: Token) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitGetExpr(this)
        }
    }

    data class Grouping(val expression: Expr) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitGroupingExpr(this)
        }

        override fun toString(): String {
            return "Grouping (${expression.toString()})"
        }

    }

    data class Literal(val value: Any?) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLiteralExpr(this)
        }

        override fun toString(): String {
            return "Literal (${value.toString()})"
        }

    }

    data class Logical(val left: Expr, val operator: Token, val right: Expr) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLogicalExpr(this)
        }
    }

    data class Set(val obj: Expr, val name: Token, val value: Expr) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitSetExpr(this)
        }
    }

    data class Super(val keyword: Token, val method: Token) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitSuperExpr(this)
        }
    }

    data class This(val keyword: Token) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitThisExpr(this)
        }
    }

    data class Unary(val operator: Token, val right: Expr) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitUnaryExpr(this)
        }

        override fun toString(): String {
            return "Unary OP=${operator.toString()} R=(${right.toString()}) "
        }
    }

    data class Variable(val name: Token) : Expr() {

        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVariableExpr(this)
        }
    }

    internal abstract fun <R> accept(visitor: Visitor<R>): R
}
