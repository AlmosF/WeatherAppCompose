package com.example.hazi.screen.elements

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.animation.RotateAnimation
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hazi.MainActivity
import com.example.hazi.R
import com.example.hazi.model.Wind
import com.example.hazi.screen.WindDirection
import com.example.hazi.ui.theme.blueBlack
import com.example.hazi.ui.theme.tanAccent

@Composable
fun Wind(wind: Wind = Wind(10.0f, 45.0f, 6.0f)) {
    val sensorManager = remember {
        MainActivity.sensorManager
    }

    val accelerometerSensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    val magnetometerSensor = remember {
        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
    }

    val lastAccelerometer = remember {
        FloatArray(3)
    }

    val lastMagnetometer = remember {
        FloatArray(3)
    }


    val rotationMatrix = remember {
        FloatArray(9)
    }
    val orientation = remember {
        FloatArray(3)
    }
    var isLastAccelerometerCopied by remember {
        mutableStateOf(false)
    }
    var isLastMagnetometerCopied by remember {
        mutableStateOf(false)
    }

    var lastUpdatedTime by remember {
        mutableLongStateOf(0L)
    }

    var currentDegree by remember {
        mutableFloatStateOf(0f)
    }

    val sensorEventListener = remember{
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null) {
                    if (event.sensor == accelerometerSensor) {
                        System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.size)
                        isLastAccelerometerCopied = true
                    } else if (event.sensor == magnetometerSensor) {
                        System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.size)
                        isLastMagnetometerCopied = true
                    }

                    if (isLastAccelerometerCopied && isLastMagnetometerCopied && System.currentTimeMillis() - lastUpdatedTime > 250) {
                        SensorManager.getRotationMatrix(
                            rotationMatrix,
                            null,
                            lastAccelerometer,
                            lastMagnetometer
                        )
                        SensorManager.getOrientation(rotationMatrix, orientation)

                        val azimuthInRadians = orientation[0]
                        val azimuthInDegrees = Math.toDegrees(azimuthInRadians.toDouble()).toFloat()

                        val rotateAnimation = RotateAnimation(
                            currentDegree,
                            -azimuthInDegrees,
                            RotateAnimation.RELATIVE_TO_SELF,
                            0.5f,
                            RotateAnimation.RELATIVE_TO_SELF,
                            0.5f
                        )

                        rotateAnimation.duration = 250
                        rotateAnimation.fillAfter = true


                        currentDegree = computeSmoothedRotation(currentDegree, -azimuthInDegrees, 0.5f)
                        lastUpdatedTime = System.currentTimeMillis()


                    }

                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // Do nothing
            }
        }
    }
    LaunchedEffect(Unit){
        val sensorDelay = SensorManager.SENSOR_DELAY_UI
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, sensorDelay)
        sensorManager.registerListener(sensorEventListener, magnetometerSensor, sensorDelay)
    }

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
                .drawBehind {
                    drawLine(
                        color = tanAccent,
                        start = Offset(this.size.width / 2, this.size.height + 0.0f),
                        end = Offset(this.size.width / 2, 0f),
                        strokeWidth = 5f
                    )

                },
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box (
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ){
                Icon(
                    painter = painterResource(id = R.mipmap.compass),
                    contentDescription = "nyil",
                    Modifier.size(150.dp),
                    tint = Color.White
                )
                Icon(
                    painter = painterResource(id = R.mipmap.nyil),
                    contentDescription = "nyil",
                    Modifier
                        .size(150.dp)
                        .rotate(wind.deg),
                    tint = Color.White)

                Icon(
                    painter = painterResource(id = R.mipmap.dirarr),
                    contentDescription = "nyil",
                    Modifier
                        .size(150.dp)
                        .rotate(currentDegree),
                    tint = Color.Red
                )



                Text(
                    text = WindDirection.getDirectionString(wind.deg),
                    modifier = Modifier
                        .align(Alignment.Center),
                    color = tanAccent
                )
                Text(text = stringResource(R.string.N),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = 10.dp),
                    color = Color.Red
                )
                Text(text = stringResource(R.string.S),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-10).dp),
                    color = tanAccent
                )
                Text(text = stringResource(R.string.W),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .offset(x = 35.dp),
                    color = tanAccent
                )
                Text(text = stringResource(R.string.E),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = (-35).dp),
                    color = tanAccent
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f),
                    //.padding(16.dp),
                contentAlignment = Alignment.Center,

                ){
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "${wind.speed} m/s",
                        color = tanAccent,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .drawBehind {
                                drawLine(
                                    color = tanAccent,
                                    start = Offset(0f, this.size.height + 20.0f),
                                    end = Offset(this.size.width, this.size.height + 20.0f),
                                    strokeWidth = 5f
                                )
                            }
                            .padding(horizontal = 16.dp)
                    )
                    Text(
                        text = "${wind.gust} m/s",
                        color = tanAccent,
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                            .fillMaxWidth()
                    )
                }

            }

        }
    }
}


private fun computeSmoothedRotation(previous: Float, current: Float, alpha: Float): Float {
    val beta = 1 - alpha
    return beta * previous + alpha * current
}