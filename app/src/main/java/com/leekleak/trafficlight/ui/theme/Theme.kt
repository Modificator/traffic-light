package com.leekleak.trafficlight.ui.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Theme(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val darkTheme: Boolean = isSystemInDarkTheme()

    MaterialTheme (
        colorScheme =
            if (Build.VERSION.SDK_INT >= 31) {
                if (darkTheme) dynamicDarkColorScheme(context)
                else dynamicLightColorScheme(context)
            } else {
                MaterialTheme.colorScheme
            }
    ) { content() }
}

@Composable
fun Modifier.card(): Modifier {
    val viewModel: ThemeVM = viewModel()
    val improveContrast by viewModel.preferenceRepo.improveContrast.collectAsState(true)
    return this.run {
            if (improveContrast) this.shadow(2.dp, MaterialTheme.shapes.large)
            else this
        }
        .clip(MaterialTheme.shapes.large)
        .background(MaterialTheme.colorScheme.surfaceContainer)
}

@Composable
fun Modifier.navBarShadow(): Modifier {
    val viewModel: ThemeVM = viewModel()
    val improveContrast by viewModel.preferenceRepo.improveContrast.collectAsState(true)
    return this.run {
        if (improveContrast) this.shadow(4.dp, MaterialTheme.shapes.extraLarge)
        else this
    }
}
