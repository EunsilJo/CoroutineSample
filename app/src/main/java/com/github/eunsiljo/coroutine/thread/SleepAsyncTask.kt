package com.github.eunsiljo.coroutine.thread

import android.os.AsyncTask
import com.github.eunsiljo.coroutine.BaseViewModel
import java.lang.Exception
import java.lang.ref.WeakReference

class SleepAsyncTask(
    private val viewModelReference: WeakReference<BaseViewModel>
) : AsyncTask<Long, Boolean, Pair<String, Throwable?>>() {

    private val ASYNC_TASK_RESULT = "ASYNC TASK RESULT!"

    override fun onPreExecute() {
        super.onPreExecute()
        viewModelReference.get()?.setLoadingValue(true)
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
        viewModelReference.get()?.setLoadingValue(false)
        result?.run {
            val (value, throwable) = this

            when (throwable) {
                null -> viewModelReference.get()?.setResultValue(value)
                else -> viewModelReference.get()?.setErrorValue(throwable)
            }
        }
    }
}