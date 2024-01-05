package com.practice.coroutines.ui.supervisor

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.practice.coroutines.databinding.ActivitySupervisorBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SupervisorActivity : AppCompatActivity() {
    @Inject
    lateinit var binding: ActivitySupervisorBinding

    val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.d("cvrr", "${throwable.message}")
    }

    val coroutineScope = CoroutineScope(Dispatchers.Main + coroutineExceptionHandler)


    val coroutineScopeWithSupervisor =
        CoroutineScope(Dispatchers.Main + SupervisorJob() + coroutineExceptionHandler)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            coroutineScopeWithSupervisor.launch {
                for (i in 0..10) {
                    delay(1000)
                    job1Result.append("$i   ")
                    if (i == 5)
                        throw Exception("SOmething went wrong")
                }
            }

            coroutineScopeWithSupervisor.launch {
                for (i in 20..30) {
                    delay(1000)
                    job2Result.append("$i   ")
                }
            }
        }


     }


}
