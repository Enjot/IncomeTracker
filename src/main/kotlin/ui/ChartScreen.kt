package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.cash.sqldelight.db.QueryResult
import com.aay.compose.barChart.BarChart
import com.aay.compose.barChart.model.BarParameters
import com.aay.compose.baseComponents.model.LegendPosition
import com.aay.compose.donutChart.DonutChart
import com.aay.compose.donutChart.PieChart
import com.aay.compose.donutChart.model.PieChartData
import kotlin.random.Random


@Composable
fun ChartScreen(
    monthStatistic: Map<String,Double>,
    dateFilter: DateFilter,
    setStatisticsFilter: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
        .background(Color.White)
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ){
        Box(modifier = Modifier.padding(start = 36.dp)){
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
                            selectedMonth = dateFilter.selectedMonth,
                            selectedYear = dateFilter.selectedYear,
                            onClick = { month, year -> setStatisticsFilter(month, year) },
                        )
                    }
                }
                Row (modifier = Modifier.fillMaxSize().padding(end = 36.dp)){
//                    DonutChartSample(monthStatistic)
                    BarChartSample(monthStatistic, dateFilter)
                }
            }
        }
    }

}



@Composable
fun BarChartSample(
    monthStatistic: Map<String,Double>,
    dataFilter: DateFilter
) {

    val barParameters: MutableList<BarParameters> = mutableListOf()

    monthStatistic.forEach {
        barParameters += BarParameters(
            dataName = it.key,
            data = listOf(it.value),
            barColor = Color(Random.nextInt(256), Random.nextInt(256),Random.nextInt(256))
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
                barWidth = 20.dp
            )

    }
}


