package com.leekleak.trafficlight.ui.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.leekleak.trafficlight.services.UsageService
import com.leekleak.trafficlight.ui.navigation.NavigationManager
import com.leekleak.trafficlight.ui.permissions.Permissions
import com.leekleak.trafficlight.util.hasBackgroundPermission
import com.leekleak.trafficlight.util.hasNotificationPermission
import com.leekleak.trafficlight.util.hasUsageStatsPermission

@Composable
fun App() {
    var notificationPermission by remember { mutableStateOf(false) }
    var backgroundPermission by remember { mutableStateOf(false) }
    var usagePermission by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                notificationPermission = hasNotificationPermission(context)
                backgroundPermission = hasBackgroundPermission(context)
                usagePermission = hasUsageStatsPermission(context)

                UsageService.startService(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    if (notificationPermission && backgroundPermission && usagePermission) {
        NavigationManager()
    } else {
        Permissions(notificationPermission, backgroundPermission, usagePermission)
    }
}