package com.leekleak.trafficlight.ui.history

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leekleak.trafficlight.R
import com.leekleak.trafficlight.charts.LineGraph
import com.leekleak.trafficlight.database.AppUsage
import com.leekleak.trafficlight.ui.theme.card
import com.leekleak.trafficlight.util.categoryTitle
import com.leekleak.trafficlight.util.px
import java.time.LocalDate
import kotlin.math.roundToInt

@Composable
fun AppDataUsage(date: LocalDate, paddingValues: PaddingValues) {
    val viewModel: HistoryVM = viewModel()
    val list by remember(date) { viewModel.getAllAppUsage(date) }.collectAsState(initial = listOf())
    val maximum = list.maxOfOrNull { it.usage.totalWifi + it.usage.totalCellular } ?: 0
    var selected by remember { mutableIntStateOf(-1) }

    LazyColumn (
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        categoryTitle(R.string.app_usage)
        itemsIndexed(list) { index, item ->
            AppItem(item, index, selected, maximum) { selected = it }
        }
    }
}

@Composable
fun AppItem(
    appUsage: AppUsage,
    i: Int,
    selected: Int,
    maximum: Long,
    onClick: (i: Int) -> Unit,
) {
    val totalWifi = appUsage.usage.totalWifi
    val totalCellular = appUsage.usage.totalCellular

    Column (Modifier.card()) {
        Column (Modifier.clickable { onClick(if (selected != i) i else -1) }) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val width = 32.dp.px.roundToInt()
                val bitmap = appUsage.drawable?.toBitmap(width, width)
                bitmap?.let { Image(bitmap = it.asImageBitmap(), contentDescription = null) }

                AnimatedContent(selected == i) { selected ->
                    if (!selected) {
                        LineGraph(
                            maximum = maximum,
                            data = Pair(totalWifi, totalCellular)
                        )
                    } else {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colorScheme.background)
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            DataBadge(
                                iconId = R.drawable.wifi,
                                description = stringResource(R.string.wifi),
                                bgTint = MaterialTheme.colorScheme.primary,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                value = totalWifi
                            )
                            DataBadge(
                                iconId = R.drawable.cellular,
                                description = stringResource(R.string.cellular),
                                bgTint = MaterialTheme.colorScheme.tertiary,
                                tint = MaterialTheme.colorScheme.onTertiary,
                                value = totalCellular
                            )
                        }
                    }
                }

            }
            AnimatedContent(selected == i) { selected ->
                if (selected) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = appUsage.name + "\n" + appUsage.appInfo.packageName
                        )
                    }
                }
            }
        }
    }
}