package id.ac.ukdw.drones_isai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class KarbonTerserapFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_karbon_terserap, container, false)

        val candlestickChart: CandleStickChart = view.findViewById(R.id.candleChart)
        val barChart: BarChart = view.findViewById(R.id.barChart)

        val months = arrayOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )

        // Candlestick Chart
        val candleEntries = listOf(
            CandleEntry(0f, 9f, 5f, 7f, 6f),
            CandleEntry(1f, 4f, 3f, 6f, 5f),
            CandleEntry(2f, 6f, 4f, 5f, 5.5f),
            CandleEntry(3f, 8f, 6f, 7f, 7f),
            CandleEntry(4f, 5f, 4f, 4.5f, 4.7f)
        )

        // Calculate y-axis range
        val candleMin = candleEntries.minByOrNull { it.low }?.low ?: 0f
        val candleMax = candleEntries.maxByOrNull { it.high }?.high ?: 10f

        // Fill missing data with empty entries
        val allCandleEntries = ArrayList<CandleEntry>()
        for (i in 0 until months.size) {
            val entry = if (i < candleEntries.size) candleEntries[i] else CandleEntry(
                i.toFloat(),
                0f,
                0f,
                0f,
                0f
            )
            allCandleEntries.add(entry)
        }

        val candleDataSet = CandleDataSet(allCandleEntries, "Candlestick Data")
        val candleData = CandleData(candleDataSet)
        candlestickChart.data = candleData

        val xAxisCandle = candlestickChart.xAxis
        xAxisCandle.valueFormatter = IndexAxisValueFormatter(months)
        xAxisCandle.position = XAxis.XAxisPosition.BOTTOM
        xAxisCandle.setDrawGridLines(false)
        xAxisCandle.granularity = 1f

        candlestickChart.setVisibleXRangeMaximum(5f) // Set the visible range on the x-axis

        val yAxisCandle = candlestickChart.axisLeft
        yAxisCandle.axisMinimum = candleMin - 1f
        yAxisCandle.axisMaximum = candleMax + 1f

        candlestickChart.invalidate()

// Bar Chart
        val barEntries = listOf(
            BarEntry(0f, 8f),
            BarEntry(1f, 5f),
            BarEntry(2f, 7f),
            BarEntry(3f, 6f),
            BarEntry(4f, 9f)
        )

        val dataSet1 = BarDataSet(barEntries, "Bar 1")
        dataSet1.color = ContextCompat.getColor(requireContext(), R.color.blue)

// Create a separate list of entries for dataSet2
        val dataSet2Entries = listOf(
            BarEntry(0f, 2f),
            BarEntry(1f, 4f),
            BarEntry(2f, 3f),
            BarEntry(3f, 2f),
            BarEntry(4f, 6f)
        )
        val dataSet2 = BarDataSet(dataSet2Entries, "Bar 2")
        dataSet2.color = ContextCompat.getColor(requireContext(), R.color.blue_light)

        val barData = BarData(dataSet1, dataSet2)

// Fill missing data with empty entries
        val allBarEntries = ArrayList<BarEntry>()
        for (i in 0 until months.size) {
            val entry = if (i < barEntries.size) barEntries[i] else BarEntry(i.toFloat(), 0f)
            allBarEntries.add(entry)
        }

// Set the x-axis labels for all 12 months
        val xAxisLabels = ArrayList<String>()
        for (i in 0 until months.size) {
            val label = if (i < barEntries.size) months[i] else ""
            xAxisLabels.add(label)
        }

        barData.barWidth = 0.35f
        barChart.data = barData

// Configure other properties of the bar chart as needed
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels.toTypedArray())
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.granularity = 1f

        barChart.groupBars(0f, 0.3f, 0.05f) // Adjust the spacing between groups and bars

// Set the visible range on the x-axis to show all 12 months
        barChart.setVisibleXRangeMaximum(12f)

// Set the range for the y-axis
        val yAxis = barChart.axisLeft
        yAxis.axisMinimum = 0f

        // Disable the y-axis on the right side
        barChart.axisRight.isEnabled = false

        barChart.invalidate()

        return view
    }


}