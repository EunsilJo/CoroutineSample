package com.github.eunsiljo.coroutine.thread

enum class SleepMessage(val what: Int) {
    PROGRESS(0), ERROR(1), RESULT(2)
}