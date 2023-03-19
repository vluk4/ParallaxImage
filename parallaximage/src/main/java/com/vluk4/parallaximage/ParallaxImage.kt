package com.vluk4.parallaximage

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import coil.compose.AsyncImage
import com.vluk4.parallaximage.sensor.SensorData
import com.vluk4.parallaximage.sensor.SensorDataViewModel
import kotlinx.coroutines.flow.StateFlow

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ParallaxImage(
    model: Any?,
    modifier: Modifier = Modifier,
    depthMultiplier: Int = 20,
    shape: Shape = RectangleShape,
    contentScale: ContentScale = ContentScale.None,
    contentDescription: String? = null
) {
    val context = LocalContext.current
    val screenOrientation = LocalConfiguration.current.orientation

    val sensorDataFlow = rememberSensorData(context, screenOrientation)
    val sensorData = sensorDataFlow.collectAsState(initial = null)

    ParallaxView(
        data = sensorData.value,
        model = model,
        shape = shape,
        modifier = modifier,
        depthMultiplier = depthMultiplier,
        contentScale = contentScale,
        contentDescription = contentDescription
    )
}

@Composable
fun rememberSensorData(context: Context, screenOrientation: Int): StateFlow<SensorData?> {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val factory = remember(context) {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SensorDataViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return SensorDataViewModel(context) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
    val sensorDataViewModel: SensorDataViewModel =
        ViewModelProvider(viewModelStoreOwner, factory)[SensorDataViewModel::class.java]

    DisposableEffect(screenOrientation) {
        sensorDataViewModel.startSensorUpdates(screenOrientation)

        onDispose {
            sensorDataViewModel.stopSensorUpdates()
        }
    }

    return sensorDataViewModel.sensorData
}

@SuppressLint("UnrememberedMutableState")
@Composable
private fun ParallaxView(
    model: Any?,
    shape: Shape,
    data: SensorData?,
    depthMultiplier: Int,
    contentScale: ContentScale,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val localDensity = LocalDensity.current

    var imageHeight by remember { mutableStateOf(0.dp) }
    var imageWidth by remember { mutableStateOf(0.dp) }

    val roll by derivedStateOf { (data?.roll ?: 0f) * depthMultiplier }
    val pitch by derivedStateOf { (data?.pitch ?: 0f) * depthMultiplier }

    Box {
        AsyncImage(
            model = model,
            modifier = modifier
                .offset {
                    IntOffset(
                        x = -(roll * 1.5).dp.roundToPx(),
                        y = (pitch * 2).dp.roundToPx()
                    )
                }
                .align(Alignment.Center)
                .blur(radius = 24.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded),
            contentDescription = contentDescription,
            contentScale = contentScale
        )

        Box(
            modifier = modifier
                .offset {
                    IntOffset(
                        x = (roll * 0.9).dp.roundToPx(),
                        y = -(pitch * 0.9).dp.roundToPx()
                    )
                }
                .height(height = imageHeight)
                .width(width = imageWidth)
                .align(Alignment.Center)
                .background(
                    color = Color.White.copy(alpha = 0.3f),
                    shape = shape
                )
        )

        AsyncImage(
            model = model,
            modifier = modifier
                .offset {
                    IntOffset(
                        x = roll.dp.roundToPx(),
                        y = -pitch.dp.roundToPx()
                    )
                }
                .align(Alignment.Center)
                .onSizeChanged { size ->
                    with(localDensity) {
                        imageHeight = size.height.toDp()
                        imageWidth = size.width.toDp()
                    }
                }
                .clip(shape = shape),
            contentDescription = contentDescription,
            contentScale = contentScale
        )
    }
}
