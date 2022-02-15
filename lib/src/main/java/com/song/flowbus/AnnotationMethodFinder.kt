package com.song.flowbus

import com.song.flowbus.annotation.EventReceiver
import java.util.concurrent.ConcurrentHashMap

object AnnotationMethodFinder {
    private val classMethodCache = ConcurrentHashMap<Class<*>, List<EventMethod>>()

    fun find(host: Any): List<EventMethod> {
        val clazz = host.javaClass
        val cacheList = classMethodCache[clazz]
        if (cacheList != null) return cacheList
        val list = mutableListOf<EventMethod>()
        clazz.declaredMethods.forEach {
            val annotationPresent = it.isAnnotationPresent(EventReceiver::class.java)
            if (annotationPresent.not()) return@forEach
            val parameterTypes = it.parameterTypes
            if (parameterTypes.isEmpty()) throw Exception("@EventReceiver should contain event type param")
            list.add(EventMethod(parameterTypes[0], it, host))
        }
        classMethodCache[clazz] = list
        return list
    }

    fun clearCache() {
        classMethodCache.clear()
    }
}