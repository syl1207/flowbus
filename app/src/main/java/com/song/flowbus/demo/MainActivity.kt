package com.song.flowbus.demo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.song.flowbus.AnnotationMethodFinder
import com.song.flowbus.CallBack
import com.song.flowbus.FlowBus
import com.song.flowbus.annotation.EventReceiver
import com.song.flowbus.registerSingEvent

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Button(this).apply {
            text = "post msg \n long click to unRegister"
            setOnClickListener {
                FlowBus.post(TestEvent("AAA", 24))
//                FlowBus.post("somthing")
            }
            setOnLongClickListener {
//                FlowBus.unregister(this@MainActivity)
                stopService(Intent(this@MainActivity, TestService::class.java))
                Toast.makeText(this@MainActivity, "unregister success!", Toast.LENGTH_LONG).show()
                true
            }
        })
        startService(Intent(this, TestService::class.java))
        FlowBus.register(this)

        registerSingEvent(object : CallBack<TestEvent> {
            override fun callback(t: TestEvent) {
                "callback receive event = $t".logIt()
            }
        })

        startActivity(Intent(this, TestActivity::class.java))
    }

    @EventReceiver
    private fun test(value: String) {
        "receiver receive  = $value ".logIt()
    }


    @EventReceiver
    private fun test2(value: TestEvent) {
        "receiver receive obj = $value".logIt()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        AnnotationMethodFinder.clearCache()
    }

}

fun Any.logIt() {
    Log.e("krik", toString())
}