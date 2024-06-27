package com.example.hazi.screen

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ComponentRegistry
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.example.hazi.model.APODResult
import com.example.hazi.model.APODUiState
import com.example.hazi.model.APODViewModel
import com.example.hazi.ui.theme.tanAccent
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.rememberFlipController
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ApodScreen(
    modifier: Modifier = Modifier,
    apodViewModel: APODViewModel = viewModel()
) {
    var date by remember { mutableStateOf(LocalDate.now()) }
    var isDatePickerDialogShowing by remember { mutableStateOf(false) }


    if (isDatePickerDialogShowing) {
        ShowDatePickerDialog { selectedDate ->
            date = selectedDate
            apodViewModel.getAPOD(date.toString())
            isDatePickerDialogShowing = false
        }
    }
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            tint = tanAccent,
            imageVector = Icons.Default.DateRange,
            contentDescription = "Select date",
            modifier = Modifier
                .padding(8.dp)
                .size(34.dp)
                .clickable {
                    isDatePickerDialogShowing = true
                }
        )
        Text(
            color = tanAccent,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(8.dp),
            text = date.toString()
        )

        when (apodViewModel.apodUiState) {

            is APODUiState.Loading -> {
                LoadingScreen(modifier.fillMaxWidth())
            }

            is APODUiState.Success -> {
                flip(apodResult = (apodViewModel.apodUiState as APODUiState.Success).apodResult)
            }

            is APODUiState.Error -> {
                ErrorScreen(modifier, (apodViewModel.apodUiState as APODUiState.Error).error)
            }

        }
    }



}


@Composable
fun flip(apodResult: APODResult){

    val flipController = rememberFlipController()


    val density = LocalContext.current.resources.displayMetrics.density

    Flippable(

        modifier = Modifier
            .padding(16.dp),
        flipController = flipController,
        frontSide = {
                    NetworkImage(
                        url = apodResult.url,
                        contentDescription = apodResult.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(8.dp))
                            .border(1.dp, tanAccent, RoundedCornerShape(8.dp))
                    )
        },
        backSide = {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Text(text = apodResult.explanation, color = tanAccent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .border(1.dp, tanAccent, RoundedCornerShape(8.dp)))
            }

        },

    )


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowDatePickerDialog(onDateSelected: (LocalDate) -> Unit) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val dialog = DatePickerDialog(context, { _, year, month, dayOfMonth ->
            onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
        }, LocalDate.now().year, LocalDate.now().monthValue - 1, LocalDate.now().dayOfMonth)
        dialog.show()

        onDispose {
            dialog.dismiss()
        }
    }
}

@Composable
fun ApodDetails(
    modifier: Modifier = Modifier,
    apodResult: APODResult
){
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, tanAccent, RoundedCornerShape(8.dp))
                .clip(shape = RoundedCornerShape(8.dp)),
            horizontalArrangement = Arrangement.Center
        ){
            Text(
                modifier = Modifier
                    .padding(8.dp),
                color = tanAccent,
                text = apodResult.date
            )
        }
        NetworkImage(
            url = apodResult.url,
            contentDescription = apodResult.title,
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(8.dp))
                .border(1.dp, tanAccent, RoundedCornerShape(8.dp))
        )
        ExpandableRow(text = apodResult.explanation)
    }

}

@Composable
fun ExpandableRow(
    text : String
) {
    var isExpanded by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, tanAccent, RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            modifier = Modifier
                .weight(5f)
                .padding(8.dp)
                .align(Alignment.CenterVertically)
                .animateContentSize(animationSpec = tween(400))
                .verticalScroll(scrollState),
            color = tanAccent,
            text = text,
            maxLines = if (isExpanded) Int.MAX_VALUE else 1
        )

        IconButton(onClick = { isExpanded = !isExpanded }) {
            Icon(

                modifier = Modifier
                    .weight(0.5f),
                tint = tanAccent,
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand"
            )
        }
    }
}

@Composable
fun NetworkImage(
    url: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components(fun ComponentRegistry.Builder.() {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            })
            .build()
    }

    val painter = rememberImagePainter(
        data = url,
        imageLoader = imageLoader,
        builder = {
            crossfade(true)
        }
    )

    Image(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}