package id.ac.ukdw.drones_isai

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import id.ac.ukdw.data.model.Body
import id.ac.ukdw.drones_isai.databinding.FragmentNilaiHSTBinding
import id.ac.ukdw.helper.DataExportable
import id.ac.ukdw.helper.TableBuilder
import id.ac.ukdw.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class NilaiAgregatFragment : Fragment(), DataExportable {

    private lateinit var binding: FragmentNilaiHSTBinding
    private val dataCache: HashMap<String, List<Body>> = HashMap()
    private val viewModel: MainViewModel by viewModels()
    private var renderedTable: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNilaiHSTBinding.inflate(inflater, container, false)
        val view = binding.root
        val cachedData = dataCache[CACHE_KEY]
        if (cachedData != null) {
            setupBarChart(cachedData)
            binding.barChart.visibility =View.VISIBLE
        } else {
            binding.loadBar.visibility = View.VISIBLE
            binding.barChart.visibility =View.GONE
            viewModel.fetchData()
        }
        // Observe the view model's LiveData for changes
        viewModel.responseData.observe(viewLifecycleOwner) { responseData ->
            if (responseData != null) {
                setupBarChart(responseData)
                binding.barChart.visibility =View.VISIBLE
                binding.loadBar.visibility = View.GONE
            }else{
                binding.barChart.visibility =View.GONE
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

    private fun populateData(body: List<Body>, filterEnabled: Boolean,
                             selectedYear: Int?, selectedKomoditas: String?,
                             selectedLokasi: String?): MutableMap<Int, MutableList<String>> {

        val mapData = mutableMapOf<Int, MutableList<String>>()

        for (item in body) {
            val carbonTanaman = item.carbonTanaman
            val carbonTanah = item.carbonTanah
            val emisiTanaman = item.emisiTanaman
            val emisiTanah = item.emisiTanah
            val emisiLingkungan = item.emisiLingkungan
            val date = item.date
            val komoditas = item.comodity
            val lokasi = item.loc

            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val parsedDate = dateFormat.parse(date)
            if (parsedDate != null) {
                calendar.time = parsedDate
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH) + 1

                // Apply filtering if the flag is enabled
                if (!filterEnabled || (selectedYear == null || year == selectedYear)
                    && (selectedKomoditas == null || komoditas == selectedKomoditas)
                    && (selectedLokasi == null || lokasi == selectedLokasi)
                ) {
                    val value = "$carbonTanaman $carbonTanah $emisiTanaman $emisiTanah $emisiLingkungan"
                    if (mapData.containsKey(month)) {
                        mapData[month]?.add(value)
                    } else {
                        mapData[month] = mutableListOf(value)
                    }
                }
            }
        }

        return mapData
    }
    private fun setupBarChart(body: List<Body>) {
        val barChart: BarChart = binding.barChart

        val mapData = populateData(body, false, null, null, null)
        if (mapData.isNotEmpty()){
            val sortedData = mapData.toSortedMap(compareBy { it })

            val barEntries1 = mutableListOf<BarEntry>()
            val xAxisLabels = mutableListOf<String>()
            val xAxisLabelsMonths = mutableListOf<String>()
            var barIndex = 0f
            val barWidth = 0.3f // Adjust the width of the bars
            val monthNames = arrayOf(
                "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
                "Jul", "Aug", "Sep", "Okt", "Nov", "Des"
            )

            for ((month, values) in sortedData) {
                var sumValue1 = 0f // terserap tanaman
                var sumValue2 = 0f // terserap tanah
                var sumValue3 = 0f // emisi tanaman
                var sumValue4 = 0f // emisi tanah
                var sumValue5 = 0f // emisi lingkungan
                var count = 0

                for (value in values) {
                    val valueArray = value.split(" ")
                    if (valueArray.size == 5) {
                        sumValue1 += valueArray[0].toFloat()
                        sumValue2 += valueArray[1].toFloat()
                        sumValue3 += valueArray[2].toFloat()
                        sumValue4 += valueArray[3].toFloat()
                        sumValue5 += valueArray[4].toFloat()
                        count++
                    }
                }

                if (count > 0) {
                    val averageValue1 = sumValue1 / count
                    val averageValue2 = sumValue2 / count
                    val averageValue3 = sumValue3 / count
                    val averageValue4 = sumValue4 / count
                    val averageValue5 = sumValue5 / count

                    val agregat = (averageValue1 + averageValue2)+(-(averageValue3+averageValue4+averageValue5))

                    val entry1 = BarEntry(barIndex, agregat)
                    barEntries1.add(entry1)
                    xAxisLabels.add(month.toString())
                    val monthName = monthNames[month - 1]
                    xAxisLabelsMonths.add(monthName)
                    barIndex += 1f
                }

            }

            val dataSet1 = BarDataSet(barEntries1, "Bar 1")

            // Set different colors for positive and negative values
            val colors = mutableListOf<Int>()
            for (entry in barEntries1) {
                if (entry.y >= 0) {
                    colors.add(ContextCompat.getColor(requireContext(), R.color.green))
                } else {
                    colors.add(ContextCompat.getColor(requireContext(), R.color.red))
                }
            }
            dataSet1.colors = colors

            dataSet1.setDrawValues(true)


            val barData = BarData(dataSet1)
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
//        barChart.groupBars(-0.25f, 0.11f, 0.11f)

            barChart.notifyDataSetChanged()
            barChart.invalidate()

            // make the table
            val rowTemplate = "|  {label}  |  {value1}  |\n"
            renderedTable = TableBuilder.buildTable(xAxisLabels, rowTemplate, barEntries1)
        }else{
            Toast.makeText(context, "Tidak Ada Data Sesuai Filter!", Toast.LENGTH_LONG).show()
        }

    }

    override fun getData(): String {
        return renderedTable
    }

    override fun getScreen(): Bitmap? {
        return (captureGraph())
    }

    private fun captureGraph(): Bitmap? {
        val graphView = binding.barChart

        // Create a bitmap with the same size as the graph view
        val bitmap = Bitmap.createBitmap(graphView.width, graphView.height, Bitmap.Config.ARGB_8888)

        // Create a canvas with the bitmap
        val canvas = Canvas(bitmap)

        // Draw the graph view onto the canvas
        graphView.draw(canvas)

        return bitmap
    }

    companion object {
        private const val CACHE_KEY = "agregat_fragment_cache"

    }
}