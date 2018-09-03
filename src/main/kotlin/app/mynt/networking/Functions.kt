package app.mynt.networking

import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.intrinsics.suspendCoroutineUninterceptedOrReturn

suspend inline fun <Type> continued(
        crossinline block: (Continuation<Type>) -> Any?
) = suspendCoroutineUninterceptedOrReturn(block)

suspend fun Read.bytes(
        amount: Int,
        offset: Int = 0
) = bytes(ByteArray(amount), amount, offset)

inline fun Connection.read(
        block: Read.() -> Unit
) = block(read)

inline fun Connection.write(
        block: Write.() -> Unit
) = block(write)