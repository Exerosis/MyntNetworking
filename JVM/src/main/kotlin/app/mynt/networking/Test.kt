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
        while (provider.isOpen)
            provider.accept(address).apply {
                while (isOpen)
                    println("Got: ${read.byte()}")
            }
    }
    Thread.sleep(10000000)
}