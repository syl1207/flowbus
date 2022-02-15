package com.song.flowbus

import java.lang.reflect.Method

data class EventMethod(val eventType: Class<*>, val method: Method, val host: Any)