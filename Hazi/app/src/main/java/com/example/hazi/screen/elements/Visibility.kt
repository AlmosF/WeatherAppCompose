package com.example.hazi.screen.elements

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hazi.R
import com.example.hazi.ui.theme.blueBlack
import com.example.hazi.ui.theme.tanAccent


@Composable
fun Visibility(modifier: Modifier = Modifier, visRang: Double = 0.0, context: Context) {

    var range = visRang / 1000
    Box(
        modifier = modifier
            .padding(16.dp)
            .border(2.dp, blueBlack, RoundedCornerShape(16.dp))
            .background(blueBlack.copy(0.4f), RoundedCornerShape(16.dp))
    ){
        Column {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
            ) {
                Image(
                    modifier = Modifier
                        .padding(8.dp),
                    painter = painterResource(id = R.mipmap.ic_barometer),
                    contentDescription = "icon")
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = "Visibility",
                    color = tanAccent
                )
            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = "$range km",
                    color = tanAccent
                )
            }
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = context.getString(
                        when {
                            range < 1.0 -> R.string.low_visibility
                            range > 10.0 -> R.string.high_visibility
                            else -> R.string.normal_visibility
                        }
                    ),
                    color = tanAccent,
                    modifier = Modifier
                        .padding(8.dp)
                )
            }
        }


    }

}


@Composable
@Preview
fun visibilityPreview() {
    var modifier = Modifier
    Visibility(modifier,10000.0, LocalContext.current)
}