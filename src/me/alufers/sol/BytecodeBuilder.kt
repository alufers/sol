package me.alufers.sol

import kotlin.reflect.full.declaredMemberProperties

class BytecodeBuilder {
    val instructions = ArrayList<BytecodeInstruction>()

    fun addInstruction(instruction: BytecodeInstruction) {
        instructions.add(instruction)
    }

    fun debugPrintBytecode() {
        for (ins in instructions) {
            val opCode = "0x" + java.lang.Integer.toHexString(ins.opCode.toInt()).padStart(2, '0')
            var props = ""
            for(field in ins::class.declaredMemberProperties) {
                props += " ${field.name} = ${field.getter.call(ins)}"
            }
            println("${opCode} - ${ins::class.simpleName}${props}")

        }
    }
}