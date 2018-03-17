package deepstream.services

expect class TimerRegistry {
    fun remove(timerId: Int?)
    fun now(): Int

    constructor()
}
