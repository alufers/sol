package me.alufers.sol

import java.nio.ByteBuffer
import kotlin.reflect.full.declaredMemberProperties

class BytecodeBuilder {
    val instructions = ArrayList<BytecodeInstruction>()
    val refsForNextInstruction = ArrayList<BytecodeReference>()

    fun addInstruction(instruction: BytecodeInstruction) {
        instructions.add(instruction)
        refsForNextInstruction.forEach { it.setInstruction(instruction) }
        refsForNextInstruction.clear()
    }

    fun addRefForNextInstruction(ref: BytecodeReference) {
        refsForNextInstruction.add(ref)
    }

    fun resolveReferences() {
        val bb = BinaryBuilder()
        instructions.forEach { ins ->
            ins.references.forEach { ref ->
                ref.resolvedAddress = bb.byteStream.size()
            }
            ins.serializeToBinary(bb)
        }
    }

    fun debugPrintBytecode() {
        for (ins in instructions) {
            val opCode = "0x" + java.lang.Integer.toHexString(ins.opCode.toInt()).padStart(2, '0')
            var props = ""
            for (field in ins::class.declaredMemberProperties) {
                props += " ${field.name} = ${field.getter.call(ins)}"
            }
            println("${opCode} - ${ins::class.simpleName}${props}")

        }
    }

    fun buildBytecode(): ByteArray {
        val bb = BinaryBuilder()
        for (ins in instructions) {
            ins.serializeToBinary(bb)
        }
        return bb.byteStream.toByteArray();
    }
}