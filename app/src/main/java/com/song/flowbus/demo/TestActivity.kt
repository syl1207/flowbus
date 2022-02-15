package com.song.flowbus.demo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.song.flowbus.FlowBus
import com.song.flowbus.annotation.EventReceiver

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FlowBus.register(this)
        setContentView(Button(this).apply {
            text = "post msg"
            setOnClickListener {
                FlowBus.post(TestEvent("s", 12))
            }
        })
    }

    @EventReceiver
    private fun testMsg(msg: String) {
        "test receive msg = $msg".logIt()
    }

    @EventReceiver
    fun observeEvent(testEvent: TestEvent) {
        testEvent.toString().logIt()
    }
}