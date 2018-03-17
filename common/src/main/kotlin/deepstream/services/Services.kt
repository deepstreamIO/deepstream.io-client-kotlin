package deepstream.services

import deepstream.*
import deepstream.util.Logger

class Services {
    val logger: Logger
    val endpointFactory: EndpointFactory
    var timerRegistry: TimerRegistry
    var connection: Connection
    var timeoutRegistry: TimeoutRegistry
    var storage: Storage?

    constructor (url: String, options: Options) {
        this.storage = options.storage
        this.endpointFactory = options.endpointFactory

        this.logger = Logger()
        this.timerRegistry = TimerRegistry()
        this.timeoutRegistry = TimeoutRegistry(this, options)
        this.connection = Connection(this, options, url)
    }

}
