package app.mynt.networking

import app.mynt.networking.nio.TCPSocketProvider
import kotlinx.coroutines.experimental.launch
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.AsynchronousChannelGroup.withThreadPool
import java.util.concurrent.Executors.newCachedThreadPool
import java.util.concurrent.TimeUnit



fun main(args: Array<String>) {
    val input = "Hello world!"
    val group = withThreadPool(newCachedThreadPool())
    val provider = TCPSocketProvider(group) {
        ByteBuffer.allocate(256).order(ByteOrder.LITTLE_ENDIAN).flip() as ByteBuffer
    }
    launch {
//        val address = InetSocketAddress("68.81.69.63", 43594)
        val address = InetSocketAddress("localhost", 25566)
        launch {
            provider.accept(address).read {
                println(String(bytes(int())))
                println("Received")
            }
        }
        provider.connect(address).write {
            int(input.length)
            bytes(input.toByteArray(), input.length, 0)
            println("Sent")
        }
    }
    group.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS)
}