package me.alufers.sol

import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class BinaryBuilder {
    val byteStream = ByteArrayOutputStream()
    fun putByte(d: Byte) {
        byteStream.write(byteArrayOf(d))
    }

    fun putInt(d: Int) {
        byteStream.write(ByteBuffer.allocate(java.lang.Integer.BYTES).putInt(d).array())
    }

    fun putDouble(d: Double) {
        val l = java.lang.Double.doubleToRawLongBits(d)
        byteStream.write(ByteBuffer.allocate(java.lang.Long.BYTES).putLong(l).array())
    }

    fun putString(str: String) {
        val data = str.toByteArray(Charsets.UTF_8)
        putInt(data.size)

        for (b in data) {
            putByte(b)
        }
    }

    fun putBoolean(d: Boolean) {
        putByte(if (d) 1 else 0x00)
    }

}