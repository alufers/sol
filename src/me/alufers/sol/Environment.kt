package me.alufers.sol

class Environment {
    class ValueAlreadyDefinedError(message: String) : Throwable(message)

    val values = HashMap<String, Any?>()
    fun define(name: String, value: Any?) {
        if (values.containsKey(name)) {
            throw ValueAlreadyDefinedError("Value $name already defined.")
        }
        values.put(name, value)
    }

    fun get(name: String): Any? {
        return values.get(name)
    }
}