package ui.chartscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.aay.compose.baseComponents.model.LegendPosition
import data.DateFilter
import data.ViewModels.ChartScreenModel
import ui.limitscreen.LimitDatePicker
import ui.utils.ScreenContent
import kotlin.random.Random


@Composable
fun ChartScreen(
    model: ChartScreenModel
) {
    val monthStatistic = model.currentMonthStatstics.collectAsState(emptyMap<String, Double>())

    ScreenContent {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Statystyki",
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.weight(1f).clickable { }
                )
                Box(
                    modifier = Modifier.padding(vertical = 24.dp, horizontal = 36.dp)
                ) {
                    LimitDatePicker(
                        selectedMonth = model.filter.value.selectedMonth,
                        selectedYear = model.filter.value.selectedYear,
                        onClick = { month, year -> model.setFilter(month, year) },
                    )
                }
            }
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ){
                    BarChartSample(monthStatistic.value, model.filter.value)
            }
        }
    }
}


@Composable
fun BarChartSample(
    monthStatistic: Map<String, Double>,
    dataFilter: DateFilter
) {

    val barParameters: MutableList<BarParameters> = mutableListOf()
    val legendItems: MutableMap<String, Color> = mutableMapOf()

    monthStatistic.forEach {

        val color = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))

        barParameters += BarParameters(
            dataName = it.key,
            data = listOf(it.value),
            barColor = color
        )
        legendItems[it.key] = color
    }



    Box(modifier = Modifier
        .fillMaxSize()
        ) {
        Box(

        ) {

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
//                    .weight(1f)
                    .fillMaxSize()
            ){
                BarChart(
                    chartParameters = barParameters,
                    descriptionStyle = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.outline,
                        fontWeight = FontWeight.Thin
                    ),
                    gridColor = Color.DarkGray,
                    xAxisData = listOf("${dataFilter.selectedYear}-${dataFilter.selectedMonth}"),
                    isShowGrid = false,
                    animateChart = true,
                    showGridWithSpacer = true,
                    legendPosition = LegendPosition.DISAPPEAR,
                    yAxisStyle = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.outline,
                        fontWeight = FontWeight.Medium
                    ),
                    xAxisStyle = TextStyle(
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.outline,
                        fontWeight = FontWeight.Medium
                    ),
                    yAxisRange = 15,
                    barWidth = 120.dp
                )
            }
            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 16.dp, end = 36.dp)
                    .align(Alignment.CenterEnd)
//                    .weight(0.2f)
            ) {
                items(legendItems.toList()) { (category, color) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.height(60.dp)
                        ) {
                        Box(modifier = Modifier.height(60.dp)){
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(color = color)
                                    .size(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Box(modifier = Modifier.height(60.dp)){
                            Text(
                                text = category,
//                            modifier = Modifier.padding(start = 10.dp, bottom = 10.dp),
                                style = MaterialTheme.typography.displaySmall.copy(
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                )
                            )
                        }
                    }
                }
            }
        }

    }
}


