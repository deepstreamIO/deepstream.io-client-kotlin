package main.kotlin.deepstream

import deepstream.*
import deepstream.services.Services

class Client {

    private var options: Options
    private var services: Services

    public var event: EventHandler
    public var rpc: RPCHandler
    public var record: RecordHandler
    public var presence: PresenceHandler

    constructor(url: String): this(url, emptyMap())

    constructor (url: String, options: Map<String, Any>) {
        this.options = Options(options)
        this.services = Services(url, this.options)

        this.event = EventHandler(this.services, this.options)
        this.rpc = RPCHandler(this.services, this.options)
        this.record = RecordHandler(this.services, this.options)
        this.presence = PresenceHandler(this.services, this.options)
    }

    fun open () {

    }
}