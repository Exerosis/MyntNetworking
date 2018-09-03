package app.mynt.networking.nio

import app.mynt.networking.*
import app.mynt.networking.fixed.FixedBufferRead
import app.mynt.networking.fixed.FixedBufferWrite
import app.mynt.networking.sequential.SequentialReadCoordinator
import app.mynt.networking.sequential.SequentialWriteCoordinator
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.nio.channels.DatagramChannel
import java.util.*
import java.util.concurrent.ExecutorService

class UDPSocketProvider(
        executor: ExecutorService,
        private val allocator: () -> ByteBuffer
) : Provider {
    private open class Handler(
            allocator: () -> ByteBuffer
    ) : Connection {
        open lateinit var channel: DatagramChannel

        override val read = FixedBufferRead(SequentialReadCoordinator { buffer, handler ->
            channel.receive(buffer)
        }, allocator())

        override val write = FixedBufferWrite(SequentialWriteCoordinator { buffer, handler ->
            channel.write(buffer, buffer, handler)
        }, allocator().flip() as ByteBuffer)

        override val isOpen
            get() = channel.isOpen

        override fun close() = channel.close()
    }



    val channels = HashMap<SocketAddress, ListenerConnection>()
    val channelFactory = { address: SocketAddress ->
        DatagramChannel.open().bind(address)
    }
    private class ListenerConnection(val channel: DatagramChannel) : Connection {
        val listeners = LinkedList<Pair<>>()

        fun doReceive() {

            while (true) {
                doReceive()
                if (System.nanoTime().rem( 30) == 0L) {
                    break;
                }
            }

            channel.receive()
        }

        override val read = FixedBufferRead(SequentialReadCoordinator { buffer, handler ->
            channel.read(buffer, buffer, handler)
        }, allocator())
        override val write = object : Write {
            override suspend fun byte(value: Byte) {

            }
        }
        override val isOpen: Boolean
            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

        override fun close() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override suspend fun accept(address: Address) = object : Connection {
        override val read = FixedBufferRead(SequentialReadCoordinator { buffer, handler ->
            channel.read(buffer, buffer, handler)
        }, allocator())
        override val write = object : Write {
            override suspend fun byte(value: Byte) {

            }
        }
        override val isOpen: Boolean
            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

        override fun close() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

    override suspend fun connect(address: Address): Connection {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val isOpen: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}