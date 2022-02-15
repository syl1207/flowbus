package com.cmos.flowbus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import java.util.concurrent.ConcurrentHashMap

internal class Impl : IBus {
    private val eventFlow = MutableSharedFlow<Any>()

    private val coroutineScope: CoroutineScope by lazy {
        MainScope()
    }

    private val jobCacheMap = ConcurrentHashMap<BusRegistry, Job>()

    override fun register(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            subscribeAnnotation(lifecycleOwner)
        }
    }

    private suspend fun subscribeAnnotation(host: Any) {
        val annotationMethodList = AnnotationMethodFinder.find(host)
        if (annotationMethodList.isEmpty()) throw Exception("should has at least one declared @EventReceiver method to receive event")
        eventFlow.collect {
            annotationMethodList.forEach { item ->
                if (it.javaClass != item.eventType) return@forEach
                //类型匹配上了，直接反射执行
                val method = item.method
                if (method.isAccessible.not()) method.isAccessible = true
                coroutineScope.launch {
                    method.invoke(item.host, it)
                }
            }
        }
    }

    override fun <T> registerWithCallBack(
        lifecycleOwner: LifecycleOwner,
        eventClazz: Class<*>,
        callBack: CallBack<T>
    ) {
        lifecycleOwner.lifecycleScope.launch {
            eventFlow.collect {
                if (it.javaClass != eventClazz) return@collect
                callBack.callback(it as T)
            }
        }
    }


    override fun register(registry: BusRegistry) {
        val job = Job()
        jobCacheMap[registry] = job
        CoroutineScope(Dispatchers.IO + job).launch {
            subscribeAnnotation(registry)
        }
    }

    override fun unregister(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.cancel()
    }

    override fun unregister(registry: BusRegistry) {
        jobCacheMap[registry]?.apply {
            cancel()
            jobCacheMap.remove(registry)
        }
    }

    override fun post(event: Any) {
        coroutineScope.launch {
            eventFlow.emit(event)
        }
    }
}