package deepstream.util

import deepstream.protocol.AUTH_ACTION
import deepstream.protocol.CONNECTION_ACTION
import deepstream.protocol.PAYLOAD_ENCODING
import deepstream.protocol.TOPIC

//topic: TOPIC
//action: ALL_ACTIONS
//name?: string
//
//isError?: boolean
//isAck?: boolean
//
//data?: string | Buffer
//parsedData?: any
//payloadEncoding?: PAYLOAD_ENCODING
//
//parseError?: false
//
//raw?: string | Buffer
//
//originalTopic?: TOPIC
//originalAction?: ALL_ACTIONS
//subscription?: string
//names?: Array<string>
//isWriteAck?: boolean
//correlationId?: string
//path?: string
//version?: number
//reason?: string
//url?: string
//protocolVersion?: string

class ChallengeMessage(public val url: String, public val protocolVersion: String) : Message(TOPIC.CONNECTION, CONNECTION_ACTION.CHALLENGE)
class AuthRequestMessage(public val authData: Any): Message(TOPIC.AUTH, AUTH_ACTION.REQUEST)

open class Message {
    lateinit var topic: TOPIC
    var action: Byte = 0
    lateinit var parsedData: Any
    lateinit var data: String
    lateinit var payloadEncoding: PAYLOAD_ENCODING

    constructor(topic: TOPIC)
    constructor(headers: Map<String, Any>)
    constructor(topic: Any, action: Any, authParams: Any?)
    constructor(topic: Any, action: Any)
    constructor(rawMessage: String)
}
