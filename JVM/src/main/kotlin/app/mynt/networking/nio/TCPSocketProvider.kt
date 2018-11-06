package app.mynt.networking.nio

import app.mynt.networking.Address
import app.mynt.networking.Connection
import app.mynt.networking.Provider
import app.mynt.networking.continued
import app.mynt.networking.fixed.FixedBufferRead
import app.mynt.networking.fixed.FixedBufferWrite
import app.mynt.networking.sequential.SequentialReadCoordinator
import app.mynt.networking.sequential.SequentialWriteCoordinator
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousChannelGroup
import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.CompletionHandler
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeUnit.MILLISECONDS
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.intrinsics.COROUTINE_SUSPENDED


class TCPSocketProvider(
        private val group: AsynchronousChannelGroup,
        private val allocator: () -> ByteBuffer
) : Provider {
    private val servers = HashMap<SocketAddress, AsynchronousServerSocketChannel>()
    private val serverFactory = { address: SocketAddress ->
        AsynchronousServerSocketChannel.open(group).bind(address)
    }

    private open class Handler(
            allocator: () -> ByteBuffer
    ) : Connection {
        open lateinit var channel: AsynchronousSocketChannel

        override val read = FixedBufferRead(SequentialReadCoordinator { buffer, handler ->
            channel.read(buffer, buffer, handler)
        }, allocator())

        override val write = FixedBufferWrite(SequentialWriteCoordinator { buffer, handler ->
            channel.write(buffer, buffer, handler)
        }, allocator().flip() as ByteBuffer)

        override val isOpen
            get() = channel.isOpen

        override fun close() = channel.close()
    }

    //--Accept--
    private class AcceptHandler(
            allocator: () -> ByteBuffer
    ) : Handler(allocator), CompletionHandler<AsynchronousSocketChannel, Continuation<Connection>> {
        override fun completed(channel: AsynchronousSocketChannel, continuation: Continuation<Connection>) {
            this.channel = channel
            continuation.resume(this)
        }

        override fun failed(reason: Throwable, continuation: Continuation<Connection>) =
                continuation.resumeWithException(reason)
    }

    override suspend fun accept(address: Address) = continued<Connection> {
        servers.computeIfAbsent(address, serverFactory).accept(it, AcceptHandler(allocator))
        COROUTINE_SUSPENDED
    }


    //--Connect--
    private class ConnectHandler(
            allocator: () -> ByteBuffer,
            override var channel: AsynchronousSocketChannel
    ) : Handler(allocator), CompletionHandler<Void?, Continuation<Connection>> {

        override fun completed(ignored: Void?, continuation: Continuation<Connection>) =
                continuation.resume(this)

        override fun failed(reason: Throwable, continuation: Continuation<Connection>) =
                continuation.resumeWithException(reason)
    }

    override suspend fun connect(address: Address) = continued<Connection> {
        val channel = AsynchronousSocketChannel.open(group)
        channel.connect(address, it, ConnectHandler(allocator, channel))
        COROUTINE_SUSPENDED
    }


    //--State--
    override val isOpen
        get() = !group.isTerminated

    override fun close() = group.shutdown()

    //TODO Maybe just make close do this?
    fun awaitClose(
            period: Long = Long.MAX_VALUE,
            units: TimeUnit = MILLISECONDS
    ) {
        close()
        group.awaitTermination(period, units)
    }
}