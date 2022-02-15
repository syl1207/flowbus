package com.song.flowbus.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.song.flowbus.CallBack
import com.song.flowbus.registerSingEvent

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