package id.ac.ukdw.drones_isai

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import id.ac.ukdw.data.model.Body
import id.ac.ukdw.drones_isai.databinding.FragmentNilaiHSTBinding
import id.ac.ukdw.helper.DataExportable
import id.ac.ukdw.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class NilaiAgregatFragment : Fragment(),DataExportable {

    private lateinit var binding: FragmentNilaiHSTBinding
    private val dataCache: HashMap<String, List<Body>> = HashMap()
    private val viewModel: MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNilaiHSTBinding.inflate(inflater, container, false)
        val view = binding.root
        val cachedData = dataCache[CACHE_KEY]
        if (cachedData != null) {
            setupCandlestickChart(cachedData)

        } else {
            viewModel.fetchData()
        }
        // Observe the view model's LiveData for changes
        viewModel.responseData.observe(viewLifecycleOwner) { responseData ->
            if (responseData != null) {
                setupCandlestickChart(responseData)
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
            val carbonTanaman = item.carbonTanaman
            val carbonTanah = item.carbonTanah
            val emisiTanah = item.emisiTanah
            val emisiTanaman = item.emisiTanaman
            val emisiLingkungan = item.emisiLingkungan

            val date = item.date

            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val parsedDate = dateFormat.parse(date)
            if (parsedDate != null) {
                calendar.time = parsedDate
                val month = calendar.get(Calendar.MONTH) + 1


                val value = "$emisiTanah $emisiTanaman $emisiLingkungan $carbonTanaman $carbonTanah"
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

    private fun setupCandlestickChart(body: List<Body>) {
        val candlestickChart: CandleStickChart = binding.candle

        val candleEntries = mutableListOf<CandleEntry>()
        val xAxisLabels = mutableListOf<String>()
        var entryIndex = 0f

        val mapData = populateData(body)

        val sortedKeys = mapData.keys.sorted()

        for (month in sortedKeys) {
            val values = mapData[month]

            val sumEmisiTanah = values?.sumByDouble { value ->
                val valueArray = value.split(" ")
                if (valueArray.size == 5) {
                    valueArray[0].toDouble()
                } else {
                    0.0
                }
            } ?: 0.0

            val sumEmisiTanaman = values?.sumByDouble { value ->
                val valueArray = value.split(" ")
                if (valueArray.size == 5) {
                    valueArray[1].toDouble()
                } else {
                    0.0
                }
            } ?: 0.0

            val sumEmisiLingkungan = values?.sumByDouble { value ->
                val valueArray = value.split(" ")
                if (valueArray.size == 5) {
                    valueArray[2].toDouble()
                } else {
                    0.0
                }
            } ?: 0.0

            val sumCarbonTanaman = values?.sumByDouble { value ->
                val valueArray = value.split(" ")
                if (valueArray.size == 5) {
                    valueArray[3].toDouble()
                } else {
                    0.0
                }
            } ?: 0.0

            val sumCarbonTanah = values?.sumByDouble { value ->
                val valueArray = value.split(" ")
                if (valueArray.size == 5) {
                    valueArray[4].toDouble()
                } else {
                    0.0
                }
            } ?: 0.0

            val count = values?.count { value ->
                val valueArray = value.split(" ")
                valueArray.size == 5
            } ?: 1 // Avoid division by zero if there are no valid values

            val averageEmisiTanah = sumEmisiTanah / count
            val averageEmisiTanaman = sumEmisiTanaman / count
            val averageEmisiLingkungan = sumEmisiLingkungan / count
            val averageCarbonTanaman = sumCarbonTanaman / count
            val averageCarbonTanah = sumCarbonTanah / count

            val value = (averageEmisiTanah + averageEmisiTanaman + averageEmisiLingkungan) - (averageCarbonTanaman + averageCarbonTanah)

            val entry = CandleEntry(entryIndex, value.toFloat(), value.toFloat(), value.toFloat(), value.toFloat())
            candleEntries.add(entry)
            xAxisLabels.add(month.toString())
            entryIndex += 1f
        }

        val candleDataSet = CandleDataSet(candleEntries, "Candlestick Data")
        candleDataSet.setDrawIcons(false)
        candleDataSet.axisDependency = YAxis.AxisDependency.LEFT
        candleDataSet.shadowColor = Color.DKGRAY
        candleDataSet.shadowWidth = 0.7f
        candleDataSet.decreasingColor = Color.RED
        candleDataSet.decreasingPaintStyle = Paint.Style.FILL
        candleDataSet.increasingColor = Color.GREEN
        candleDataSet.increasingPaintStyle = Paint.Style.FILL
        candleDataSet.neutralColor = Color.BLUE
        candleDataSet.valueTextSize = 10f
        candleDataSet.setDrawValues(true) // Enable value display on candles

        val candleData = CandleData(candleDataSet)
        candlestickChart.data = candleData

        candlestickChart.legend.isEnabled = false
        candlestickChart.description.isEnabled = false

        // Animate the Candlestick Chart
        candlestickChart.animateXY(1000, 1000)

        val xAxisCandle = candlestickChart.xAxis
        xAxisCandle.valueFormatter = IndexAxisValueFormatter(xAxisLabels.toTypedArray())
        xAxisCandle.position = XAxis.XAxisPosition.BOTTOM
        xAxisCandle.setDrawGridLines(false)
        xAxisCandle.granularity = 1f

        candlestickChart.setVisibleXRangeMaximum(candleEntries.size.toFloat()) // Set the visible range on the x-axis
        candlestickChart.axisRight.isEnabled = false

        val yAxisCandle = candlestickChart.axisLeft
        yAxisCandle.axisMinimum = candleEntries.minByOrNull { it.low }?.low ?: 0f
        yAxisCandle.axisMaximum = candleEntries.maxByOrNull { it.high }?.high ?: 10f

        candlestickChart.invalidate()
    }

    override fun getData(): String  {
        val cachedData = dataCache[CACHE_KEY]
        return cachedData.toString() ?: "No Data on Fragment 1"
    }

    companion object {
        private const val CACHE_KEY = "karbon_terserap_fragment_cache"

    }


}