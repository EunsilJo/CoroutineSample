package com.github.eunsiljo.coroutine.thread

import com.github.eunsiljo.coroutine.BaseViewModel
import java.lang.ref.WeakReference
import java.util.concurrent.Executors

class ThreadViewModel : BaseViewModel() {
    companion object {
        private const val THREAD_POOL_SIZE = 2
    }

    private val threadPool by lazy { Executors.newFixedThreadPool(THREAD_POOL_SIZE) }

    fun getThreadResult(sleepMillis: Long) {
        SleepThread(
            sleepMillis,
            SleepHandler(WeakReference(this@ThreadViewModel))
        ).also { threadPool.execute(it) }
    }

    fun getThreadResultWithAsyncTask(sleepMillis: Long) {
        SleepAsyncTask(WeakReference(this@ThreadViewModel))
            .executeOnExecutor(threadPool, sleepMillis)
    }

    override fun onCleared() {
        super.onCleared()
        threadPool.shutdown()
    }
}