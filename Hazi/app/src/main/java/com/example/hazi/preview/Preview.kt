package com.example.hazi.preview

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.hazi.screen.AddNewCityForm

import kotlinx.coroutines.launch


@Composable
//@Preview
fun Preview() {
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    AddNewCityForm(
        addNewCity = { cityName ->
            coroutineScope.launch {
                Log.d("MainScreen", "addNewCity: $cityName")
            }
        },
        dialogDismiss = { showAddDialog = false })

}
