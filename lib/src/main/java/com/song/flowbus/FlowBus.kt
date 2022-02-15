package com.song.flowbus

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

interface IBus {
    /**
     * 1、需要使用@EventReceiver标注接收方法 ，用于确定接收参数类型
     * 2、当lifecycleOwner DESTROYED时，会自动停止接收事件
     */
    fun register(lifecycleOwner: LifecycleOwner)

    /**
     * 无lifecycleOwner的类使用此方法
     */
    fun register(registry: BusRegistry)

    /**
     * 1、当lifecycleOwner DESTROYED时，会自动停止接收事件
     */
    fun <T> registerWithCallBack(
        lifecycleOwner: LifecycleOwner,
        eventClazz: Class<*>,
        callBack: CallBack<T>
    )


    fun unregister(lifecycleOwner: LifecycleOwner)
    fun unregister(registry: BusRegistry)
    fun post(event: Any)
}

interface CallBack<T> {
    fun callback(t: T)
}

/**
 */
interface BusRegistry

object FlowBus : IBus by Impl()


inline fun <reified T> AppCompatActivity.registerSingEvent(callBack: CallBack<T>) {
    FlowBus.registerWithCallBack(this, T::class.java, callBack)
}

inline fun <reified T> Fragment.registerSingEvent(
    lifecycleOwner: LifecycleOwner = viewLifecycleOwner,
    callBack: CallBack<T>
) {
    FlowBus.registerWithCallBack(lifecycleOwner, T::class.java, callBack)
}



