package me.alufers.sol

import me.alufers.sol.BytecodeInstruction
import sun.plugin.dom.exception.InvalidStateException

class BytecodeReference(private var ins: BytecodeInstruction? = null) {
    private var didSetIns = false
    var resolvedAddress: Int? = null

    init {
        val refIns = ins
        if (refIns != null) {
            refIns.references.add(this)
            didSetIns = true
        }

    }

    fun setInstruction(refIns: BytecodeInstruction) {
        if (didSetIns) throw InvalidStateException("Cannot set refererenced instruction twice!")
        ins = refIns
        refIns.references.add(this)
        didSetIns = true
    }



}