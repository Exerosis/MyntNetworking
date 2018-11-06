package app.mynt.networking

import turbo.net.connect


fun main(args: Array<String>) {
    val test = connect(25565, "localhost")
    println(test::class)
}