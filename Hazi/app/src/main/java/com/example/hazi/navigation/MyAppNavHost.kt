package com.example.hazi.navigation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.hazi.MainActivity
import com.example.hazi.screen.ApodScreen
import com.example.hazi.screen.CityMapSelectScreen
import com.example.hazi.screen.MainScreen
import com.example.hazi.screen.SettingsScreen
import com.example.hazi.screen.Wscreen
import com.example.hazi.screen.SplashScreen
import com.example.hazi.ui.theme.blueBlackDark
import com.google.android.gms.location.LocationServices


@RequiresApi(Build.VERSION_CODES.O)
@Composable

fun MyAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "mainmenu"
){
    var animation = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("animation", true)
    var unit = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("unit", true)

    var latLng by remember { mutableStateOf("47.497913,19.040236") }

    val latLong = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getString("lat", "47.497913") + "," + LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getString("long", "19.040236")

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(LocalContext.current)
    if (ActivityCompat.checkSelfPermission(LocalContext.current, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(LocalContext.current, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        val task = fusedLocationClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val long = location.longitude
                latLng = "$lat,$long"
                if (MainActivity.logging) Log.d("MyAppNavHost", "latLong: $latLong")
            }
        }
    }
    NavHost(
        modifier = modifier
            .background(blueBlackDark)
        ,
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            if (animation)
            {
                fadeIn(animationSpec = tween(600)) +
                        scaleIn(
                            animationSpec = tween(1000),
                            transformOrigin = TransformOrigin.Center,

                            )
            }
            else{
                EnterTransition.None
            }
        },
        exitTransition = {
            if (animation){
                fadeOut(animationSpec = tween(1000)) +
                        scaleOut(
                            animationSpec = tween(1000),
                            transformOrigin = TransformOrigin.Center,

                            )
            }
            else{
                ExitTransition.None
            }
        }
    ){
        composable("splash_screen"){
            SplashScreen(navController = navController)
        }
        composable("mainmenu"){
            animation = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("animation", true)
            MainScreen(/*onNavigateToWeather = {navController.navigate("weather?upperBound=3")},*/
                navController = navController,
                latLong = latLong
            )
        }
        composable("weatherDetails?cityName={cityName}",
            arguments = listOf(navArgument("cityName"){
                defaultValue = "Budapest"
                type = NavType.StringType
            })
        ){
            animation = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("animation", true)
            val city = it.arguments?.getString("cityName")
            if (MainActivity.logging) Log.d("WeatherDetailsFragment", "WeatherDetailsFragmentt: $city")
            city?.let{
                Wscreen()
            }
        }
        composable("settings"){
            animation = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("animation", true)
            if (MainActivity.logging) Log.d("SettingsScreen", "SettingsScreen")
            SettingsScreen(sharedPref = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE))
        }

        composable("apod"){
            animation = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("animation", true)
            ApodScreen(modifier.fillMaxSize())
        }

        composable("map"){
            animation = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("animation", true)
            CityMapSelectScreen()
        }

    }

}

