package com.example.hazi

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hazi.model.CitiesItem
import com.example.hazi.navigation.MyAppNavHost
import com.example.hazi.screen.readJsonFile
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint

//Ez nem volt itt
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var loc : List<CitiesItem>

    companion object{
        lateinit var arrayList : ArrayList<String>
        lateinit var sensorManager: SensorManager
        val logging = false
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arrayList = ArrayList()

        loc = readJsonFile(this, R.raw.cities)

        for (i in loc){
            arrayList.add(i.name+", "+i.country_name)
        }

        //requestPermission(this)
        requestPermission2()

        //lastLocation()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        setContent {
            MyAppNavHost(modifier = Modifier.fillMaxSize(), startDestination = "splash_screen")

        }
    }

    private fun lastLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            val task = fusedLocationClient.lastLocation
            task.addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val long = location.longitude
                    if (logging) Log.d("MyAppNavHost", "latLong: $lat,$long")
                    getSharedPreferences("sharedPref", 0).edit().putString("lat", lat.toString()).apply()
                    getSharedPreferences("sharedPref", 0).edit().putString("long", long.toString()).apply()
                }
            }
        }
    }

    private fun requestPermission2() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission to get your current location. Please grant this permission.")
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                            100
                        )
                    }
                    .create()
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    100
                )
            }
        } else {
            getLastLocation()
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation()
        }
    }

    private fun getLastLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val task = fusedLocationClient.lastLocation
            task.addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val long = location.longitude
                    if (logging) Log.d("MainActivity", "Lat: $lat, Long: $long")
                    getSharedPreferences("sharedPref", MODE_PRIVATE).edit().apply {
                        putString("lat", lat.toString())
                        putString("long", long.toString())
                        apply()
                    }
                }
            }
        }
    }


}



fun requestPermission(context : Activity){
    if(ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED){
        if(ActivityCompat.shouldShowRequestPermissionRationale(context, android.Manifest.permission.ACCESS_FINE_LOCATION)){
            ActivityCompat.requestPermissions(context, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }else{
            ActivityCompat.requestPermissions(context, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HaziTheme {
        MyAppNavHost(modifier = Modifier.fillMaxSize())
    }
}*/