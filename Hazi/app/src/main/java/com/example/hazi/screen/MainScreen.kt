package com.example.hazi.screen

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.canopas.lib.showcase.IntroShowCaseScaffold
import com.canopas.lib.showcase.ShowcaseStyle
import com.example.hazi.CityListViewModel
import com.example.hazi.MainActivity
import com.example.hazi.R
import com.example.hazi.model.CitiesItem
import com.example.hazi.ui.theme.blueBlackDark
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.IOException
import com.example.hazi.MainActivity.Companion.arrayList
import com.example.hazi.data.geoCoding.GeocodingUiState
import com.example.hazi.data.geoCoding.GeocodingViewModel
import com.example.hazi.data.geoCoding.GeocodingViewModelFactory
import com.example.hazi.model.WeatherViewModel2
import com.example.hazi.model.WeatherViewModelFactory
import com.example.hazi.preference.MySettings
import com.example.hazi.screen.elements.CardBox
import com.example.hazi.screen.test.WeatherCardP
import java.text.Normalizer
import java.text.Normalizer.Form

fun normalizeCityName(cityName: String): String {
    val temp = Normalizer.normalize(cityName, Form.NFD)
    return temp.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    latLong: String = "",
    modifier: Modifier = Modifier,
    cityListViewModel: CityListViewModel = hiltViewModel()
){

    val context = LocalContext.current
    var unit by remember { mutableStateOf(context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("unit", true)) }

    val cityList by cityListViewModel.getAllCities().collectAsState(emptyList())

    var gpsPermission by rememberSaveable { mutableStateOf(false) }

    gpsPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED

    val lat = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getString("lat", "47.4979")!!.toDouble()
    val long = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getString("long", "19.0402")!!.toDouble()

    val geocodingViewModel: GeocodingViewModel = viewModel(
        key = "$lat,$long",
        factory = GeocodingViewModelFactory(latLong = "$lat,$long")
    )

    val api_key = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getString("API_KEY", "")


    val internet = isNetworkAvailable(context)
    Log.d("Internet", internet.toString())

    Scaffold (
        modifier = modifier
            .fillMaxSize(),
        containerColor = blueBlackDark,
        bottomBar = {
            bottomAppBar(navController, cityListViewModel)
        }
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ){
            GCardBox(navController = navController, geocodingViewModel = geocodingViewModel)
            Box {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    items(cityList) {it ->

                        val weatherVM: WeatherViewModel2 = viewModel(
                            key = "${it.CityName}${unit}${internet}${api_key}", //triggers to recompose even if the unit changes, same as the GCardBox
                            factory = WeatherViewModelFactory(cityName = it.CityName+","+unit+","+api_key)
                        )
                        if (MainActivity.logging) Log.d("CityList", it.CityName+","+unit+","+api_key)
                        if (MainActivity.logging) Log.d("CityList", weatherVM.weatherUiState.toString())
                        WeatherCardP(
                            weatherViewModel2 = weatherVM,
                            navController = navController,
                            removeItem = { cityListViewModel.deleteCity(it) }
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun GCardBox(
    navController: NavController,
    geocodingViewModel: GeocodingViewModel
) {
    val context = LocalContext.current

    when(geocodingViewModel.geocodingUiState){
        is GeocodingUiState.Loading -> {
            if (MainActivity.logging) Log.d("geocodingViewModel", "Loading")
            CardBox(
                cityItem = "Loading...",
                navController = navController,
                cardBgPic = R.mipmap.clouds,
                removeItem = {},
                localCity = false
            )
        }
        is GeocodingUiState.Success -> {
            val cityName = (geocodingViewModel.geocodingUiState as GeocodingUiState.Success).geocodingResult.name

            val unit = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("unit", true)
            val internet = isNetworkAvailable(context)
            val api_key = context.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getString("API_KEY", "")
            val weatherVM: WeatherViewModel2 = viewModel(
                key = "$cityName$unit$api_key$internet",
                factory = WeatherViewModelFactory(cityName = "$cityName,$unit,$api_key")
            )
            WeatherCardP(
                weatherViewModel2 = weatherVM,
                navController = navController,
                removeItem = {},
                local = false
            )
        }
        is GeocodingUiState.Error -> {
            CardBox(
                cityItem = "Budapest",
                navController = navController,
                cardBgPic = R.mipmap.clouds,
                removeItem = {},
                localCity = false
            )
        }
    }
}

@Composable
fun bottomAppBar(navController: NavController, cityListViewModel: CityListViewModel) {

    val coroutineScope = rememberCoroutineScope()

    var gpsPermission by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current


    gpsPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED
    val store = MySettings(context)

    val isFirstStart = store.isFirstStart.collectAsState(initial = true)

    var showAddDialog by rememberSaveable {
        mutableStateOf(false)
    }

    IntroShowCaseScaffold(
        showIntroShowCase = isFirstStart.value,
        onShowCaseCompleted = {
            coroutineScope.launch {
                store.saveFirstStart(false)
            }
        }) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            FloatingActionButton(
                onClick = {
                    navController.navigate("settings")
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(56.dp)
                    .introShowCaseTarget(
                        index = 0,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = Color(0xFF00FF00), // specify color of background
                            backgroundAlpha = 0.5f, // specify transparency of background
                            targetCircleColor = Color.White // specify color of target circle
                        ),
                        content = {
                            Column {
                                Text(
                                    text = "Settings",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Click here to see the settings",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }

                    )
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = blueBlackDark
                )
            }

            FloatingActionButton(
                onClick = {
                    navController.navigate("apod")
                },
                modifier = Modifier
                    .padding(16.dp)
                    .size(50.dp)
                    .introShowCaseTarget(
                        index = 1,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = Color(0xFF00FF00), // specify color of background
                            backgroundAlpha = 0.5f, // specify transparency of background
                            targetCircleColor = Color.White // specify color of target circle
                        ),
                        content = {
                            Column {
                                Text(
                                    text = "Astronomy Picture of the Day",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "This is provided by NASA, you can see the picture of the day here",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }

                    )
            ){

                Icon(
                    Icons.Default.Favorite,
                    contentDescription = "APOD",
                    tint = blueBlackDark
                )

            }


            if (gpsPermission) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("map")
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .size(50.dp)
                        .introShowCaseTarget(
                            index = 2,
                            style = ShowcaseStyle.Default.copy(
                                backgroundColor = Color(0xFF00FF00), // specify color of background
                                backgroundAlpha = 0.5f, // specify transparency of background
                                targetCircleColor = Color.White // specify color of target circle
                            ),
                            content = {
                                Column {
                                    Text(
                                        text = "Map",
                                        color = Color.White,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Click here to see the map",
                                        color = Color.White,
                                        fontSize = 16.sp
                                    )
                                }
                            }

                        )
                ) {
                    Icon(
                        ImageVector.vectorResource(id = R.drawable.ic_map_icon),
                        contentDescription = "Home",
                        tint = blueBlackDark
                    )
                }
            }
            FloatingActionButton(
                onClick = {
                    showAddDialog = true
                },
                modifier = Modifier
                    .padding(16.dp)
                    .introShowCaseTarget(
                        index = 3,
                        style = ShowcaseStyle.Default.copy(
                            backgroundColor = Color(0xFF00FF00), // specify color of background
                            backgroundAlpha = 0.5f, // specify transparency of background
                            targetCircleColor = Color.White // specify color of target circle
                        ),
                        content = {
                            Column {
                                Text(
                                    text = "Add CityEntity",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Click here to add a city",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }

                    )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add CityEntity",
                    tint = blueBlackDark
                )
            }

            if (showAddDialog) {
                SelectCityDialog(
                    cities = arrayList,
                    onCitySelected = { cityName ->
                        coroutineScope.launch {
                            cityListViewModel.addCity(cityName)
                        }
                    },
                    dismissRequest = { showAddDialog = false }
                )
            }

        }
    }
}

fun readJsonFile(context: Context, resourceId: Int): List<CitiesItem> {
    if (MainActivity.logging) Log.d("JSON read", "readJsonFile: $resourceId")
    val locations = mutableListOf<CitiesItem>()

    try {
        val inputStream = context.resources.openRawResource(resourceId)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()

        val json = String(buffer, Charsets.UTF_8)
        val jsonArray = JSONArray(json)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val location = CitiesItem(
                jsonObject.getString("name"),
                jsonObject.getString("state_code"),
                jsonObject.getString("state_name"),
                jsonObject.getString("country_code"),
                jsonObject.getString("country_name")
            )
            locations.add(location)
        }
    } catch (e: IOException) {
        e.printStackTrace()
        if (MainActivity.logging) Log.e("MainActivity", "Error reading JSON file: " + e.localizedMessage)
    }

    return locations
}
