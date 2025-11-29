package com.leekleak.trafficlight.ui.navigation

import androidx.lifecycle.ViewModel
import com.leekleak.trafficlight.database.HourlyUsageRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class NavigationManagerVM : ViewModel(), KoinComponent {
    val hourlyUsageRepo: HourlyUsageRepo by inject()
}