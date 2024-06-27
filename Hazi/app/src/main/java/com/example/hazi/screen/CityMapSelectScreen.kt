package com.example.hazi.screen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

@Composable
fun CityMapSelectScreen() {
    val context = LocalContext.current
    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)


    val latitude = context.getSharedPreferences("sharedPref", 0).getString("lat", "47.497913")?.toDouble() ?: 47.497913
    val longitude = context.getSharedPreferences("sharedPref", 0).getString("long", "19.040236")?.toDouble() ?: 19.040236


    var latLng by remember { mutableStateOf(LatLng(latitude, longitude)) } // Default location
    var loading by remember { mutableStateOf(true) } // Loading state

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 100)
    } else {
        LaunchedEffect(Unit) {
            val task = fusedLocationClient.lastLocation
            task.addOnSuccessListener { location ->
                if (location != null) {
                    latLng = LatLng(location.latitude, location.longitude)
                    Log.d("CityMapSelectScreen", "Location: $latLng")
                    loading = false // Set loading to false once the location is retrieved
                    context.getSharedPreferences("sharedPref", 0).edit().putString("lat", location.latitude.toString()).apply()
                    context.getSharedPreferences("sharedPref", 0).edit().putString("long", location.longitude.toString()).apply()
                }
            }
        }
    }
    if (!loading) {
        Map(latLng = latLng, moveAble = true)
    }
}