package com.vluk4.parallaximage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.vluk4.parallaximage.ui.theme.ParallaxImageTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParallaxImageTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colors.background),
                    contentAlignment = Alignment.Center
                ) {
                    ParallaxImage(
                        model = R.drawable.image_sample,
                        modifier = Modifier.scale(0.8f),
                        contentScale = ContentScale.Fit,
                        shape = RoundedCornerShape(16.dp),
                        depthMultiplier = 20
                    )
                }
            }
        }
    }
}