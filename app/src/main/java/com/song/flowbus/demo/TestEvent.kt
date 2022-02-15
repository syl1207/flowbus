package com.song.flowbus.demo

data class TestEvent(val name: String, val age: Int) {
    override fun toString(): String {
        return "TestEvent{name = $name, age = $age}"
    }
}