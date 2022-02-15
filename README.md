### 概述
    该库使用sharedFlow简单实现bus总线功能

### 主要API介绍(见IBus类)

- **register(lifecycleOwner: LifecycleOwner)**
  给lifecyleOwner注册事件监听，使用此方法时需要用@EventReceiver指定接收方法
  Tip1：当lifeycleOwner状态为ON_DESTROY时，会自动unregister事件，此时无法收到消息
  Tip2: 常规场景下可以无需主动调用unregister取消监听

- **register(registry: BusRegistry)**
  给没有lifecycle的宿主注册事件监听，使用此方法时需要用@EventReceiver指定接收方法
  BusRegistry为空接口，只要实现即可，供后期拓展

- **registerWithCallBack(lifecycleOwner: LifecycleOwner, eventClazz: Class<星号>,callBack: CallBack<T>)**
  用回调方式注册事件，无需直接使用该方法，直接使用拓展方法*AppCompatActivity.registerSingEvent(callBack: CallBack<T>)*
  或者
  *Fragment.registerSingEvent(lifecycleOwner: LifecycleOwner = viewLifecycleOwner, callBack: CallBack<T>)*

-  **unregister(lifecycleOwner: LifecycleOwner)**
   注销监听，Activity onDestroy时会自动注销，无需主动调用，如果有其他需要注销的时机可以调用此方法

- **unregister(registry: BusRegistry)**
  注销监听，需要跟register成对调用

-  **post(event: Any)**
   发送事件

### 代码示例

#### 给lifecycleOwner注册事件监听
```kotlin
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
```

#### 无lifecycleOwner方式注册事件监听
```kotlin
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
```

####callback方式注册event监听
```kotlin
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
```