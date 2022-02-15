package com.cmos.flowbus.demo

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.cmos.flowbus.BusRegistry
import com.cmos.flowbus.FlowBus
import com.cmos.flowbus.annotation.EventReceiver

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