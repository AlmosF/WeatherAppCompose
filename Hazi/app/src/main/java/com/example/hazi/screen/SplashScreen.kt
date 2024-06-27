package com.example.hazi.screen

import android.content.Context
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hazi.R
import com.example.hazi.ui.animation.ScaleOutAnimatedText
import com.example.hazi.ui.theme.blueBlackDark
import com.example.hazi.ui.theme.tanAccent
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController
){
    val animation = LocalContext.current.getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getBoolean("animation", true)


    val scale = remember {
        androidx.compose.animation.core.Animatable(if (animation) 0f else 1.5f)
    }
    val color = remember {
        Animatable(if (animation) blueBlackDark else tanAccent)
    }

    LaunchedEffect(key1 = true) {

        if(animation) {
            scale.animateTo(
                targetValue = 1.5f,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = {
                        OvershootInterpolator(4f).getInterpolation(it)
                    }
                )
            )
            color.animateTo(
                targetValue = tanAccent,
                animationSpec = tween(
                    durationMillis = 1000,
                    easing = LinearOutSlowInEasing
                )
            )
        }
        delay(1000L)

        navController.navigate("mainmenu") {
            popUpTo("splash_screen") { inclusive = true }
        }
    }

    Box(contentAlignment = Alignment.Center){
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ){
            Image(painter = painterResource(id = R.mipmap.ic_wicon_monochrome),
                contentDescription = "Logo",
                modifier = Modifier
                    .scale(scale.value)
                    .graphicsLayer {

                        if (animation) {
                            rotationZ = scale.value * 240
                        }
                    }
            )
            if (animation){
                ScaleOutAnimatedText(
                    text = "Weather App",
                    animateOnMount = true,
                    modifier = Modifier.padding(20.dp),
                    style = TextStyle(fontSize = 45.sp, color = tanAccent)
                )
            } else {
                Text(text = "Weather App",
                    color = color.value,
                    style = TextStyle(fontSize = 45.sp, color = tanAccent),
                    modifier = Modifier
                        .padding(top = 20.dp)
                )
            }

        }
        Text(
            text = "© 2024 Hazi\nMade by:\nAlmos Fekete",
            color = color.value,
            fontSize = 15.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(15.dp)
        )
    }
}