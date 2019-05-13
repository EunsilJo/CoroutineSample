package com.github.eunsiljo.coroutine.thread

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Handler
import android.os.Message
import com.github.eunsiljo.coroutine.BaseViewModel
import java.lang.Exception
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


    fun getThreadResultWithAsyncTask(sleepMillis: Long) {
        SleepAsyncTask().executeOnExecutor(threadPool, sleepMillis)
    }

    @SuppressLint("StaticFieldLeak")
    inner class SleepAsyncTask : AsyncTask<Long, Boolean, Pair<String, Throwable?>>() {
        private val ASYNC_TASK_RESULT = "ASYNC TASK RESULT!"

        override fun onPreExecute() {
            super.onPreExecute()
            setLoadingValue(true)
        }

        override fun doInBackground(vararg millis: Long?): Pair<String, Throwable?> =
            try {
                Thread.sleep(millis[0] ?: 0L)
                ASYNC_TASK_RESULT to null
            } catch (exception: Exception) {
                "" to exception
            }

        override fun onPostExecute(result: Pair<String, Throwable?>?) {
            super.onPostExecute(result)
            setLoadingValue(false)
            result?.run {
                val (value, throwable) = this

                when (throwable) {
                    null -> setResultValue(value)
                    else -> setErrorValue(throwable)
                }
            }
        }
    }
}