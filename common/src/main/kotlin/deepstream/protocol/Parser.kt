package deepstream.protocol
import deepstream.util.Message

//fun isError (message: Message): Boolean {
//    return (message.action in 0x50..111) || message.topic === TOPIC.PARSER
//}
//
//fun parse (buffer: ByteArray, queue: ByteArray): Array<Message> {
//    var offset = 0
//    val messages = arrayOf<Message>()
//    do {
//        val (bytesConsumed, rawMessage) = readBinary(buffer, offset)
//        if (rawMessage === null) {
//            break
//        }
//        queue.plus(rawMessage)
//        offset += bytesConsumed
//        if (rawMessage.fin) {
//            val message = parseMessage(joinMessages(queue))
//            // queue.size = 0
//            messages.plus(message)
//        }
//    } while (offset < buffer.size)
//    return messages
//}

//data class RawMessage(val fin: Boolean, val rawMessage: ByteArray)
//data class Result(val bytesConsumed: Int, val rawMessage: RawMessage)
//fun readBinary(buffer: ByteArray, offset: Int): Result {
//    return Result(0, RawMessage(false, buffer))
//}
//
//fun joinMessages(queue: ByteArray): ByteArray {
//
//}
//
//fun parseMessage(rawMessage: ByteArray): Message {
//
//}
//
//fun parseData (message: Message): Boolean {
//    if (message.parsedData === null || message.data === null) {
//        return true
//    }
//
//    if (message.payloadEncoding !== PAYLOAD_ENCODING.JSON) {
//        // return Exception("unable to parse data of type '${message.payloadEncoding}'")
//    }
//
//    // TODO: Figure out how to parse JSON and represent for native solutions
//
//    // if (typeof message.data === 'string') {
//        // return Exception('tried to parse string data with binary parser')
//    // }
//
////    message.parsedData = parseJSON(message.data)
////    if (message.parsedData === undefined) {
////        return new Error(`unable to parse data ${message.data}`)
////    }
//
//    return true
//}