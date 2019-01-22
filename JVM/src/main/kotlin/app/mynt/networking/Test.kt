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
    val group = withThreadPool(newCachedThreadPool())
    launch {
        val provider = TCPSocketProvider(group) {
            ByteBuffer.allocate(256).order(ByteOrder.LITTLE_ENDIAN).flip() as ByteBuffer
        }
        val address = InetSocketAddress("localhost", 25565)
        while (provider.isOpen) {
            val client = provider.accept(address)
            while (client.isOpen)
                println("Got: ${client.read.byte()}")
        }
    }
    group.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS)
}