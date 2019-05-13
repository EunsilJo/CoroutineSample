package com.github.eunsiljo.coroutine.thread

import android.os.Handler
import java.lang.Exception

class SleepThread(
    private val sleepMillis: Long,
    private val handler: Handler
) : Thread() {

    companion object {
        private const val THREAD_RESULT = "THREAD RESULT!"
    }

    override fun run() {
        super.run()
        handler.run {
            sendMessage(obtainMessage(SleepMessage.PROGRESS.what, true))
        }

        try {
            sleep(sleepMillis)

            handler.run {
                sendMessage(obtainMessage(SleepMessage.RESULT.what, THREAD_RESULT))
            }
        } catch (exception: Exception) {
            handler.run {
                sendMessage(obtainMessage(SleepMessage.ERROR.what, exception))
            }
        } finally {
            handler.run {
                sendMessage(obtainMessage(SleepMessage.PROGRESS.what, false))
            }
        }
    }
}