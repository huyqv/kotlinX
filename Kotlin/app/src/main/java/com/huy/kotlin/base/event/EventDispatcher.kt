package com.huy.kotlin.base.event

import android.util.SparseArray


/**
 * -------------------------------------------------------------------------------------------------
 * @Project: Kotlin
 * @Created: Huy QV 2017/10/14
 * @Description: ...
 * None Right Reserved
 * -------------------------------------------------------------------------------------------------
 */
class EventDispatcher private constructor() {

    private val observers = SparseArray<MutableSet<EventListener>>()

    private val sync = Any()

    companion object {
        val instance: EventDispatcher by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            EventDispatcher()
        }
    }

    fun addListener(vararg events: Int, listener: EventListener) {

        synchronized(sync) {
            events.forEach {
                val listeners = observers[it] ?: mutableSetOf()
                listeners.add(listener)
                observers.put(it, listeners)
            }
        }
    }

    fun removeListener(vararg events: Int, listener: EventListener) {
        synchronized(sync) {
            events.forEach { observers[it].remove(listener) }
        }
    }

    fun post(eventId: Int, vararg args: Any?) {
        var listeners: Set<EventListener>?
        synchronized(sync) {
            listeners = observers.get(eventId)
            if (listeners != null && !listeners!!.isEmpty()) {
                for (listener in listeners!!) {
                    listener.onEvent(eventId, *args)
                }
            }
        }
    }

}