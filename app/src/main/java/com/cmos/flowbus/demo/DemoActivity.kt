package com.cmos.flowbus.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cmos.flowbus.CallBack
import com.cmos.flowbus.registerSingEvent

class DemoActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerSingEvent(object : CallBack<TestEvent> {
            override fun callback(t: TestEvent) {
                "callback receive event = $t".logIt()
            }
        })
    }
}