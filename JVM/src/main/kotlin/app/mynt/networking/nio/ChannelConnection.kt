package app.mynt.networking.nio

import app.mynt.networking.Connection
import java.nio.channels.Channel

abstract class ChannelConnection(
        private val channel: Channel
) : Connection {
    override val isOpen
        get() = channel.isOpen

    override fun close() = channel.close()
}