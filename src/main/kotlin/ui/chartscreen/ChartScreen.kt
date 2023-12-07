package ui.chartscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.aay.compose.baseComponents.model.GridOrientation
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.lineChart.LineChart
import com.aay.compose.lineChart.model.LineParameters
import com.aay.compose.lineChart.model.LineType
import data.DateFilter
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
            Row {

                Column(modifier = Modifier.padding(end = 36.dp).weight(1f)) {
                    BarChartSample(monthStatistic.value, model.filter.value)
                }
//                    Column(modifier = Modifier.padding(end = 36.dp).weight(1f)) {
//                        LineChartSample()
//                    }

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

    monthStatistic.forEach {
        barParameters += BarParameters(
            dataName = it.key,
            data = listOf(it.value),
            barColor = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        )
    }



    Box(Modifier.fillMaxSize()) {

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
            legendPosition = LegendPosition.TOP,
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
            barWidth = 20.dp
        )

    }
}

@Composable
fun LineChartSample() {

    val testLineParameters: List<LineParameters> = listOf(
        LineParameters(
            label = "revenue",
            data = listOf(70.0, 00.0, 50.33, 40.0, 100.500, 50.0),
            lineColor = Color.Gray,
            lineType = LineType.CURVED_LINE,
            lineShadow = true,
        ),
        LineParameters(
            label = "Earnings",
            data = listOf(60.0, 80.6, 40.33, 86.232, 88.0, 90.0),
            lineColor = Color(0xFFFF7F50),
            lineType = LineType.DEFAULT_LINE,
            lineShadow = true
        ),
        LineParameters(
            label = "Earnings",
            data = listOf(1.0, 40.0, 11.33, 55.23, 1.0, 100.0),
            lineColor = Color(0xFF81BE88),
            lineType = LineType.CURVED_LINE,
            lineShadow = false,
        )
    )

    Box(Modifier) {
        LineChart(
            modifier = Modifier.fillMaxSize(),
            linesParameters = testLineParameters,
            isGrid = true,
            gridColor = Color.Blue,
            xAxisData = listOf("2015", "2016", "2017", "2018", "2019", "2020"),
            animateChart = true,
            showGridWithSpacer = true,
            yAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray,
            ),
            xAxisStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.W400
            ),
            yAxisRange = 14,
            oneLineChart = false,
            gridOrientation = GridOrientation.VERTICAL
        )
    }
}


