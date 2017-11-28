package me.alufers.sol

import java.io.File
import java.nio.charset.Charset


fun runCode(source: String, label: String): Boolean {
    val reporter = ErrorReporter(label)
    val scanner = Scanner(source, reporter)
    val tokens = scanner.scan()
    val start = System.currentTimeMillis()
    if (!reporter.hadError) {
        val parser = Parser(ArrayList(tokens), reporter)
        val statements = parser.parse()
        if (!reporter.hadError) {
            val interpreter = Interpreter(reporter)
            interpreter.interpret(statements ?: ArrayList())
            if (reporter.hadError) {
                return false
            }
        } else {
            return false
        }
    } else {
        return false
    }
    println("finished in " + (System.currentTimeMillis() - start).toString() + "ms")
    return true

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

fun runFromFile(path: String): Boolean {
    val file = File(path)

    return runCode(file.readText(Charset.defaultCharset()), path)
}

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        System.exit(if (runFromFile(args[0])) 0 else 666)
    } else {
        repl()
    }
}