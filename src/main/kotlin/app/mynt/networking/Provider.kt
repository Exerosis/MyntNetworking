package app.mynt.networking

interface Provider {
    suspend fun accept(address: Address): Connection

    suspend fun connect(address: Address): Connection

    val isOpen: Boolean

    fun close()
}