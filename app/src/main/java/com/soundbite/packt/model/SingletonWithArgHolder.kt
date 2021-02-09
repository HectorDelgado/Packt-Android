package com.soundbite.packt.model

/**
 * Used to create a singleton object with a single parameter.
 * Note: Any subclass should have a companion object that extends this class.
 *
 * @param constructor A reference to the 1-arg constructor of the target class.
 */
open class SingletonWithArgHolder<out T, in A>(private val constructor: (A) -> T) {
    @Volatile
    private var INSTANCE: T? = null

    /**
     * Returns a singleton instance of the inherited class.
     *
     * @param arg The argument that will be passed to the singleton instance.
     *
     * @return a singleton instance of the inherited class.
     */
    fun getInstance(arg: A) =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: constructor(arg).also {
                INSTANCE = it
            }
        }
}
