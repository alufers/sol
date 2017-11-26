package me.alufers.sol

class Environment {
    class ValueAlreadyDefinedError(message: String) : Throwable(message)
    class ValueNotDefinedError(message: String) : Throwable(message)

    val values = HashMap<String, Any?>()
    fun define(name: String, value: Any?) {
        if (values.containsKey(name)) {
            throw ValueAlreadyDefinedError("$name is already defined")
        }
        values.put(name, value)
    }

    fun get(name: String): Any? {
        if (!values.containsKey(name)) {
            throw ValueNotDefinedError("$name is not defined")
        }
        return values.get(name)
    }

    fun set(name: String, value: Any?): Any? {
        if (!values.containsKey(name)) {
            throw ValueNotDefinedError("$name is not defined")
        }
        return values.set(name, value)
    }
}