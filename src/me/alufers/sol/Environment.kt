package me.alufers.sol

class Environment(val parent: Environment? = null) {
    class ValueAlreadyDefinedError(message: String) : Throwable(message)
    class ValueNotDefinedError(message: String) : Throwable(message)

    val values = HashMap<String, Any?>()
    fun define(name: String, value: Any?) {
        if (values.containsKey(name)) {
            throw ValueAlreadyDefinedError("Cannot redefine '$name'")
        }
        values.put(name, value)
    }

    fun get(name: String): Any? {
        if (!values.containsKey(name)) {
            if (parent != null) {
                return parent.get(name)
            } else {
                throw ValueNotDefinedError("$name is not defined")
            }
        }
        return values.get(name)
    }

    fun set(name: String, value: Any?) {
        if (!values.containsKey(name)) {
            if (parent != null) {
                parent.set(name, value)
                return
            } else {
                throw ValueNotDefinedError("$name is not defined")
            }
        }

        values.set(name, value)
    }
}