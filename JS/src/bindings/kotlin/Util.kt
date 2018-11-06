package app.mynt.networking

external fun require(module: String): dynamic

inline fun jsObject(init: dynamic.() -> Unit): dynamic {
    val anonymousObject = js("{}")
    init(anonymousObject)
    return anonymousObject
}