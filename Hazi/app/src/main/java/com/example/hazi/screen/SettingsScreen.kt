package com.example.hazi.screen

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hazi.MainActivity
import com.example.hazi.R


data class ToggleableInfo(
    var isChecked: Boolean,
    var text: String
)

@SuppressLint("RememberReturnType")
@Composable
fun SettingsScreen(sharedPref : SharedPreferences){

    val unit = sharedPref.getBoolean("unit", true)
    val anim = sharedPref.getBoolean("animation", true)
    var unitSwitch by remember {
        mutableStateOf(
            ToggleableInfo(
                isChecked = sharedPref.getBoolean("unit", true),
                text = if (unit) "Metric" else "Imperial"
            )
        )
    }

    var animationSwitch by remember {
        mutableStateOf(
            ToggleableInfo(
                isChecked = sharedPref.getBoolean("animation", true),
                text = if (anim) "On" else "Off"
            )
        )
    }

    var ApiKey by remember {
        mutableStateOf(
            sharedPref.getString("API_KEY", "")
        )
    }

    Column {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.blueBlack))
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Text(
                color = colorResource(id = R.color.tanAccent),
                text = "Unit",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = density
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            Color(0xFFe3cdb3),
                            Offset(0f, y + 50),
                            Offset(size.width, y + 50),
                            strokeWidth
                        )
                    },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = 2 * density
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            Color(0xFFe3cdb3),
                            Offset(0f, y),
                            Offset(size.width, y),
                            strokeWidth
                        )
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    color = colorResource(id = R.color.tanAccent),
                    text = unitSwitch.text,
                    modifier = Modifier
                        .padding(start = 16.dp),
                    fontSize = 20.sp,
                )
                Switch(
                    checked = unitSwitch.isChecked,
                    modifier = Modifier
                        .padding(end = 16.dp),
                    onCheckedChange = {
                        unitSwitch = unitSwitch.copy(isChecked = it)
                        unitSwitch.text = if (it) "Metric" else "Imperial"
                        sharedPref.edit().putBoolean("unit", it).apply()
                        if (MainActivity.logging) Log.d("SettingsScreen", sharedPref.getBoolean("unit", true).toString())
                        if (MainActivity.logging) Log.d("SettingsScreen", unitSwitch.isChecked.toString())
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Color.Green,
                        uncheckedTrackColor = Color.Red
                    )
                )
            }

            Text(
                color = colorResource(id = R.color.tanAccent),
                text = "Animations",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = density
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            Color(0xFFe3cdb3),
                            Offset(0f, y + 40),
                            Offset(size.width, y + 40),
                            strokeWidth
                        )
                    },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = 2 * density
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            Color(0xFFe3cdb3),
                            Offset(0f, y + 40),
                            Offset(size.width, y + 40),
                            strokeWidth
                        )
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    color = colorResource(id = R.color.tanAccent),
                    text = animationSwitch.text,
                    modifier = Modifier
                        .padding(start = 16.dp),
                    style = TextStyle(
                        fontSize = 20.sp,
                        shadow = if (animationSwitch.isChecked) Shadow(
                            color = Color.Green,
                            offset = Offset(2f, 5f),
                            blurRadius = 3f
                        ) else null
                    )
                )
                Switch(
                    checked = animationSwitch.isChecked,
                    modifier = Modifier
                        .padding(end = 16.dp),
                    onCheckedChange = {
                        animationSwitch = animationSwitch.copy(isChecked = it)
                        animationSwitch.text = if (it) "On" else "Off"
                        sharedPref.edit().putBoolean("animation", it).apply()
                        if (MainActivity.logging) Log.d("SettingsScreen", sharedPref.getBoolean("animation", true).toString())
                        if (MainActivity.logging) Log.d("SettingsScreen", animationSwitch.isChecked.toString())
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Color.Green,
                        uncheckedTrackColor = Color.Red
                    )
                )

            }
            Text(
                color = colorResource(id = R.color.tanAccent),
                text = "API Key",
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = density
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            Color(0xFFe3cdb3),
                            Offset(0f, y + 50),
                            Offset(size.width, y + 50),
                            strokeWidth
                        )
                    },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = 2 * density
                        val y = size.height - strokeWidth / 2
                        drawLine(
                            Color(0xFFe3cdb3),
                            Offset(0f, y + 40),
                            Offset(size.width, y + 40),
                            strokeWidth
                        )
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = colorResource(id = R.color.tanAccent),
                        focusedBorderColor = colorResource(id = R.color.tanAccent),
                        unfocusedBorderColor = colorResource(id = R.color.tanAccent),
                        cursorColor = colorResource(id = R.color.tanAccent),
                    ),
                    value = ApiKey!!,
                    maxLines = 1,
                    label = { Text(
                        color = colorResource(id = R.color.tanAccent),
                        text = "API Key"
                    ) },
                    onValueChange = {
                        ApiKey = it
                        sharedPref.edit().putString("API_KEY", it).apply()
                    },
                    modifier = Modifier.fillMaxWidth())
            }



        }
        Spacer(modifier =
        Modifier
            .fillMaxSize()
            .weight(1f)
            .background(colorResource(id = R.color.blueBlack))
        )
    }

}