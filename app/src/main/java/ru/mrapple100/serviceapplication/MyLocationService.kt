package ru.mrapple100.serviceapplication

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat

class MyLocationService: Service(),LocationListener {
    lateinit var locationManager: LocationManager
    lateinit var geocoder: Geocoder
    companion object{
        val INTENT_LOCATION = "location_intent"
    }

    var binder = MyLocalBinder()
    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        getLocation()
    }

    fun getLocation(){
        Log.d("SENDBROID","ONCREATE")

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        Log.d("SENDBROID","ONCREATE21111111111")


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0f, this)

        Log.d("SENDBROID","ONCREATE2")
    }

    override fun onLocationChanged(location: Location) {
        Log.d("SENDBROID","123")
        var intent = Intent(INTENT_LOCATION)
        intent.putExtra("lat",location.latitude)
        intent.putExtra("long",location.longitude)

        Log.d("SENDBROID",location.toString())
        sendBroadcast(intent)
    }

    inner class MyLocalBinder: Binder(){
        //методы которые мы можем использовать в активити/сервисе
        fun getService(): MyLocationService{
            return this@MyLocationService
        }

    }

}