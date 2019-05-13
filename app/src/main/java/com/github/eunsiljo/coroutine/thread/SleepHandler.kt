package com.github.eunsiljo.coroutine.thread

import android.os.Handler
import android.os.Message
import com.github.eunsiljo.coroutine.BaseViewModel
import java.lang.ref.WeakReference

class SleepHandler(
    private val viewModelReference: WeakReference<BaseViewModel>
) : Handler() {

    override fun handleMessage(msg: Message?) {
        msg?.run {
            when (what) {
                SleepMessage.PROGRESS.what -> {
                    viewModelReference.get()?.setLoadingValue(obj as? Boolean ?: false)
                }
                SleepMessage.ERROR.what -> {
                    viewModelReference.get()?.setErrorValue(obj as? Throwable)
                }
                SleepMessage.RESULT.what -> {
                    viewModelReference.get()?.setResultValue(obj as? String ?: "")
                }
                else -> super.handleMessage(msg)
            }
        } ?: super.handleMessage(msg)
    }
}