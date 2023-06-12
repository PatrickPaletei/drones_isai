package id.ac.ukdw.drones_isai

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import id.ac.ukdw.data.model.Body
import id.ac.ukdw.viewmodel.SharedViewModel
import id.ac.ukdw.drones_isai.databinding.FragmentEmisiKarbonBinding
import id.ac.ukdw.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class EmisiKarbonFragment : Fragment() {

    private lateinit var binding: FragmentEmisiKarbonBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val dataCache: HashMap<String, List<Body>> = HashMap()
    private val viewModel: MainViewModel by viewModels()
    private val months = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEmisiKarbonBinding.inflate(inflater, container, false)
        val view = binding.root

        val cachedData = dataCache[CACHE_KEY]
        if (cachedData != null) {
            setupBarChart(cachedData)

        } else {
            viewModel.fetchData()
        }
        // Observe the view model's LiveData for changes
        viewModel.responseData.observe(viewLifecycleOwner) { responseData ->
            if (responseData != null) {
                setupBarChart(responseData)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                // Handle the error message, e.g., display it to the user

            }
        }

        return view
    }

    private fun populateData(body: List<Body>): MutableMap<Int, MutableList<String>> {
        val mapData = mutableMapOf<Int, MutableList<String>>()

        for (item in body) {
            val emisiTanaman = item.carbonTanaman
            val emisiTanah = item.carbonTanah
            val emisiLingkungan = item.emisiLingkungan
            val date = item.date

            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val parsedDate = dateFormat.parse(date)
            if (parsedDate != null) {
                calendar.time = parsedDate
                val month = calendar.get(Calendar.MONTH) + 1

                val value = "$emisiTanaman $emisiTanah $emisiLingkungan"
                if (mapData.containsKey(month)) {
                    mapData[month]?.add(value)
                } else {
                    mapData[month] = mutableListOf(value)
                }
            }
        }

        for ((month, values) in mapData) {
            Log.d("dataPair", "$month: $values")
        }

        if (body.isEmpty()) {
            Log.d("noData", "body is empty")
        }
        return mapData
    }



    private fun setupBarChart(body: List<Body>) {
        val barChart: BarChart = binding.barChart

        val mapData = populateData(body)

        val sortedData = mapData.toSortedMap(compareBy { it })

        val barEntries1 = mutableListOf<BarEntry>()
        val barEntries2 = mutableListOf<BarEntry>()
        val barEntries3 = mutableListOf<BarEntry>()
        val xAxisLabels = mutableListOf<String>()
        var barIndex = 0f
        val groupSpace = 0.12f // Adjust the spacing between groups
        val barWidth = 0.25f // Adjust the width of the bars

        for ((month, values) in sortedData) {
            var sumValue1 = 0f
            var sumValue2 = 0f
            var sumValue3 = 0f
            var count = 0

            for (value in values) {
                val valueArray = value.split(" ")
                if (valueArray.size == 3) {
                    sumValue1 += valueArray[0].toFloat()
                    sumValue2 += valueArray[1].toFloat()
                    sumValue3 += valueArray[2].toFloat()
                    count++
                }
            }

            if (count > 0) {
                val averageValue1 = sumValue1 / count
                val averageValue2 = sumValue2 / count
                val averageValue3 = sumValue3 / count

                val entry1 = BarEntry(barIndex, averageValue1)
                val entry2 = BarEntry(barIndex, averageValue2)
                val entry3 = BarEntry(barIndex, averageValue3)
                barEntries1.add(entry1)
                barEntries2.add(entry2)
                barEntries3.add(entry3)
                xAxisLabels.add(month.toString())
                barIndex += 1f
            }
        }

        val dataSet1 = BarDataSet(barEntries1, "Bar 1")
        dataSet1.color = ContextCompat.getColor(requireContext(), R.color.blue)
        dataSet1.setDrawValues(true)

        val dataSet2 = BarDataSet(barEntries2, "Bar 2")
        dataSet2.color = ContextCompat.getColor(requireContext(), R.color.green)
        dataSet2.setDrawValues(true)

        val dataSet3 = BarDataSet(barEntries3, "Bar 3")
        dataSet3.color = ContextCompat.getColor(requireContext(), R.color.red_candle)
        dataSet3.setDrawValues(true)

        val barData = BarData(dataSet1, dataSet2,dataSet3)
        barData.barWidth = barWidth

        barChart.data = barData

        // Configure other properties of the bar chart as needed
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels.toTypedArray())
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f
        barChart.xAxis.spaceMin = 0.5f // Set the minimum spacing between labels
        barChart.xAxis.spaceMax = 0.5f // Set the maximum spacing between labels
        barChart.animateXY(1000, 1000, Easing.EaseInOutQuad)
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        // Disable the y-axis on the right side
        barChart.axisRight.isEnabled = false

        // Calculate the total width of the groups
        val groupCount = barEntries1.size // Assumes both barEntries1 and barEntries2 have the same size
        val groupWidth = barData.barWidth * groupCount + groupSpace * (groupCount - 1)

        // Calculate the left offset to align the bars to the left side
        val startOffset = -groupWidth / 2

        // Group the bars and adjust spacing
        barChart.groupBars(0f, 0.08f, 0f)

        // Adjust the range of the Y-axis to accommodate the data
        val minValue = minOf(barEntries1.minByOrNull { it.y }?.y ?: 0f, barEntries2.minByOrNull { it.y }?.y ?: 0f)
        val maxValue = maxOf(barEntries1.maxByOrNull { it.y }?.y ?: 0f, barEntries2.maxByOrNull { it.y }?.y ?: 0f)
        barChart.axisLeft.axisMinimum = minValue - 10
        barChart.axisLeft.axisMaximum = maxValue + 10

        barChart.notifyDataSetChanged()
        barChart.invalidate()
    }

    companion object {
        private const val CACHE_KEY = "karbon_terserap_fragment_cache"


    }


}