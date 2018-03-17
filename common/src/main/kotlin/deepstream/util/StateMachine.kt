package deepstream.util

class StateMachine<V>(logger: Logger, initialState: V, listener: StateChangedListener<V>, transitions: Any) {
    var state: V = initialState

    fun transition(initialised: Any) {

    }

}

interface StateChangedListener<V> {
    fun onStateChanged (oldState: V, newState: V)
}

class Transition<N,S> {
    constructor(name: N, from: S, to: S) {

    }

    constructor(name: N, to: S) {

    }
}