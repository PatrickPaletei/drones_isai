package id.ac.ukdw.drones_isai

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import id.ac.ukdw.drones_isai.databinding.FragmentKarbonTerserapBinding

class KarbonTerserapFragment : Fragment() {

    private lateinit var binding: FragmentKarbonTerserapBinding

    private val months = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentKarbonTerserapBinding.inflate(inflater, container, false)
        val view = binding.root

        setupCandlestickChart()
        setupBarChart()
        binding.fabZoom.setOnClickListener {
            val newFragment = TerserapLandFragment()
            val parentFragment = parentFragment
            if (parentFragment is Fragment) {
                parentFragment.parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, newFragment)
                    .addToBackStack(null)
                    .commit()
                val mainActivity = activity as? MainActivity
                mainActivity?.hideBottomNavigationView()
            }
        }
        return view
    }


    private fun setupCandlestickChart() {
        val candlestickChart: CandleStickChart = binding.candleChart
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

//        // Fill missing data with empty entries
//        val allCandleEntries = ArrayList<CandleEntry>()
//        for (i in 0 until months.size) {
//            val entry = if (i < candleEntries.size) candleEntries[i] else CandleEntry(
//                i.toFloat(),
//                0f,
//                0f,
//                0f,
//                0f
//            )
//            allCandleEntries.add(entry)
//        }

        val candleDataSet = CandleDataSet(candleEntries, "Candlestick Data")
        candleDataSet.color = ContextCompat.getColor(
            requireContext(),
            R.color.blue
        ) // Color for neutral (open = close)
        candleDataSet.shadowColor =
            ContextCompat.getColor(requireContext(), R.color.blue_light_2) // Color for shadow
        candleDataSet.shadowWidth = 0.7f // Width of the shadow lines
        candleDataSet.decreasingColor = ContextCompat.getColor(
            requireContext(),
            R.color.red_candle
        ) // Color for decreasing (open > close)
        candleDataSet.decreasingPaintStyle = Paint.Style.FILL // Style for decreasing candle bars
        candleDataSet.increasingColor = ContextCompat.getColor(
            requireContext(),
            R.color.green
        ) // Color for increasing (open < close)
        candleDataSet.increasingPaintStyle = Paint.Style.FILL // Style for increasing candle bars
        candleDataSet.neutralColor = ContextCompat.getColor(
            requireContext(),
            R.color.blue
        ) // Color for neutral (open = close)
        candleDataSet.valueTextSize = 10f // Text size for values displayed on the bars

        val candleData = CandleData(candleDataSet)
        candlestickChart.data = candleData

        candlestickChart.legend.isEnabled = false
        candlestickChart.description.isEnabled = false

        // Animate the Candlestick Chart
        candlestickChart.animateXY(1000, 1000)

        val xAxisCandle = candlestickChart.xAxis
        xAxisCandle.valueFormatter = IndexAxisValueFormatter(months)
        xAxisCandle.position = XAxis.XAxisPosition.BOTTOM
        xAxisCandle.setDrawGridLines(false)
        xAxisCandle.granularity = 1f

        candlestickChart.setVisibleXRangeMaximum(5f) // Set the visible range on the x-axis
        candlestickChart.axisRight.isEnabled = false

        val yAxisCandle = candlestickChart.axisLeft
        yAxisCandle.axisMinimum = candleMin - 1f
        yAxisCandle.axisMaximum = candleMax + 1f

        candlestickChart.invalidate()
    }

    private fun setupBarChart() {
        val barChart: BarChart = binding.barChart
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
        // Set the x-axis labels for all 12 months
        val xAxisLabels = ArrayList<String>()
        for (i in       months.indices) {
            val label = if (i < barEntries.size) months[i] else ""
            xAxisLabels.add(label)
        }


        barData.barWidth = 0.35f
        barChart.data = barData

// Configure other properties of the bar chart as needed
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels.toTypedArray())
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//        barChart.xAxis.setDrawGridLines(false)
        barChart.xAxis.granularity = 1f
        barChart.xAxis.spaceMin = 0.5f // Set the minimum spacing between labels
        barChart.xAxis.spaceMax = 0.5f // Set the maximum spacing between labels
        barChart.animateXY(1000, 1000, Easing.EaseInOutQuad)
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.groupBars(-0.3f, 0.15f, 0.04f) // Adjust the spacing between groups and bars


// Set the range for the y-axis
        val yAxis = barChart.axisLeft
        yAxis.axisMinimum = 0f

        // Disable the y-axis on the right side
        barChart.axisRight.isEnabled = false

        barChart.invalidate()
    }

}