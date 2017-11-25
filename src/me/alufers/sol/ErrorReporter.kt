package me.alufers.sol


class ErrorReporter(val reporterLabel: String) {
    var hadError = false
    fun reportError(error: String) {
        println("[ERROR] [$reporterLabel] $error")
        hadError = true
    }

    fun reportError(error: String, location: CodeLocation) {
        println("[ERROR] [$reporterLabel] $error $location")
        hadError = true
    }
}