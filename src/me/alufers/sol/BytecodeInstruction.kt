package me.alufers.sol

abstract class BytecodeInstruction(val opCode: Byte) {

    val references = ArrayList<BytecodeReference>()
    abstract fun serializeToBinary(bb: BinaryBuilder)

    open class SimpleInstruction(opCode: Byte) : BytecodeInstruction(opCode) {
        override fun serializeToBinary(bb: BinaryBuilder) {
            bb.putByte(opCode)
        }
    }

    /**
     * Does nothing.
     */
    /**/ class NoOp() : SimpleInstruction(0x00)

    /**
     * Adds a number literal onto the stack
     */
    data class PushNumber(val value: Double) : BytecodeInstruction(0x01) {
        override fun serializeToBinary(bb: BinaryBuilder) {
            bb.putByte(opCode)
            bb.putDouble(value)
        }
    }

    /**
     * Adds a string literal onto the stack
     */
    data class PushString(val value: String) : BytecodeInstruction(0x02) {
        override fun serializeToBinary(bb: BinaryBuilder) {
            bb.putByte(opCode)
            bb.putString(value)
        }
    }

    /**
     * Adds a boolean literal onto the stack
     */
    data class PushBoolean(val value: Boolean) : BytecodeInstruction(0x03) {
        override fun serializeToBinary(bb: BinaryBuilder) {
            bb.putByte(opCode)
            bb.putBoolean(value)
        }
    }

    /**
     * Adds a nil onto the stack.
     */
    /**/ class PushNil() : SimpleInstruction(0x04)

    /**
     * Pops elements from the stack
     */
    /**/ class Pop() : SimpleInstruction(0x05)

    /**
     * Defines a new variable in the current environment
     */
    data class DefineVar(val name: String) : BytecodeInstruction(0x06) {
        override fun serializeToBinary(bb: BinaryBuilder) {
            bb.putByte(opCode)
            bb.putString(name)
        }
    }

    /**
     * Assigns a value from the stack to a variable in the current environment
     */
    data class AssignVar(val name: String) : BytecodeInstruction(0x07) {
        override fun serializeToBinary(bb: BinaryBuilder) {
            bb.putByte(opCode)
            bb.putString(name)
        }
    }

    /**
     * Loads a variable from the current environment and places it on the stack
     */
    data class LoadVar(val name: String) : BytecodeInstruction(0x08) {
        override fun serializeToBinary(bb: BinaryBuilder) {
            bb.putByte(opCode)
            bb.putString(name)
        }
    }

    /**
     * Evaluates TOS1 + TOS and pushes the result on the stack
     */
    /**/ class BinaryAdd() : SimpleInstruction(0x09)

    /**
     *  Evaluates TOS1 - TOS  and pushes the result on the stack
     */
    /**/ class BinarySubstract() : SimpleInstruction(0x0A)

    /**
     *  Evaluates TOS1 * TOS  and pushes the result on the stack
     */
    /**/ class BinaryMultiply() : SimpleInstruction(0x0B)

    /**
     *  Evaluates TOS1 / TOS  and pushes the result on the stack
     */
    /**/ class BinaryDivide() : SimpleInstruction(0x0C)

    /**
     *  Evaluates TOS1 % TOS  and pushes the result on the stack
     */
    /**/ class BinaryModulo() : SimpleInstruction(0x0D)

    /**
     *  Evaluates TOS1 ** TOS  and pushes the result on the stack
     */
    /**/ class BinaryExponentiate() : SimpleInstruction(0x0E)

    /**
     *  Evaluates  -TOS and pushes the result on the stack
     */
    /**/ class UnaryNumericalNegate() : SimpleInstruction(0x0F)

    /**
     *  Evaluates !TOS and pushes the result on the stack
     */
    /**/ class UnaryLogicalNegate() : SimpleInstruction(0x10)

    /**
     *  Creates a new block with an environment and pushes it on the block stack
     */
    /**/ class PushBlock() : SimpleInstruction(0x11)

    /**
     *  Pops a block from the block stack
     */
    /**/ class PopBlock() : SimpleInstruction(0x12)

    /**
     * Swaps TOS and TOS1
     */
    /**/ class Swap() : SimpleInstruction(0x13)

    /**
     * Evaluates TOS1 == TOS  and pushes the result on the stack
     */
    /**/ class BinaryCompareEquals() : SimpleInstruction(0x14)

    /**
     * Evaluates TOS1 != TOS  and pushes the result on the stack
     */
    /**/ class BinaryCompareNotEquals() : SimpleInstruction(0x15)

    /**
     * Evaluates TOS1 > TOS  and pushes the result on the stack
     */
    /**/ class BinaryCompareGreater() : SimpleInstruction(0x16)

    /**
     * Evaluates TOS1 >= TOS  and pushes the result on the stack
     */
    /**/ class BinaryCompareGreaterEqual() : SimpleInstruction(0x17)

    /**
     * Evaluates TOS1 < TOS  and pushes the result on the stack
     */
    /**/ class BinaryCompareLess() : SimpleInstruction(0x18)

    /**
     * Evaluates TOS1 <= TOS  and pushes the result on the stack
     */
    /**/ class BinaryCompareLessEqual() : SimpleInstruction(0x19)

    /**
     * Jumps to DEST when TOS is truthy
     */
    data class JumpTruthy(val dest: BytecodeReference) : BytecodeInstruction(0x1A) {
        override fun serializeToBinary(bb: BinaryBuilder) {
            bb.putByte(opCode)
            bb.putInt(dest.resolvedAddress ?: 0)
        }
    }

    /**
     * Jumps to DEST when TOS is not truthy
     */
    data class JumpNotTruthy(val dest: BytecodeReference) : BytecodeInstruction(0x1B) {
        override fun serializeToBinary(bb: BinaryBuilder) {
            bb.putByte(opCode)
            bb.putInt(dest.resolvedAddress ?: 0)
        }
    }

    /**
     * Jumps to DEST
     */
    data class Jump(val dest: BytecodeReference) : BytecodeInstruction(0x1C) {
        override fun serializeToBinary(bb: BinaryBuilder) {
            bb.putByte(opCode)
            bb.putInt(dest.resolvedAddress ?: 0)
        }
    }

    /**
     * Increments TOS and pushes the result onto the stack
     */
    /**/ class Increment() : SimpleInstruction(0x1D)

    /**
     * Decrements TOS and pushes the result onto the stack
     */
    /**/ class Decrement() : SimpleInstruction(0x1E)


}