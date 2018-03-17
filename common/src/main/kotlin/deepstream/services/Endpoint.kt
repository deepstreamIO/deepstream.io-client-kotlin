package deepstream.services

import deepstream.util.Message

interface Endpoint {

    fun sendParsedMessage(message: Message)
    fun close()
    fun open()
}
