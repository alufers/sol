package me.alufers.sol

import java.util.ArrayList

class BytecodeCompiler(val errorReporter: ErrorReporter) : Expr.Visitor<Unit>, Stmt.Visitor<Unit> {
    val bytecodeBuilder = BytecodeBuilder()

    fun addIns(ins: BytecodeInstruction) {
        bytecodeBuilder.addInstruction(ins)
    }

    fun compile(statements: ArrayList<Stmt>): String {
        try {
            for (stmt in statements) {
                compileStatement(stmt)
            }
        } catch (e: CompileError) {
            errorReporter.reportError(e.message ?: "Unknown error", e.location)
        }
        return "<errored>"
    }

    fun compileExpression(expr: Expr) {
        return expr.accept(this)
    }

    override fun visitAssignExpr(expr: Expr.Assign) {
        compileExpression(expr.value)
        addIns(BytecodeInstruction.AssignVar(expr.name.literalValue as String))
        // no pop, we want the value
    }

    override fun visitBinaryExpr(expr: Expr.Binary) {
        compileExpression(expr.right)
        compileExpression(expr.left)
        when (expr.operator.type) {
            TokenType.EQUAL_EQUAL -> addIns(BytecodeInstruction.BinaryCompareEquals())
            TokenType.BANG_EQUAL -> addIns(BytecodeInstruction.BinaryCompareNotEquals())
            TokenType.PLUS -> addIns(BytecodeInstruction.BinaryAdd())
            TokenType.MINUS -> addIns(BytecodeInstruction.BinarySubstract())
            TokenType.STAR -> addIns(BytecodeInstruction.BinaryMultiply())
            TokenType.STAR_STAR -> addIns(BytecodeInstruction.BinaryExponentiate())
            TokenType.SLASH -> addIns(BytecodeInstruction.BinaryDivide())
            TokenType.LESS -> addIns(BytecodeInstruction.BinaryCompareLess())
            TokenType.LESS_EQUAL -> addIns(BytecodeInstruction.BinaryCompareLessEqual())
            TokenType.GREATER -> addIns(BytecodeInstruction.BinaryCompareGreater())
            TokenType.GREATER_EQUAL -> addIns(BytecodeInstruction.BinaryCompareGreaterEqual())
            TokenType.PERCENT -> addIns(BytecodeInstruction.BinaryModulo())
            else -> throw CompileError("Binary operator ${expr.operator.lexeme} is not supported", expr.operator.location)
        }
        // discard results of left and right expressions
        addIns(BytecodeInstruction.Swap())
        addIns(BytecodeInstruction.Pop())
        addIns(BytecodeInstruction.Swap())
        addIns(BytecodeInstruction.Pop())
    }

    override fun visitCallExpr(expr: Expr.Call) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitGetExpr(expr: Expr.Get) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitGroupingExpr(expr: Expr.Grouping) {
        compileExpression(expr)
    }

    override fun visitLiteralExpr(expr: Expr.Literal) {
        when (expr.value) {
            is Double -> addIns(BytecodeInstruction.PushNumber(expr.value))
            is String -> addIns(BytecodeInstruction.PushString(expr.value))
            is Boolean -> addIns(BytecodeInstruction.PushBoolean(expr.value))
            null -> addIns(BytecodeInstruction.PushNil())
            else -> throw CompileError("Unsupported literal type", expr.location)
        }
    }

    override fun visitLogicalExpr(expr: Expr.Logical) {
        TODO("XD")
    }

    override fun visitSetExpr(expr: Expr.Set) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitSuperExpr(expr: Expr.Super) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitThisExpr(expr: Expr.This) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitUnaryExpr(expr: Expr.Unary) {
        when (expr.operator.type) {
            TokenType.MINUS -> addIns(BytecodeInstruction.UnaryNumericalNegate())
            TokenType.BANG -> addIns(BytecodeInstruction.UnaryLogicalNegate())
        }
    }

    override fun visitVariableExpr(expr: Expr.Variable) {
        addIns(BytecodeInstruction.LoadVar(expr.name.literalValue as String))
    }

    override fun visitPostfixExpr(expr: Expr.Postfix) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun compileStatement(stmt: Stmt) {
        return stmt.accept(this)
    }

    override fun visitBlockStmt(stmt: Stmt.Block) {
        addIns(BytecodeInstruction.PushBlock())
        for (innerStmt in stmt.statements) {
            compileStatement(innerStmt)
        }
        addIns(BytecodeInstruction.PopBlock())
    }

    override fun visitClassStmt(stmt: Stmt.Class) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitExpressionStmt(stmt: Stmt.Expression) {
        compileExpression(stmt.expression)
        addIns(BytecodeInstruction.Pop())
    }

    override fun visitFunctionStmt(stmt: Stmt.Function) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitIfStmt(stmt: Stmt.If) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitPrintStmt(stmt: Stmt.Print) {
        compileExpression(stmt.expression)
        addIns(BytecodeInstruction.NoOp())
        addIns(BytecodeInstruction.Pop())
    }

    override fun visitReturnStmt(stmt: Stmt.Return) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitBreakStmt(stmt: Stmt.Break) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitMutDeclarationStmt(stmt: Stmt.MutDeclaration) {
        addIns(BytecodeInstruction.DefineVar(stmt.name.literalValue as String))
        if (stmt.initializer !== null) {
            compileExpression(stmt.initializer)
            addIns(BytecodeInstruction.AssignVar(stmt.name.literalValue as String))
            addIns(BytecodeInstruction.Pop())
        }
    }

    override fun visitConstDeclarationStmt(stmt: Stmt.ConstDeclaration) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun visitWhileStmt(stmt: Stmt.While) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}