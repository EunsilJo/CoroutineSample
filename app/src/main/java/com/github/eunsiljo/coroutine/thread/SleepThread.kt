package com.github.eunsiljo.coroutine.thread

import android.os.Handler
import java.lang.Exception

class SleepThread(private val millis: Long, private val handler: Handler) : Thread() {
    companion object {
        private const val THREAD_RESULT = "THREAD RESULT!"
    }

    override fun run() {
        try {
            handler.run {
                sendMessage(obtainMessage(SleepMessage.PROGRESS.what, true))
            }
            sleep(millis)
            handler.run {
                sendMessage(obtainMessage(SleepMessage.PROGRESS.what, false))
                sendMessage(obtainMessage(SleepMessage.RESULT.what, THREAD_RESULT))
            }
        } catch (exception: Exception) {
            handler.run {
                sendMessage(obtainMessage(SleepMessage.ERROR.what, exception))
            }
        }
        super.run()
    }
}