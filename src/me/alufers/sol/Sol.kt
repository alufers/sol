package me.alufers.sol

import java.io.File
import java.nio.charset.Charset


fun runCode(source: String, label: String) {
    val reporter = ErrorReporter(label)
    val scanner = Scanner(source, reporter)
    val tokens = scanner.scan()

    if (!reporter.hadError) {
        val parser = Parser(ArrayList(tokens), reporter)
        val expr = parser.expression()
        println(expr)
    }
}

fun repl() {
    println("Sol REPL v0.1")
    println("")
    while (true) {
        val line = readLine()
        print(">")
        if (line == null) {
            continue
        }
        runCode(line, "<REPL>")
    }
}

fun runFromFile(path: String) {
    val file = File(path)

    runCode(file.readText(Charset.defaultCharset()), path)
}

fun main(args: Array<String>) {
    if (args.size > 0) {
        runFromFile(args[0])
    } else {
        repl()
    }
}