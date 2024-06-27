package com.example.hazi.screen

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.hazi.MainActivity
import com.example.hazi.R
import com.example.hazi.model.ForecastResponse
import com.example.hazi.model.ForecastUiState
import com.example.hazi.model.ForecastViewModel
import com.example.hazi.model.WeatherResult
import com.example.hazi.model.WeatherUiState
import com.example.hazi.model.WeatherViewModel
import com.example.hazi.screen.elements.DisplayAnimatedSvg
import com.example.hazi.ui.theme.Purple40
import com.example.hazi.ui.theme.blueBlack
import com.example.hazi.ui.theme.tanAccent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.android.gms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs

@Composable
fun Wscreen(
    weatherViewModel: WeatherViewModel = viewModel(),
    forecastViewModel: ForecastViewModel = viewModel(),
    modifier: Modifier = Modifier
){
    when (weatherViewModel.weatherUiState) {
        is WeatherUiState.Loading ->{
            if (MainActivity.logging) Log.d("WeatherUiState", "Loading")
            when (forecastViewModel.forecastUiState) {
                is ForecastUiState.Loading -> LoadingScreen(modifier)
                is ForecastUiState.Success -> LoadingScreen(modifier)
                is ForecastUiState.Error -> ErrorScreen(modifier, (forecastViewModel.forecastUiState as ForecastUiState.Error).error)
            }
        }
        is WeatherUiState.Success -> {
            when (forecastViewModel.forecastUiState){
                is ForecastUiState.Success -> {
                    if (MainActivity.logging) Log.d("WeatherUiState", "Success")
                    WeatherDetails(
                    (weatherViewModel.weatherUiState as WeatherUiState.Success).weatherResult,
                    (forecastViewModel.forecastUiState as ForecastUiState.Success).forecastResult,
                    modifier = modifier
                )}
                is ForecastUiState.Loading -> LoadingScreen(modifier)
                is ForecastUiState.Error -> ErrorScreen(modifier, (forecastViewModel.forecastUiState as ForecastUiState.Error).error)
            }
        }
        is WeatherUiState.Error -> {
            ErrorScreen(modifier, (weatherViewModel.weatherUiState as WeatherUiState.Error).error)
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components{
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
        Image(
            painter = rememberAsyncImagePainter(R.mipmap.pedro, imageLoader),
            contentDescription = "Loading",
            modifier = Modifier.size(100.dp)
        )
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier, error: String? = null) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components{
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(R.mipmap.pedro, imageLoader),
                contentDescription = "Loading",
                modifier = Modifier.size(100.dp)
            )
            Text(
                "Error: $error",
                color = Color.Red,
            )
        }

    }
}

@Composable
fun WeatherDetails(
    weatherResult: WeatherResult,
    forecastResult: ForecastResponse,
    modifier: Modifier = Modifier
) {
    val tabItems = listOf("Main", "More", "Map")
    val unit = if(LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("unit", true)){
        "°C"
    } else {
        "°F"
    }

    Image(painter = painterResource(id = CustomBG(weatherResult)),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row (
            modifier = Modifier
                .fillMaxSize()
                .weight(2f)
        ){
            Box(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .weight(2f)
                    .fillMaxSize()
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Transparent),
                ) {
                    Text(
                        text = weatherResult.name,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.white),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                            .border(
                                1.dp,
                                colorResource(id = R.color.blueBlack),
                                RoundedCornerShape(25.dp)
                            )
                            .background(blueBlack.copy(0.5f), RoundedCornerShape(25.dp))
                            .clip(RoundedCornerShape(25.dp))
                            .weight(1f)
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)

                    )
                    Text(
                        text = weatherResult.weather[0].description,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.white),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                            .border(
                                1.dp,
                                colorResource(id = R.color.blueBlack),
                                RoundedCornerShape(25.dp)
                            )
                            .clip(RoundedCornerShape(25.dp))
                            .background(blueBlack.copy(0.5f), RoundedCornerShape(25.dp))
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                            .weight(1f)
                    )
                    Text(
                        text = String.format("%.1f", weatherResult.main.temp_max) + unit + " / " +
                                String.format("%.1f", weatherResult.main.temp_min) + unit +
                                " Feels Like " +
                                String.format("%.1f", weatherResult.main.feels_like) + unit,
                        fontSize = 15.sp,
                        color = colorResource(id = R.color.white),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                            .border(
                                1.dp,
                                colorResource(id = R.color.blueBlack),
                                RoundedCornerShape(25.dp)
                            )
                            .clip(RoundedCornerShape(25.dp))
                            .background(blueBlack.copy(0.5f), RoundedCornerShape(25.dp))
                            .weight(1f)
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    )
                    val sdf = SimpleDateFormat("E, HH:mm", Locale.getDefault())
                    val sign = if(weatherResult.timezone >= 0) "+" else "-"
                    val absTimezone = abs(weatherResult.timezone / 3600)
                    val timeZone = TimeZone.getTimeZone("GMT$sign$absTimezone")
                    sdf.timeZone = timeZone
                    Text(
                        text = sdf.format(Date(System.currentTimeMillis())),
                        fontSize = 15.sp,
                        color = colorResource(id = R.color.white),
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                            .border(
                                1.dp,
                                colorResource(id = R.color.blueBlack),
                                RoundedCornerShape(25.dp)
                            )
                            .clip(RoundedCornerShape(25.dp))
                            .background(blueBlack.copy(0.5f), RoundedCornerShape(25.dp))
                            .weight(1f)
                            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                    )

                }

            }

            DisplayAnimatedSvg(
                Modifier
                    .size(130.dp)
                    .align(Alignment.CenterVertically),
                weatherResult.weather[0].icon
            )
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .weight(5f)
                .background(color = Color.Transparent),
            color = blueBlack,
            contentColor = TabRowDefaults.contentColor, // Use MaterialTheme for backward compatibility
            content = {
                HorizontalPagerContent(tabItems, weatherResult, forecastResult)

            }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalPagerContent(tabItems: List<String>, weatherResult: WeatherResult, forecastResult: ForecastResponse) {
    val selectedTabIndex = remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = tabItems.size)

    var gpsPermission by rememberSaveable { mutableStateOf(false) }

    gpsPermission = ContextCompat.checkSelfPermission(LocalContext.current, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED

    LaunchedEffect(selectedTabIndex.intValue){
        pagerState.animateScrollToPage(selectedTabIndex.intValue)
    }
    LaunchedEffect(pagerState.currentPage){
        selectedTabIndex.intValue = pagerState.currentPage
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex.intValue,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            contentColor = tanAccent,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex.intValue]),
                    color = Purple40
                )
            }
        ) {
            tabItems.forEachIndexed { index, tab ->
                Tab(
                    text = { Text(tab) },
                    selected = index == selectedTabIndex.intValue,
                    onClick = { selectedTabIndex.intValue = index},
                    selectedContentColor = Purple40,
                    unselectedContentColor = tanAccent
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { index ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                when(index){
                    0 -> WeatherDetailsMainScreen(weatherResult, forecastResult)
                    1 -> WeatherDetailsMoreScreen(weatherResult = weatherResult, forecastResult = forecastResult)
                    2 -> if (gpsPermission) {
                        Map(LatLng(weatherResult.coord.lat, weatherResult.coord.lon))
                    } else {
                        Text("No permission")
                    }
                }
            }
        }
    }
}