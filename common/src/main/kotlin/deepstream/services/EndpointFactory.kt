package deepstream.services

expect class EndpointFactory {
    constructor()
    fun createSocket(connection: Connection, url: String, endpointOptions: Any?): Endpoint
}
