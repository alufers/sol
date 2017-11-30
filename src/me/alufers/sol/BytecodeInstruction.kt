package me.alufers.sol

abstract class BytecodeInstruction(val opCode: Byte) {
    /**
     * Does nothing.
     */
    /**/ class NoOp() : BytecodeInstruction(0x00)

    /**
     * Adds a number literal onto the stack
     */
    data class PushNumber(val value: Double) : BytecodeInstruction(0x01)

    /**
     * Adds a string literal onto the stack
     */
    data class PushString(val value: String) : BytecodeInstruction(0x02)

    /**
     * Adds a boolean literal onto the stack
     */
    data class PushBoolean(val value: Boolean) : BytecodeInstruction(0x03)

    /**
     * Adds a nil onto the stack.
     */
    /**/ class PushNil() : BytecodeInstruction(0x04)

    /**
     * Pops elements from the stack
     */
    /**/ class Pop() : BytecodeInstruction(0x05)

    /**
     * Defines a new variable in the current environment
     */
    data class DefineVar(val name: String) : BytecodeInstruction(0x06)

    /**
     * Assigns a value from the stack to a variable in the current environment
     */
    data class AssignVar(val name: String) : BytecodeInstruction(0x07)

    /**
     * Loads a variable from the current environment and places it on the stack
     */
    data class LoadVar(val name: String) : BytecodeInstruction(0x08)

    /**
     * Evaluates TOS1 + TOS and pushes the result on the stack
     */
    /**/ class BinaryAdd() : BytecodeInstruction(0x09)

    /**
     *  Evaluates TOS1 - TOS  and pushes the result on the stack
     */
    /**/ class BinarySubstract() : BytecodeInstruction(0x0A)

    /**
     *  Evaluates TOS1 * TOS  and pushes the result on the stack
     */
    /**/ class BinaryMultiply() : BytecodeInstruction(0x0B)

    /**
     *  Evaluates TOS1 / TOS  and pushes the result on the stack
     */
    /**/ class BinaryDivide() : BytecodeInstruction(0x0C)

    /**
     *  Evaluates TOS1 % TOS  and pushes the result on the stack
     */
    /**/ class BinaryModulo() : BytecodeInstruction(0x0D)

    /**
     *  Evaluates TOS1 ** TOS  and pushes the result on the stack
     */
    /**/ class BinaryExponentiate() : BytecodeInstruction(0x0E)

    /**
     *  Evaluates  -TOS and pushes the result on the stack
     */
    /**/ class UnaryNumericalNegate() : BytecodeInstruction(0x0F)

    /**
     *  Evaluates !TOS and pushes the result on the stack
     */
    /**/ class UnaryLogicalNegate() : BytecodeInstruction(0x10)

    /**
     *  Creates a new block with an environment and pushes it on the block stack
     */
    /**/ class PushBlock() : BytecodeInstruction(0x11)

    /**
     *  Pops a block from the block stack
     */
    /**/ class PopBlock() : BytecodeInstruction(0x12)

    /**
     * Swaps TOS and TOS1
     */
    /**/ class Swap() :BytecodeInstruction(0x13)

    /**
     * Evaluates TOS1 == TOS  and pushes the result on the stack
     */
    /**/ class BinaryCompareEquals() :BytecodeInstruction(0x14)

    /**
     * Evaluates TOS1 != TOS  and pushes the result on the stack
     */
    /**/ class BinaryCompareNotEquals() :BytecodeInstruction(0x15)

    /**
     * Evaluates TOS1 > TOS  and pushes the result on the stack
     */
    /**/ class BinaryCompareGreater() :BytecodeInstruction(0x16)

    /**
     * Evaluates TOS1 >= TOS  and pushes the result on the stack
     */
    /**/ class BinaryCompareGreaterEqual() :BytecodeInstruction(0x17)

    /**
     * Evaluates TOS1 < TOS  and pushes the result on the stack
     */
    /**/ class BinaryCompareLess() :BytecodeInstruction(0x18)

    /**
     * Evaluates TOS1 <= TOS  and pushes the result on the stack
     */
    /**/ class BinaryCompareLessEqual() :BytecodeInstruction(0x19)

    /**
     * Evaluates TOS1 or TOS  and pushes the result on the stack
     */
    /**/ class BinaryLogicalOr() :BytecodeInstruction(0x1A)

    /**
     * Evaluates TOS1 and TOS  and pushes the result on the stack
     */
    /**/ class BinaryLogicalAnd() :BytecodeInstruction(0x1B)




}