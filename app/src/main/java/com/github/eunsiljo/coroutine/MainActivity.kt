package com.github.eunsiljo.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.eunsiljo.coroutine.coroutine.CoroutineViewModel
import com.github.eunsiljo.coroutine.rx.RxViewModel
import com.github.eunsiljo.coroutine.thread.ThreadViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SLEEP_MILLIS = 3000L
    }

    private val threadViewModel by lazy {
        ViewModelProviders.of(this@MainActivity).get(ThreadViewModel::class.java)
    }

    private val rxViewModel by lazy {
        ViewModelProviders.of(this@MainActivity).get(RxViewModel::class.java)
    }

    private val coroutineViewModel by lazy {
        ViewModelProviders.of(this@MainActivity).get(CoroutineViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subscribeViewModel(threadViewModel)
        subscribeViewModel(rxViewModel)
        subscribeViewModel(coroutineViewModel)

        button.setOnClickListener {
            //threadViewModel.getThreadResult(SLEEP_MILLIS)
            //rxViewModel.getRxResult(SLEEP_MILLIS)
            coroutineViewModel.getCoroutineResult(SLEEP_MILLIS)
        }
    }

    private fun subscribeViewModel(viewModel: BaseViewModel) {
        viewModel.run {
            loadingState.observe(
                this@MainActivity,
                Observer { visible ->
                    progress.visibility = when (visible) {
                        true -> View.VISIBLE
                        false -> View.INVISIBLE
                    }
                }
            )
            errorState.observe(
                this@MainActivity,
                Observer { throwable ->
                    throwable?.printStackTrace()
                }
            )
            resultState.observe(
                this@MainActivity,
                Observer { result ->
                    Toast.makeText(this@MainActivity, result, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
