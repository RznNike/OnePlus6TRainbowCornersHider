package ru.rznnike.fajita.cornersoverlay.app.global.event

import android.os.Handler
import java.util.*
import kotlin.collections.ArrayList

class EventDispatcher {
    private val eventListeners = HashMap<Int, MutableList<EventListener>>()

    fun addEventListener(eventCode: Int, listener: EventListener): EventListener {
        if (eventListeners[eventCode] == null) {
            eventListeners[eventCode] = ArrayList()
        }

        val list = eventListeners[eventCode]
        list?.add(listener)

        return listener
    }

    fun removeEventListener(listener: EventListener) = eventListeners
        .filter { it.value.size > 0 }
        .forEach { it.value.remove(listener) }

    fun sendEvent(eventCode: Int, data: Any? = null) {
        eventListeners
            .filter { it.key == eventCode && it.value.size > 0 }
            .forEach {
                it.value.forEach { listener -> Handler().post { listener.onEvent(eventCode, data) } }
            }
    }

    interface EventListener {
        fun onEvent(eventCode: Int, data: Any?)
    }
}