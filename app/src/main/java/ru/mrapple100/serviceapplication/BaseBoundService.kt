package ru.mrapple100.serviceapplication

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.text.SimpleDateFormat
import java.util.Date

class BaseBoundService: Service() {

    var binder = MyLocalBinder()
    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("HH:mm:ss")
        return dateFormat.format(Date())
    }
    inner class MyLocalBinder: Binder(){
        //методы которые мы можем использовать в активити/сервисе
        fun getService(): BaseBoundService{
            return this@BaseBoundService
        }
        fun getText(){
            //
        }
    }
}