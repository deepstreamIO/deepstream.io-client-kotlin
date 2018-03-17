package deepstream.services

import deepstream.*
import deepstream.constant.*
import deepstream.protocol.AUTH_ACTION
import deepstream.protocol.CONNECTION_ACTION
import deepstream.protocol.TOPIC
import deepstream.constant.CONNECTION_STATE as CS
import deepstream.constant.CONNECTION_TRANSITION as CT
import deepstream.util.*

import kotlinx.serialization.*
import kotlinx.serialization.json.JSON

class Connection(private val services: Services, private val options: Options, url: String) : StateChangedListener<CS> {
    private var handlers: MutableMap<Byte, Handler>

    private val originalUrl: String
    private var url: String

    private var lastHeartBeat: Int = -1
    lateinit var endpoint: Endpoint

    private var stateMachine: StateMachine<CS>

    private var isInLimbo: Boolean = true

    lateinit var resumeCallback: () -> Nothing
    lateinit var loginListener: LoginListener

    lateinit var authParams: Any
    lateinit var clientData: Any

    private final var transitions = setOf<Transition<Any, Any>>(
            Transition(CT.INITIALISED, CS.CLOSED, CS.INITIALISING),
            Transition(CT.CONNECTED, CS.INITIALISING, CS.AWAITING_CONNECTION),
            Transition(CT.CONNECTED, CS.REDIRECTING, CS.AWAITING_CONNECTION),
            Transition(CT.CONNECTED, CS.RECONNECTING, CS.AWAITING_CONNECTION),
            Transition(CT.CONNECTED, CS.INITIALISING, CS.AWAITING_CONNECTION),
            Transition(CT.CHALLENGE, CS.AWAITING_CONNECTION, CS.CHALLENGING),
            Transition(CT.CONNECTION_REDIRECTED, CS.CHALLENGING, CS.REDIRECTING),
            Transition(CT.CHALLENGE_DENIED, CS.CHALLENGING, CS.CHALLENGE_DENIED),
            Transition(CT.CHALLENGE_ACCEPTED, CS.CHALLENGING, CS.AWAITING_AUTHENTICATION),
            Transition(CT.AUTHENTICATION_TIMEOUT, CS.AWAITING_CONNECTION, CS.AUTHENTICATION_TIMEOUT),
            Transition(CT.AUTHENTICATION_TIMEOUT, CS.AWAITING_AUTHENTICATION, CS.AUTHENTICATION_TIMEOUT),
            Transition(CT.AUTHENTICATE, CS.AWAITING_AUTHENTICATION, CS.AUTHENTICATING),
            Transition(CT.UNSUCCESFUL_LOGIN, CS.AUTHENTICATING, CS.AWAITING_AUTHENTICATION),
            Transition(CT.SUCCESFUL_LOGIN, CS.AUTHENTICATING, CS.OPEN),
            Transition(CT.TOO_MANY_AUTH_ATTEMPTS, CS.AUTHENTICATING, CS.TOO_MANY_AUTH_ATTEMPTS),
            Transition(CT.TOO_MANY_AUTH_ATTEMPTS, CS.AWAITING_AUTHENTICATION, CS.TOO_MANY_AUTH_ATTEMPTS),
            Transition(CT.AUTHENTICATION_TIMEOUT, CS.AWAITING_AUTHENTICATION, CS.AUTHENTICATION_TIMEOUT),
            Transition(CT.RECONNECT, CS.RECONNECTING, CS.RECONNECTING),
            Transition(CT.CLOSED, CS.CLOSING, CS.CLOSED),
            Transition(CT.OFFLINE, CS.PAUSING, CS.OFFLINE),
            Transition(CT.ERROR, CS.RECONNECTING),
            Transition(CT.LOST, CS.RECONNECTING),
            Transition(CT.RESUME, CS.RECONNECTING),
            Transition(CT.PAUSE, CS.PAUSING),
            Transition(CT.CLOSE, CS.CLOSING)
    )

    init {
        this.handlers = mutableMapOf()
        this.stateMachine = StateMachine(services.logger, CS.CLOSED, this, transitions)

        this.stateMachine.transition(CT.INITIALISED)
        this.originalUrl = url
        this.url = this.originalUrl

        if (!options.lazyConnect) {
            this.createEndpoint()
        }
    }

    private fun createEndpoint() {
        this.endpoint = services.endpointFactory.createSocket(this, this.url, options.endpointOptions)
        this.endpoint.open();
    }

    override fun onStateChanged(oldState: CS, newState: CS) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun registerHandler (topic: Byte, handler: Handler) {
        this.handlers.set(topic, handler)
    }

    fun sendMessage (message: Message) {
        if (!this.isOpen()) {
            services.logger.error(message, EVENT.IS_CLOSED)
            return
        }
        this.endpoint!!.sendParsedMessage(message)
    }

    private fun isOpen(): Boolean {
        return false
    }

    fun authenticate (authParams: Any, loginListener: LoginListener) {
        if (
            this.stateMachine.state === CS.CHALLENGE_DENIED ||
            this.stateMachine.state === CS.TOO_MANY_AUTH_ATTEMPTS ||
            this.stateMachine.state === CS.AUTHENTICATION_TIMEOUT
        ) {
            services.logger.error(Message(TOPIC.CONNECTION), EVENT.IS_CLOSED)
            return
        }

        this.authParams = authParams
        this.loginListener = loginListener

        if (this.stateMachine.state === CS.AWAITING_AUTHENTICATION && this.authParams !== null) {
            this.sendAuthParams()
        }

        if (this.endpoint !== null) {
            this.createEndpoint()
        }
    }

    private fun sendAuthParams() {
        this.stateMachine.transition(CT.AUTHENTICATE)
        this.sendMessage(AuthRequestMessage(this.authParams))
    }

    /**
     * Closes the connection. Using this method
     * will prevent the client from reconnecting.
     */
    fun close () {
        services.timerRegistry.remove(options.heartbeatInterval)
        this.sendMessage(Message(TOPIC.CONNECTION, CONNECTION_ACTION.CLOSING))
        this.stateMachine.transition(CT.CLOSE)
    }

    fun pause () {
        this.stateMachine.transition(CT.PAUSE)
        services.timerRegistry.remove(options.heartbeatInterval)
        this.endpoint!!.close()
    }

    fun resume (resumeCallback: () -> Nothing) {
        this.stateMachine.transition(CT.RESUME)
        this.resumeCallback = resumeCallback
        this.tryReconnect()
    }

    /********************************
     ****** Endpoint Callbacks ******
    ********************************/
    /**
     * Will be invoked once the connection is established. The client
     * can't send messages yet, and needs to get a connection ACK or REDIRECT
     * from the server before authenticating
    */
    fun onOpen () {
        this.clearReconnect()
        this.lastHeartBeat = services.timerRegistry.now()
        this.checkHeartBeat()
        this.stateMachine.transition(CT.CONNECTED)
        this.sendMessage(ChallengeMessage(this.originalUrl, "0.1a"))
        this.stateMachine.transition(CT.CHALLENGE)
    }

    /**
     * Callback for generic connection errors. Forwards
     * the error to the client.
     *
     * The connection is considered broken once this method has been
     * invoked.
     */
    fun onError () {
        services.timerRegistry.remove(options.heartbeatInterval)
        this.stateMachine.transition(CT.ERROR)
        this.tryReconnect()
    }

    fun onMessage(message: Message) {
        println(message)
    }

    fun onClose() {
    }

    private fun clearReconnect() {
    }

    private fun checkHeartBeat() {
    }


    private fun tryReconnect() {
    }
}

