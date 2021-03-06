package com.song.flowbus.demo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.song.flowbus.BusRegistry
import com.song.flowbus.FlowBus
import com.song.flowbus.annotation.EventReceiver
import com.song.flowbus.demo.TestEvent
import com.song.flowbus.demo.logIt

class TestService : Service(), BusRegistry {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        FlowBus.register(this)
    }

    @EventReceiver
    private fun testReceive(name: String) {
        "service receive String  = $name".logIt()
    }


    @EventReceiver
    private fun testReceive(event: TestEvent) {
        "service receive String  = $event".logIt()
    }

    override fun onDestroy() {
        super.onDestroy()
        FlowBus.unregister(this)
    }
}