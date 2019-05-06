package com.github.eunsiljo.coroutine.thread

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import com.github.eunsiljo.coroutine.BaseViewModel
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ThreadViewModel : BaseViewModel() {
    companion object {
        private const val CORE_POOL_SIZE = 5
        private const val MAXINUM_POOL_SIZE = 64
        private const val KEEP_ALIVE_TIME = 0L
    }

    private val threadPool by lazy {
        ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAXINUM_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.MICROSECONDS,
            LinkedBlockingQueue<Runnable>()
        )
    }

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

    fun onDestroy() {
        threadPool.shutdown()
    }
}