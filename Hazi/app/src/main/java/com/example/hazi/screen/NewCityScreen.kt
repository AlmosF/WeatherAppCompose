package com.example.hazi.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.hazi.MainActivity
import com.example.hazi.ui.theme.blueBlack
import com.example.hazi.ui.theme.tanAccent

@Composable
fun AddNewCityForm(
    addNewCity: (String) -> Unit,
    dialogDismiss: () -> Unit
) {
    Dialog(onDismissRequest = dialogDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                var cityName by remember {
                    mutableStateOf("")
                }
                OutlinedTextField(
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = tanAccent
                    ),
                    value = cityName,
                    label = { Text(
                        text = "CityEntity name",
                        color = tanAccent
                    ) },
                    onValueChange = {
                        cityName = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Button(onClick = {
                    addNewCity(cityName)
                    dialogDismiss()
                }) {
                    Text(text = "Add CityEntity")
                }
            }
        }
    }
}

@Composable
fun SelectCityDialog(
    cities: List<String>,
    onCitySelected: (String) -> Unit,
    dismissRequest: () -> Unit, ) {
    val filteredCities = remember { mutableStateOf(cities) }
    val searchText = remember { mutableStateOf("") }

    Dialog(onDismissRequest = dismissRequest) {
        Surface(
            modifier = Modifier
                .height(400.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(size = 20.dp),
            color = blueBlack
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Row {
                    Text(
                        text = "Add CityEntity",
                        fontSize = 20.sp,
                        color = tanAccent
                    )
                }
                //Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = tanAccent
                    ),
                    value = searchText.value,
                    onValueChange = { newText ->
                        searchText.value = newText

                        Thread(
                            Runnable {
                                val filtered = cities.filter {
                                    it.contains(newText, ignoreCase = true)
                                }
                                filteredCities.value = filtered
                            }
                        ).start()


                    },
                    placeholder = { Text(
                        "Search CityEntity",
                        color = tanAccent
                    ) },
                    label = { Text(
                        "CityEntity",
                        color = tanAccent
                    ) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            val strokeWidth = density * 2
                            val y = size.height - strokeWidth / 2
                            drawLine(
                                Color.Black,
                                Offset(0f, y + 20),
                                Offset(size.width, y + 20),
                                strokeWidth
                            )
                        }
                )
                Spacer(modifier = Modifier
                    .height(10.dp)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    items(filteredCities.value.size) { index ->
                        val city =
                            filteredCities.value[index]
                        Text(
                            text = city,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCitySelected(city)
                                    if (MainActivity.logging) Log.d(
                                        "SelectCityDialog",
                                        "Selected city: $city"
                                    )
                                    dismissRequest()
                                }
                                .padding(end = 2.dp),
                            fontSize = 20.sp,
                            color = tanAccent
                        )
                    }
                }
            }
        }
    }
}