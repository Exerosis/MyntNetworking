package app.mynt.networking

import java.net.SocketAddress
import java.nio.ByteBuffer
import kotlin.coroutines.experimental.Continuation

fun main(args: Array<String>) {

}

fun magicRead(buffer: ByteBuffer, callback: (SocketAddress, ByteBuffer, Int) -> Unit) {

}

class UDPTest(
        val read: (ByteBuffer, (ByteBuffer, Int) -> Unit) -> Unit
) : ReadCoordinator {


    override fun buffer(
            using: ByteBuffer,
            buffer: ByteBuffer,
            amount: Int,
            continuation: Continuation<ByteBuffer>
    ): Any {
        read
    }

    override fun array(
            using: ByteBuffer,
            array: ByteArray,
            amount: Int,
            offset: Int,
            continuation: Continuation<ByteArray>
    ): Any {

    }

    override fun <Type : Number> number(
            using: ByteBuffer,
            amount: Int,
            reader: (ByteBuffer) -> Type,
            continuation: Continuation<Type>
    ): Any {

    }
}