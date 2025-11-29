package com.leekleak.trafficlight.ui.theme

import androidx.lifecycle.ViewModel
import com.leekleak.trafficlight.model.PreferenceRepo
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class ThemeVM : ViewModel(), KoinComponent {
    val preferenceRepo: PreferenceRepo by inject()
}