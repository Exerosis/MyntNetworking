package turbo.net

import app.mynt.networking.jsObject
import app.mynt.networking.require
import org.khronos.webgl.ArrayBuffer

val module = require("turbo-net")
private val ALLOW = jsObject {
    val allowHalfOpen = true
}
private val DISALLOW = jsObject {
    val allowHalfOpen = false
}

external class Socket {
    fun write(
            buffer: ArrayBuffer,
            length: Int,
            callback: (dynamic, ArrayBuffer, Int) -> Unit
    )

    fun read(
            buffer: ArrayBuffer,
            callback: (dynamic, ArrayBuffer, Int) -> Unit
    )
}

fun connect(port: Int, host: String, allowHalfOpen: Boolean = false): Socket {
    return module.connect(port, host, if (allowHalfOpen) ALLOW else DISALLOW)
}

fun createServer(allowHalfOpen: Boolean = false, onSocket: (Socket) -> Unit) {
    module.createServer(if (allowHalfOpen) ALLOW else DISALLOW, onSocket)
}