package me.alufers.sol


inline fun measureTiming(name:String, block: () -> Unit)  {
    val start = System.currentTimeMillis()
    block()
    println("$name:" + (System.currentTimeMillis() - start).toString() + "ms\n")
}