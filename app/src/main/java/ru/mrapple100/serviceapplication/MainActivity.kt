package ru.mrapple100.serviceapplication

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import ru.mrapple100.serviceapplication.ui.theme.ServiceApplicationTheme
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {

    lateinit var context: Context
    var isBound = false
    lateinit var boundService: BaseBoundService
    val locationArray = arrayListOf(0.0,0.0)

    val myConnection = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val binder = service as BaseBoundService.MyLocalBinder

            boundService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
        }

    }
    val receiver = object : BroadcastReceiver(){
        override fun onReceive(p0: Context?, intent: Intent?) {
            locationArray[0] = intent?.getDoubleExtra("lat",0.0)!!
            locationArray[1] = intent?.getDoubleExtra("long",0.0)!!
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        launcher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION))

        val intent = Intent(this,BaseBoundService::class.java)
        bindService(intent,myConnection, BIND_AUTO_CREATE)
        val intent2 = Intent(this, MyLocationService::class.java)
        startService(intent2)



        val stateText = mutableStateOf("time")
        val stateLocation = mutableStateOf("0/0")

        thread {
            while(true){
                Thread.sleep(1000)
                if(isBound) {
                    stateText.value = boundService.getCurrentTime()
                }
              //  startService(intent2)

                stateLocation.value = "${locationArray[0]} / ${locationArray[1]}"
            }
        }

        setContent {
            ServiceApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(stateText,stateLocation,context)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, IntentFilter(MyLocationService.INTENT_LOCATION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    override fun onStop() {
        super.onStop()
        unbindService(myConnection)
    }
    val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(),
        ActivityResultCallback {

        })
}

@Composable
fun Greeting(name: MutableState<String>,locationState:MutableState<String>, context: Context, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name.value,
            modifier = modifier
        )
        Text(
            text = locationState.value,
            modifier = modifier)
        Button(
            onClick = {
                val intent = Intent(context, BaseService::class.java)
                context.startService(intent)
            }) {
            Text(text = "Запуск Сервиса")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ServiceApplicationTheme {
//        Greeting("Android")
//    }
//}