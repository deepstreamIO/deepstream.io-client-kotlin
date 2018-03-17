package deepstream

import deepstream.services.EndpointFactory
import deepstream.services.Storage

class Options {
    var path: String = "/deepstream"
    var lazyConnect: Boolean = false
    var endpointOptions: Any? = null
    var storage: Storage
    var endpointFactory: EndpointFactory
    val heartbeatInterval: Int = 30000

    constructor (options: Map<String, Any>) {
        if (!options.containsKey("endpointFactory")) {
            this.endpointFactory = EndpointFactory()
        } else {
            this.endpointFactory = options.get("endpointFactory") as EndpointFactory
        }

        if (!options.containsKey("storage")) {
            this.storage = Storage()
        } else {
            this.storage = options.get("storage") as Storage
        }
    }
}
