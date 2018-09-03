package app.mynt.networking

import java.nio.ByteBuffer
import kotlin.coroutines.experimental.Continuation

//TODO do we really want these?
interface WriteCoordinator {
    fun array(
            using: ByteBuffer,
            array: ByteArray,
            amount: Int = array.size,
            offset: Int = 0,
            continuation: Continuation<Unit>
    ): Any

    fun buffer(
            using: ByteBuffer,
            buffer: ByteBuffer,
            continuation: Continuation<Unit>
    ): Any

    fun <Type : Number> number(
            using: ByteBuffer,
            value: Type,
            writer: (Type, ByteBuffer) -> ByteBuffer,
            continuation: Continuation<Unit>
    ): Any
}