package id.ac.ukdw.drones_isai

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import id.ac.ukdw.viewmodel.SharedViewModel
import id.ac.ukdw.drones_isai.databinding.FragmentEmisiKarbonBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class EmisiKarbonFragment : Fragment() {

    private lateinit var binding: FragmentEmisiKarbonBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
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

        sharedViewModel.data.observe(viewLifecycleOwner) { dataApi ->
            // Handle the updated data here
            Log.d("testVM", dataApi.toString())

            // Parse and populate data
            val mapData = populateData(dataApi)

            // Set up charts
            setupBarChart(mapData)
        }

        return view
    }

    private fun populateData(dataApi: MutableMap<String, MutableList<String>>): Map<Int, MutableList<Float>> {
        val mapData = mutableMapOf<Int, MutableList<Float>>()

        val data = dataApi

        Log.d("ikan", data.toString())
        for ((date, value) in data) {
            for (index in value.indices) {
                val values = value[index].split(" ").map { it.toFloat() }

                val calendar = Calendar.getInstance()
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val parsedDate = dateFormat.parse(date)
                if (parsedDate != null) {
                    calendar.time = parsedDate
                    val month = calendar.get(Calendar.MONTH) + 1

                    if (mapData.containsKey(month)) {
                        mapData[month]?.addAll(values)
                    } else {
                        mapData[month] = values.toMutableList()
                    }
                }
            }
        }

        for ((month, values) in mapData) {
            Log.d("dataPair", "$month: $values")
        }
        Log.d("mapData", mapData.toString())
        return mapData
    }




    private fun setupBarChart(mapData: Map<Int, List<Float>>) {
        val barChart: BarChart = binding.barChart

        val sortedData = mapData.toSortedMap(compareBy { it })

        val barEntries1 = mutableListOf<BarEntry>()
        val barEntries2 = mutableListOf<BarEntry>()
        val barEntries3 = mutableListOf<BarEntry>()
        val xAxisLabels = mutableListOf<String>()
        val barColors = mutableListOf<Int>()
        var barIndex = 0f
        val groupSpace = 0.12f // Adjust the spacing between groups
        val barWidth = 0.3f // Adjust the width of the bars

        for ((month, values) in sortedData) {
            val valueArray = values.toString().split(" ")
            if (valueArray.size == 3) {
                val entry1 = BarEntry(barIndex, valueArray[0].toFloat())
                val entry2 = BarEntry(barIndex, valueArray[1].toFloat())
                val entry3 = BarEntry(barIndex, valueArray[2].toFloat())

                barEntries1.add(entry1)
                barEntries2.add(entry2)
                barEntries3.add(entry3)

                xAxisLabels.add(month.toString())
                barIndex += 1f
            }
        }

        val dataSetList = mutableListOf<BarDataSet>()
        val colorArray = arrayOf(R.color.blue, R.color.green, R.color.red_candle)
        val entriesList = listOf(barEntries1, barEntries2, barEntries3)

        for (i in entriesList.indices) {
            val entries = entriesList[i]
            val dataSet = BarDataSet(entries, "Bar ${i + 1}")
            dataSet.color = ContextCompat.getColor(requireContext(), colorArray[i])
            dataSetList.add(dataSet)
        }

        if (dataSetList.size >= 2) {
            val barData = BarData(dataSetList as List<IBarDataSet>?)
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
            val groupCount = entriesList.size
            val groupWidth = barData.barWidth * groupCount + groupSpace * (groupCount - 1)

            // Calculate the left offset to align the bars to the left side
            val startOffset = -groupWidth / 2

            // Group the bars and adjust spacing
            barChart.groupBars(0f, groupSpace, startOffset)
            barChart.invalidate()
        } else {
            Log.d("error bar", dataSetList.toString())
        }
    }




}