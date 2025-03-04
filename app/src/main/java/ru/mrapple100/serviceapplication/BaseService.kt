package ru.mrapple100.serviceapplication

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class BaseService: Service() {



    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        runBlocking(Dispatchers.IO) {
            var i = 3
            while (i > 0) {
                delay(5000)
                Log.d("Console", "From Service $i ")
                i--
            }
        }

        return START_STICKY
    }
}