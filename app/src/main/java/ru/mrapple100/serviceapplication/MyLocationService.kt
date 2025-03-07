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
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyLocationService: Service(),LocationListener {
    lateinit var locationManager: LocationManager
    lateinit var geocoder: Geocoder
    companion object{
        val INTENT_LOCATION = "location_intent"
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()

        Log.d("SENDBROID","ONCREATE")


        Log.d("SENDBROID","ONCREATE21111111111")
        GlobalScope.async {
            while (true) {
                delay(5000)
                if (ActivityCompat.checkSelfPermission(
                        this@MyLocationService,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@MyLocationService,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {

                    return@async
                }
                locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


                val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.FUSED_PROVIDER)
                val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

                val provider = when {
                    isGpsEnabled -> LocationManager.FUSED_PROVIDER
                    isNetworkEnabled -> LocationManager.NETWORK_PROVIDER
                    else -> null
                }

                GlobalScope.launch(Dispatchers.Main) {
                    locationManager.requestLocationUpdates(
                        provider!!,
                        5000,
                        5f,
                        this@MyLocationService
                    )
                    Log.d("SENDBROID", "ONCREATE2")
                }

            }
        }

    }


    override fun onLocationChanged(location: Location) {
        Log.d("SENDBROID","location.toString()")

        var intent = Intent(INTENT_LOCATION)
        intent.putExtra("lat",location.latitude)
        intent.putExtra("long",location.longitude)
        Log.d("SENDBROID",location.toString())
        sendBroadcast(intent)
    }


    override fun onDestroy() {
        super.onDestroy()
        try {
            locationManager.removeUpdates(this)
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        super.onStatusChanged(provider, status, extras)
        Log.d("STATUS",""+status)
    }

}