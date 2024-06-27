package com.example.hazi.screen

import android.content.SharedPreferences
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.hazi.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

@Composable
fun Map(
    latLng: LatLng,
    moveAble: Boolean = false,
){
    if (MainActivity.logging) Log.d("CityMapSelectScreen", "MapCalled: $latLng")

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 10f)
    }

    var latiLongi by remember {
        mutableStateOf(latLng)
    }

    var properties by remember{
        mutableStateOf(
            MapProperties(
                isTrafficEnabled = true,
                mapType = MapType.TERRAIN
            )
        )
    }
    val coroutineScope = rememberCoroutineScope()

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapLongClick = {
            if (moveAble) {
                latiLongi = it
                val rnadom = Random(System.currentTimeMillis())
                val cameraPosition = CameraPosition.Builder()
                    .target(it)
                    .zoom(10f + rnadom.nextInt(5))
                    .tilt(30f + rnadom.nextInt(15))
                    .bearing(-45f + rnadom.nextInt(90))
                    .build()

                coroutineScope.launch {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newCameraPosition(cameraPosition), 1000
                    )
                }

            }
        },
        properties = properties //MapProperties(isMyLocationEnabled = true)
    ) {

        val geo = Geocoder(LocalContext.current)
        val city = geo.getFromLocation(latiLongi.latitude, latiLongi.longitude, 1)

        if (moveAble && !city.isNullOrEmpty()){
            val sharedPref: SharedPreferences = LocalContext.current.getSharedPreferences("sharedPref", 0)
            val editor = sharedPref.edit()
            editor.putString("city", city[0]?.locality)
            editor.putString("lat", latiLongi.latitude.toString())
            editor.putString("long", latiLongi.longitude.toString())
            editor.apply()

            if (MainActivity.logging) Log.d("Map", sharedPref.getString("city", "null")!!)
        }

        for (i in city!!) {
            if (MainActivity.logging) Log.d("Map", "CityEntity: ${i.locality}")
        }
        Marker(
            state = MarkerState(position = latiLongi),
            //Get location name
            title = city[0]?.locality,
            snippet = "Marker in ${city[0]?.locality}"
        )
    }
}
