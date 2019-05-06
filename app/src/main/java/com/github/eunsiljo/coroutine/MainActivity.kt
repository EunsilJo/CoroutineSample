package com.github.eunsiljo.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.github.eunsiljo.coroutine.rx.RxViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SLEEP_MILLIS = 5000L
    }

    private val rxViewModel by lazy { RxViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rxViewModel.run {
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
                    throwable.printStackTrace()
                }
            )
            resultState.observe(
                this@MainActivity,
                Observer { result ->
                    Toast.makeText(this@MainActivity, result, Toast.LENGTH_SHORT).show()
                }
            )
        }

        button.setOnClickListener {
            rxViewModel.getRxResult(SLEEP_MILLIS)
        }
    }

    override fun onDestroy() {
        rxViewModel.onDestroy()
        super.onDestroy()
    }
}
