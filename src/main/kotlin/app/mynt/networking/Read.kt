package app.mynt.networking

//TODO Add number overloads?
expect interface Read {
    suspend fun bytes(
            bytes: ByteArray,
            amount: Int = bytes.size,
            offset: Int = 0
    ): ByteArray

    suspend fun byte(): Byte

    suspend fun short(): Short

    suspend fun int(): Int

    suspend fun float(): Float

    suspend fun long(): Long

    suspend fun double(): Double


}