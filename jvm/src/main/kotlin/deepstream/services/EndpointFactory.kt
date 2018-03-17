package deepstream.services

import deepstream.util.Message
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import java.io.IOException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.net.URI
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory

actual class EndpointFactory {
    actual constructor() {
        println("creating")
    }

    actual fun createSocket(connection: Connection, url: String, endpointOptions: Any?): Endpoint {
        return WebSocket(connection, URI.create(url), Draft_6455())
    }

    private inner class WebSocket internal constructor(private val deepstreamConnection: Connection, serverUri: URI, draft: Draft) : WebSocketClient(serverUri, draft), Endpoint {
        override fun open() {
            super.connect()
        }

        init {
            // Set the SSL context if the socket server is using Secure WebSockets
            if (serverUri.toString().startsWith("wss:")) {
                val sslContext: SSLContext
                val factory: SSLSocketFactory
                try {
                    sslContext = SSLContext.getInstance("TLS")
                    sslContext.init(null, null, null)
                    factory = sslContext.getSocketFactory()
                    this.socket = factory.createSocket()
                } catch (e: NoSuchAlgorithmException) {
                    throw RuntimeException(e)
                } catch (e: KeyManagementException) {
                    throw RuntimeException(e)
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }

            }
        }

        override fun onOpen(handshakedata: ServerHandshake) {
            deepstreamConnection.onOpen()
        }

        override fun onMessage(message: String) {
            deepstreamConnection.onMessage(Message(message))
        }

        override fun onClose(code: Int, reason: String, remote: Boolean) {
            try {
                deepstreamConnection.onClose()
            } catch (e: Exception) {
            }

        }

        override fun onError(ex: Exception) {
            println(ex.toString())
            if (ex is NullPointerException && ex.message == "ssl == null") {
                return
            }
            deepstreamConnection.onError()
        }

        override fun sendParsedMessage(message: Message) {
        }
    }
}