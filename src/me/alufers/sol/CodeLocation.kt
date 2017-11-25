package me.alufers.sol

data class CodeLocation(val line: Int, val col: Int, val charOffset: Int, val where: String? = null) {
    override fun toString(): String {
        val whereStr = where ?: "<unknown>"
        return "at $whereStr:$line:$col"
    }
}