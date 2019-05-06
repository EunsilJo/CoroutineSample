package com.github.eunsiljo.coroutine.thread

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import com.github.eunsiljo.coroutine.BaseViewModel

class ThreadViewModel : BaseViewModel() {

    fun getThreadResult(sleepMillis: Long) {
        SleepThread(
            sleepMillis,
            @SuppressLint("HandlerLeak")
            object : Handler() {
                override fun handleMessage(msg: Message?) {
                    msg?.run {
                        when (what) {
                            SleepMessage.PROGRESS.what -> {
                                setLoadingValue(obj as? Boolean ?: false)
                            }
                            SleepMessage.ERROR.what -> {
                                setErrorValue(obj as? Throwable)
                            }
                            SleepMessage.RESULT.what -> {
                                setResultValue(obj as? String ?: "")
                            }
                            else -> super.handleMessage(msg)
                        }
                    } ?: super.handleMessage(msg)
                }
            }
        ).start()
    }
}