package me.alufers.sol.tool

import java.io.IOException
import java.io.PrintWriter
import java.util.Arrays


fun main(args: Array<String>) {
    if (args.size != 1) {
        System.err.println("Usage: generate_ast <output directory>")
        System.exit(1)
    }
    val outputDir = args[0]

    defineAst(outputDir, "Expr", Arrays.asList(

            "Assign   : Token name, Expr value",
            "Binary   : Expr left, Token operator, Expr right",
            "Call     : Expr callee, Token paren, List<Expr> arguments",
            "Get      : Expr obj, Token name",
            "Grouping : Expr expression",
            "Literal  : Object value",
            "Logical  : Expr left, Token operator, Expr right",
            "Set      : Expr obj, Token name, Expr value",
            "Super    : Token keyword, Token method",
            "This     : Token keyword",
            "Unary    : Token operator, Expr right",
            "Variable : Token name"

    ))


    defineAst(outputDir, "Stmt", Arrays.asList(

            "Block      : List<Stmt> statements",
            "Class      : Token name, Expr superclass, List<Stmt.Function> methods",
            "Expression : Expr expression",
            "Function   : Token name, List<Token> parameters, List<Stmt> body",
            "If         : Expr condition, Stmt thenBranch, Stmt elseBranch",
            "Print      : Expr expression",
            "Return     : Token keyword, Expr value",
            "MutDeclaration        : Token name, Expr initializer",
            "While      : Expr condition, Stmt body"

    ))


}


@Throws(IOException::class)
private fun defineAst(
        outputDir: String, baseName: String, types: List<String>) {
    val path = "$outputDir/$baseName.kt"
    val writer = PrintWriter(path, "UTF-8")

    writer.println("package me.alufers")
    writer.println("")
    writer.println("abstract open class $baseName {")


    defineVisitor(writer, baseName, types)


    // The AST classes.
    for (type in types) {
        val className = type.split(":")[0].trim()
        val fields = type.split(":")[1].trim()
        defineType(writer, baseName, className, fields)
    }

    // The base accept() method.
    writer.println("")
    writer.println("  internal abstract fun <R> accept(visitor: Visitor<R>): R")


    writer.println("}")
    writer.close()
}


private fun defineVisitor(
        writer: PrintWriter, baseName: String, types: List<String>) {
    writer.println("  interface Visitor<R> {")

    types
            .map { it.split(":")[0].trim() }
            .forEach { writer.println("fun visit$it$baseName (${baseName.toLowerCase()}: $it): R") }

    writer.println("  }")
}


private fun defineType(
        writer: PrintWriter, baseName: String,
        className: String, fieldList: String) {
    writer.println("")
    val fieldDecls = fieldList.split(", ")
            .map { "val ${it.split(" ").reversed().joinToString(":")}" }
            .joinToString(",")
    writer.println("  public data class $className($fieldDecls) : $baseName() {")


    // Visitor pattern.
    writer.println()
    writer.println("    override fun <R> accept(visitor: Visitor<R> ): R {")
    writer.println("      return visitor.visit$className$baseName(this);")
    writer.println("    }")



    writer.println("  }")
}
