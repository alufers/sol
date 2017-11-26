package me.alufers.sol


inline fun <R> measureTiming(name: String, block: () -> R): R {
    val start = System.currentTimeMillis()
    val v = block()
    println("$name:" + (System.currentTimeMillis() - start).toString() + "ms\n")
    return v
}