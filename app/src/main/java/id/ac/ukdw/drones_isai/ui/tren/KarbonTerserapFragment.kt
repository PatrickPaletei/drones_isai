package id.ac.ukdw.drones_isai.ui.tren

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import id.ac.ukdw.data.DataPopulator
import id.ac.ukdw.data.model.Body
import id.ac.ukdw.drones_isai.R
import id.ac.ukdw.drones_isai.databinding.FragmentKarbonTerserapBinding
import id.ac.ukdw.drones_isai.utils.GraphCaptureUtils
import id.ac.ukdw.helper.DataExportable
import id.ac.ukdw.helper.TableBuilder
import id.ac.ukdw.viewmodel.MainViewModel
import id.ac.ukdw.viewmodel.SharedFilterViewModel


class KarbonTerserapFragment : Fragment(), DataExportable {

    private lateinit var binding: FragmentKarbonTerserapBinding
    private lateinit var sharedViewModel: SharedFilterViewModel
    private val viewModel: MainViewModel by viewModels()
    private var renderedTable: String = ""
    private val dataPopulator: DataPopulator = DataPopulator()
    private var selectedTahun: String? = null
    private var selectedLokasi: String? = null
    private var selectedComodity: String? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentKarbonTerserapBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.barChart.visibility = View.GONE
        viewModel.fetchData()

        // Observe the view model's LiveData for changes
        viewModel.responseData.observe(viewLifecycleOwner) { responseData ->
            if (responseData != null) {
                setupBarChart(responseData)
                binding.barChart.visibility = View.VISIBLE
                binding.loadBar.visibility = View.GONE
            } else {
                binding.barChart.visibility = View.GONE
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                // Handle the error message, e.g., display it to the user
                binding.loadBar.visibility = View.GONE
                binding.noDataBar.text = "Tidak Ada Data Untuk Ditampilkan"
            }
        }


        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedFilterViewModel::class.java]
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.buttonState.observe(viewLifecycleOwner) { isPressed ->
            if (isPressed) {
                selectedTahun = sharedViewModel.tahunValue.value
                selectedLokasi = sharedViewModel.lokasiValue.value
                selectedComodity = sharedViewModel.comodityValue.value
                viewModel.responseData.observe(viewLifecycleOwner) { responseData ->
                    if (responseData != null && selectedLokasi != null && selectedComodity != null && selectedTahun != null) {
                        clearChart()
                        setupBarChartFilter(
                            responseData,
                            selectedTahun!!,
                            selectedComodity!!,
                            selectedLokasi!!
                        )
                        Log.d(
                            "globalOnR",
                            "onResume: $selectedTahun $selectedLokasi $selectedComodity $responseData"
                        )
                    }
                }
            } else {
                viewModel.responseData.observe(viewLifecycleOwner) { responseData ->
                    clearChart()
                    if (responseData != null) {
                        setupBarChart(responseData)
                    }
                }
            }
        }

    }

    private fun clearChart() {
        val barChart: BarChart = binding.barChart
        barChart.clear()
    }

    private fun setupBarChart(body: List<Body>) {
        val barChart: BarChart = binding.barChart

        val mapData = dataPopulator.populateData(body)

        val sortedData = mapData.toSortedMap(compareBy { it })


        val barEntries1 = mutableListOf<BarEntry>()
        val barEntries2 = mutableListOf<BarEntry>()
        val xAxisLabels = mutableListOf<String>()
        val xAxisLabelsMonths = mutableListOf<String>()
        var barIndex = 0f
        val barWidth = 0.3f // Adjust the width of the bars
        val monthNames = arrayOf(
            "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
            "Jul", "Aug", "Sep", "Okt", "Nov", "Des"
        )

        for ((month, values) in sortedData) {
            var sumValue1 = 0f
            var sumValue2 = 0f
            var count = 0

            for (value in values) {
                val valueArray = value.split(" ")
                if (valueArray.size == 7) {
                    sumValue1 += valueArray[0].toFloat()
                    sumValue2 += valueArray[1].toFloat()
                    count++
                }
            }

            if (count > 0) {
                val averageValue1 = sumValue1 / count
                val averageValue2 = sumValue2 / count

                val entry1 = BarEntry(barIndex, averageValue1)
                val entry2 = BarEntry(barIndex, averageValue2)
                barEntries1.add(entry1)
                barEntries2.add(entry2)
                xAxisLabels.add(month.toString())
                val monthName = monthNames[month - 1]
                xAxisLabelsMonths.add(monthName)
                barIndex += 1f
            }

        }

        val dataSet1 = BarDataSet(barEntries1, "Bar 1")
        dataSet1.color = ContextCompat.getColor(requireContext(), R.color.blue)
        dataSet1.setDrawValues(true)

        val dataSet2 = BarDataSet(barEntries2, "Bar 2")
        dataSet2.color = ContextCompat.getColor(requireContext(), R.color.blue_light)
        dataSet2.setDrawValues(true)

        val barData = BarData(dataSet1, dataSet2)
        barData.barWidth = barWidth

        barChart.data = barData

        // Configure other properties of the bar chart as needed
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabelsMonths.toTypedArray())
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f
        barChart.animateXY(1000, 1000, Easing.EaseInOutQuad)
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        // Disable the y-axis on the right side
        barChart.axisRight.isEnabled = false

        // Group the bars and adjust spacing
        barChart.groupBars(-0.25f, 0.11f, 0.11f)

        barChart.notifyDataSetChanged()
        barChart.invalidate()

        // make the table
        val rowTemplate = "|  {label}  |  {value1}  |  {value2} |\n"
        renderedTable = TableBuilder.buildTable(xAxisLabels, rowTemplate, barEntries1, barEntries2)
    }

    private fun setupBarChartFilter(body: List<Body>, year: String, comodity: String, loc: String) {
        val barChart: BarChart = binding.barChart

        val mapData = dataPopulator.populateDataFilter(body, true, year.toInt(), comodity, loc)

        val sortedData = mapData.toSortedMap(compareBy { it })


        val barEntries1 = mutableListOf<BarEntry>()
        val barEntries2 = mutableListOf<BarEntry>()
        val xAxisLabels = mutableListOf<String>()
        val xAxisLabelsMonths = mutableListOf<String>()
        var barIndex = 0f
        val barWidth = 0.3f // Adjust the width of the bars
        val monthNames = arrayOf(
            "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
            "Jul", "Aug", "Sep", "Okt", "Nov", "Des"
        )

        for ((month, values) in sortedData) {
            var sumValue1 = 0f
            var sumValue2 = 0f
            var count = 0

            for (value in values) {
                val valueArray = value.split(" ")
                if (valueArray.size == 5) {
                    sumValue1 += valueArray[0].toFloat()
                    sumValue2 += valueArray[1].toFloat()
                    count++
                }
            }

            if (count > 0) {
                val averageValue1 = sumValue1 / count
                val averageValue2 = sumValue2 / count

                val entry1 = BarEntry(barIndex, averageValue1)
                val entry2 = BarEntry(barIndex, averageValue2)
                barEntries1.add(entry1)
                barEntries2.add(entry2)
                xAxisLabels.add(month.toString())
                val monthName = monthNames[month - 1]
                xAxisLabelsMonths.add(monthName)
                barIndex += 1f
            }

        }

        val dataSet1 = BarDataSet(barEntries1, "Bar 1")
        dataSet1.color = ContextCompat.getColor(requireContext(), R.color.blue)
        dataSet1.setDrawValues(true)

        val dataSet2 = BarDataSet(barEntries2, "Bar 2")
        dataSet2.color = ContextCompat.getColor(requireContext(), R.color.blue_light)
        dataSet2.setDrawValues(true)

        val barData = BarData(dataSet1, dataSet2)
        barData.barWidth = barWidth

        barChart.data = barData

        // Configure other properties of the bar chart as needed
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabelsMonths.toTypedArray())
        barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChart.xAxis.granularity = 1f
        barChart.animateXY(1000, 1000, Easing.EaseInOutQuad)
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        // Disable the y-axis on the right side
        barChart.axisRight.isEnabled = false

        // Group the bars and adjust spacing
        barChart.groupBars(-0.25f, 0.05f, 0.11f)

        barChart.notifyDataSetChanged()
        barChart.invalidate()

        // make the table
        val rowTemplate = "|  {label}  |  {value1}  |  {value2} |\n"
        renderedTable = TableBuilder.buildTable(xAxisLabels, rowTemplate, barEntries1, barEntries2)
    }


    override fun getData(): String {
        return renderedTable
    }

    override fun getScreen(): Bitmap? {
        return (captureGraph())
    }

    private fun captureGraph(): Bitmap? {
        val graphView = binding.barChart
        return GraphCaptureUtils.captureGraph(graphView)
    }

}