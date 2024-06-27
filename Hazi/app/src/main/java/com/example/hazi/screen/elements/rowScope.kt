package com.example.hazi.screen.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hazi.ui.theme.tanAccent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    coilImage: String? = null,
    sunriseset: Int? = null,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    contentAlignment: Alignment = Alignment.Center,
    icon: Int? = null
) {

    Box(
        modifier = modifier
            .weight(weight)
            .fillMaxSize(),
        contentAlignment = contentAlignment
    ) {
        if (coilImage != null) {
            /*AsyncImage(
                model = coilImage,
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
            )*/
            DisplayAnimatedSvg(Modifier.size(35.dp), coilImage)
        }
        else if(sunriseset != null){

            val visible = remember { MutableStateFlow(false) }

            LaunchedEffect(key1 = true) {
                delay(1000L)
                visible.value = true
            }

            Image(
                painter = painterResource(id = sunriseset),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .size(100.dp)
                    .padding(3.dp),
                contentScale = ContentScale.Fit,
            )
        }
        else if(icon != null){
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .size(25.dp)
                    .padding(3.dp),
                tint = tanAccent
            )
        }

        else {
            Text(
                text = text,
                textAlign = textAlign,
                color = tanAccent,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}