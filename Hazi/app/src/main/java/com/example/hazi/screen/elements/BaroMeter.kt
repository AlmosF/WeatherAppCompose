package com.example.hazi.screen.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hazi.R
import com.example.hazi.screen.forgatas
import com.example.hazi.ui.theme.blueBlack
import com.example.hazi.ui.theme.tanAccent

@Composable
fun BaroMeter(pressure: Double){
    val pair = forgatas(pressure, LocalContext.current)
    Box(
        modifier = Modifier
            .padding(16.dp)
            .border(2.dp, blueBlack, RoundedCornerShape(16.dp))
            .background(blueBlack.copy(0.4f), RoundedCornerShape(16.dp))
            .fillMaxWidth(),
    ){
        Row (
            modifier = Modifier
                .padding(16.dp)
        ){
            Box(){
                Image(
                    painter = painterResource(id = R.mipmap.barometer),
                    contentDescription = "barometer",
                    Modifier.size(150.dp)
                )

                Image(
                    painter = painterResource(id = R.mipmap.baroarrow),
                    contentDescription = "nyil",
                    Modifier
                        .size(150.dp)
                        .rotate(pair.first.toFloat()),
                )
                Text(text = "${pressure.toLong()} hPa",
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    color = pair.second
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 30.dp),
                contentAlignment = Alignment.Center,

                ){
                Text(
                    text = pair.third,
                    color = tanAccent,
                    fontSize = 17.sp,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
@Preview
fun BaroMeterPreview(){
    BaroMeter(1018.0)
}