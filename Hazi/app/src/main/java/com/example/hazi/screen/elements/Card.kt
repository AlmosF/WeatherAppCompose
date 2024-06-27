package com.example.hazi.screen.elements

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.PixelCopy
import android.view.View
import android.view.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.hazi.R
import com.example.hazi.model.WeatherData
import com.example.hazi.model.WeatherResult
import com.example.hazi.screen.CustomBGByWeatherData
import com.example.hazi.screen.CustomBGByWeatherResult
import com.example.hazi.ui.AutoResizedText
import com.example.hazi.ui.theme.tanAccent
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@SuppressLint("RememberReturnType")
@Composable
fun CardBox(
    cityItem: String,
    weatherData: WeatherData? = null,
    removeItem: () -> Unit,
    navController: NavController,
    cardBgPic: Int,
    localCity : Boolean = true
) {

    var textStyleBody1 = MaterialTheme.typography.headlineMedium
    var textStyle by remember {mutableStateOf(textStyleBody1)}


    val ApiKey = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getString("API_KEY", "0").toString()

    val metImp = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("unit", true)

    var unit = if(LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("unit", true))
    {
        "°C"
    } else {
        "°F"
    }

    var currentCity = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getString("city", "Budapest").toString()


    var currentCountry = if (weatherData != null) {
        weatherData.sys.country
    } else {
        "--"
    }


    Card (
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable
            {
                if (cityItem != "Jelenlegi helyszín") {
                    navController.navigate("weatherDetails?cityName=${cityItem},${metImp},${ApiKey}" /*${cityItem.cityCountry}"*/)
                } else {
                    navController.navigate("weatherDetails?cityName=${currentCity},${metImp},${ApiKey}")
                }
            },
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        ){
            Image(
                painter = painterResource(id = CustomBGByWeatherData(weatherData)),
                contentDescription = "clouds",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize())
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            )
            {
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .weight(.8f)
                ) {
                    AsyncImage(
                        model = "https://openweathermap.org/img/wn/${weatherData?.weather?.get(0)?.icon}@2x.png",
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp)
                            .padding(start = 8.dp)
                    )
                }

                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                        .weight(1f)
                ){
                    Text(
                        text = if (weatherData != null)(weatherData?.main?.temp.toString() + unit) else "23.53°C",
                        color = tanAccent,
                        fontSize = 17.sp,
                    )
                }

                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                        .weight(2f)
                        .height(75.dp)
                ){
                    AutoResizedText(text = cityItem, style = textStyle, color = tanAccent)
                    AutoResizedText(text = currentCountry!!, style = textStyle, color = tanAccent)

                }
                if(localCity) {
                    Column(
                        modifier = Modifier
                            .weight(1.6f)

                    ) {
                        Button(
                            onClick =
                            {
                                removeItem()
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .border(2.dp, tanAccent, RoundedCornerShape(15.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        ) {
                            Text(
                                text = "Delete",
                                color = tanAccent,
                            )
                        }

                    }
                }else{
                    Column(
                        modifier = Modifier
                            .weight(1.6f)
                    ){}
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("RememberReturnType")
@Composable
fun CardBox2(
    cityItem: String,
    weatherData: WeatherResult?,
    removeItem: () -> Unit,
    navController: NavController,
    cardBgPic: Int,
    error: String = "",
    localCity : Boolean = true
) {

    var textStyleBody1 = MaterialTheme.typography.headlineMedium
    var textStyle by remember {mutableStateOf(textStyleBody1)}

    val context = LocalContext.current
    val view = LocalView.current

    val ApiKey = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getString("API_KEY", "0").toString()

    val metImp = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("unit", true)

    var unit = if(LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("unit", true))
    {
        "°C"
    } else {
        "°F"
    }

    var currentCity = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getString("city", "Budapest").toString()


    var currentCountry = if (weatherData != null) {
        weatherData.sys.country
    } else {
        "--"
    }

    var layoutCoordinatesH by remember { mutableStateOf(0) }
    var layoutCoordinatesW by remember { mutableStateOf(0) }
    var layoutCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null)}

    Card (
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .onGloballyPositioned {
                layoutCoordinatesH = it.size.height
                layoutCoordinatesW = it.size.width
                layoutCoordinates = it
            }
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(15.dp))
            /*.clickable
            {
                if (cityItem != "Jelenlegi helyszín") {
                    navController.navigate("weatherDetails?cityName=${cityItem},${metImp},${ApiKey}" /*${cityItem.cityCountry}"*/)
                } else {
                    navController.navigate("weatherDetails?cityName=${currentCity},${metImp},${ApiKey}")
                }
            },*/
            .combinedClickable(
                onClick = {
                    if (cityItem != "Jelenlegi helyszín") {
                        navController.navigate("weatherDetails?cityName=${cityItem},${metImp},${ApiKey}" /*${cityItem.cityCountry}"*/)
                    } else {
                        navController.navigate("weatherDetails?cityName=${currentCity},${metImp},${ApiKey}")
                    }
                },
                onLongClick = {
                    val location = IntArray(2)
                    view.getLocationInWindow(location)
                    val startX =
                        location[0] + (layoutCoordinates?.positionInRoot()?.x?.toInt() ?: 0)
                    val startY =
                        location[1] + (layoutCoordinates?.positionInRoot()?.y?.toInt() ?: 0)
                    captureScreenshot(
                        view,
                        (context as Activity).window,
                        startX,
                        startY,
                        layoutCoordinatesW,
                        layoutCoordinatesH
                    ) {
                        val uri = saveImage(it, context)
                        shareImage(uri, context)
                    }
                }

            )
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        ){
            Image(
                painter = painterResource(id = if (weatherData == null) R.mipmap.clouds else CustomBGByWeatherResult(weatherData)),
                contentDescription = "clouds",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize())
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            )
            {
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .weight(.8f)
                ) {
                    /*AsyncImage(
                        model = "https://openweathermap.org/img/wn/${weatherData?.weather?.get(0)?.icon}@2x.png",
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp)
                            .padding(start = 8.dp)
                    )*/
                    //DisplayGif()
                    val modifier = Modifier.size(70.dp)
                    DisplayAnimatedSvg(modifier, weatherData?.weather?.get(0)?.icon.toString())
                }

                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                        .weight(1f)
                ){
                    Text(
                        text = if (weatherData != null) String.format("%.1f", weatherData.main.temp) + unit else "23.5°C",
                        color = tanAccent,
                        fontSize = 17.sp,
                    )
                }

                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp, end = 8.dp)
                        .weight(2f)
                        .height(75.dp)
                ){
                    AutoResizedText(text = cityItem, style = textStyle, color = tanAccent)
                    AutoResizedText(text = currentCountry, style = textStyle, color = tanAccent)

                }
                if(localCity) {
                    Column(
                        modifier = Modifier
                            .weight(1.6f)

                    ) {
                        Button(
                            onClick =
                            {
                                removeItem()
                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .border(2.dp, tanAccent, RoundedCornerShape(15.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        ) {
                            Text(
                                text = "Delete",
                                color = tanAccent,
                            )
                        }

                    }
                }else{
                    Column(
                        modifier = Modifier
                            .weight(1.6f)
                    ){}
                }
            }
        }
    }
}

@Composable
fun DisplayAnimatedSvg(modifier: Modifier = Modifier, weather: String = "01n"){
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier("_${weather}", "raw", context.packageName)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resourceId))
    LottieAnimation(
        composition = composition,
        modifier = modifier,
        iterations = LottieConstants.IterateForever
        // You can control the animation by providing additional parameters here
    )
}

@Composable
fun DisplayGif(){
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
        painter = rememberAsyncImagePainter(R.drawable.rand, imageLoader),
        contentDescription = null,
        modifier = Modifier
            .size(70.dp)
            .padding(start = 8.dp)
    )
}

fun captureFullScreenshot2(window: Window, bitmapCallback: (Bitmap) -> Unit) {
    val displayMetrics = Resources.getSystem().displayMetrics
    val height = displayMetrics.heightPixels
    val width = displayMetrics.widthPixels

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Use PixelCopy for Android O and above
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        PixelCopy.request(window,
            Rect(0, 100, width, height+100),
            bitmap,
            { success ->
                if (success == PixelCopy.SUCCESS) {
                    bitmapCallback.invoke(bitmap)
                }
            },
            Handler(Looper.getMainLooper()))
    } else {
        val rootView = window.decorView.rootView
        val bitmap = Bitmap.createBitmap(rootView.width, rootView.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        rootView.draw(canvas)
        bitmapCallback.invoke(bitmap)
    }
}

fun captureScreenshot(view: View, window: Window, startX: Int, startY: Int, width: Int, height: Int, bitmapCallback: (Bitmap)->Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        PixelCopy.request(window,
            Rect(startX, startY, startX + width, startY + height),
            bitmap,
            {
                if (it == PixelCopy.SUCCESS) {
                    bitmapCallback.invoke(bitmap)
                }
            },
            Handler(Looper.getMainLooper()) )
    } else {
        val tBitmap = Bitmap.createBitmap(
            width, height, Bitmap.Config.RGB_565
        )
        val canvas = Canvas(tBitmap)
        view.draw(canvas)
        canvas.setBitmap(null)
        bitmapCallback.invoke(tBitmap)
    }
}

fun saveImage(bitmap: Bitmap, context: Context): Uri {
    val imagesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val image = File(imagesDir, "screenshot.png")
    try {
        val fileOutputStream = FileOutputStream(image)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", image)
}

fun shareImage(uri: Uri, context: Context) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "image/*"
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    context.startActivity(Intent.createChooser(intent, "Share"))
}
