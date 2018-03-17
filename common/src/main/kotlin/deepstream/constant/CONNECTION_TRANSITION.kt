package deepstream.constant

enum class CONNECTION_TRANSITION(transition: String) {
    INITIALISED("initialised"),
    CONNECTED("connected"),
    CHALLENGE("challenge"),
    AUTHENTICATE("authenticate"),
    RECONNECT("reconnect"),
    CHALLENGE_ACCEPTED("accepted"),
    CHALLENGE_DENIED("challenge-denied"),
    CONNECTION_REDIRECTED("redirected"),
    TOO_MANY_AUTH_ATTEMPTS("too-many-auth-attempts"),
    CLOSE("close"),
    CLOSED("closed"),
    UNSUCCESFUL_LOGIN("unsuccesful-login"),
    SUCCESFUL_LOGIN("succesful-login"),
    ERROR("error"),
    LOST("connection-lost"),
    PAUSE("pause"),
    OFFLINE("offline"),
    RESUME("resume"),
    EXIT_LIMBO("exit-limbo"),
    AUTHENTICATION_TIMEOUT("authentication-timeout")
}
