package app.mynt.networking

import app.mynt.networking.nio.TCPSocketProvider
import kotlinx.coroutines.experimental.launch
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousChannelGroup.withThreadPool
import java.util.concurrent.Executors.newCachedThreadPool

fun main(args: Array<String>) {
    launch {
        val provider = TCPSocketProvider(withThreadPool(newCachedThreadPool())) {
            ByteBuffer.allocate(256).flip() as ByteBuffer
        }

        val address = InetSocketAddress("localhost", 25565)
        launch {
            provider.accept(address).read {
                val first = byte()
                println("First: $first")

                val second = int()
                println("Second: $second")

                val third = bytes(4)
                println("Third: ${String(third)}")
            }
        }

        provider.connect(address).write {
            byte(10.toByte())
            println("Sent First")

            int(4)
            println("Sent Second")

            bytes("test".toByteArray(), 4, 0)
            println("Sent Third")
        }
    }

    Thread.sleep(10000)
}