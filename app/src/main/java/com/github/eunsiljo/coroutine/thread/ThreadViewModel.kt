package com.github.eunsiljo.coroutine.thread

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import com.github.eunsiljo.coroutine.BaseViewModel
import java.util.concurrent.Executors

class ThreadViewModel : BaseViewModel() {
    companion object {
        private const val THREAD_POOL_SIZE = 2
    }

    private val threadPool by lazy { Executors.newFixedThreadPool(THREAD_POOL_SIZE) }

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
        ).also { threadPool.execute(it) }
    }

    override fun onCleared() {
        super.onCleared()
        threadPool.shutdown()
    }
}