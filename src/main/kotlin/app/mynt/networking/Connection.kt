package app.mynt.networking

interface Connection {
    val read: Read
    val write: Write
    val isOpen: Boolean

    fun close()
}