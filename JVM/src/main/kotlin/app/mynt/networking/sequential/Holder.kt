package app.mynt.networking.sequential

//TODO meh this is not great.
open class Holder<Type> {
    protected var value: Type? = null

    fun holding(): Boolean {
        return value != null
    }

    fun hold(value: Type) {
        this.value = value
    }

    fun release(): Type {
        val temp = value!!
        value = null
        return temp
    }
}