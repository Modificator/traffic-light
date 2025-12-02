package com.leekleak.trafficlight.ui.history

import androidx.lifecycle.ViewModel
import com.leekleak.trafficlight.database.DayUsage
import com.leekleak.trafficlight.database.HourlyUsageRepo
import com.leekleak.trafficlight.database.HourUsage
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate


class HistoryVM: ViewModel(), KoinComponent {
    private val hourlyUsageRepo: HourlyUsageRepo by inject()

    fun dayUsage(date: LocalDate): DayUsage =
        hourlyUsageRepo.calculateDayUsage(date)

    fun dayUsageBasic(date: LocalDate): DayUsage =
        hourlyUsageRepo.calculateDayUsageBasic(date)

    val getMaxCombinedUsage: Flow<Long> = hourlyUsageRepo.getMaxCombinedUsage()
}