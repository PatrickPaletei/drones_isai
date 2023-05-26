package id.ac.ukdw.drones_isai

import android.content.Context
import android.os.Bundle
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
import id.ac.ukdw.drones_isai.databinding.FragmentNilaiHSTBinding


class NilaiHSTFragment : Fragment() {

    private lateinit var binding: FragmentNilaiHSTBinding
    private var bottomNavigationViewListener: BottomNavigationViewListener? = null
    // Declare a member variable for barData
    private lateinit var barData: BarData

    private val months = arrayOf(
        "HST0", "HST1", "HST2"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentNilaiHSTBinding.inflate(inflater, container, false)
        val view = binding.root

        setupBarChart()
        binding.zoomChart.setOnClickListener {
            val newFragment = HstLandFragment()
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

    private fun setupBarChart(){
        val barChart: BarChart = binding.barChart


        // Bar Chart
        val barEntries = listOf(
            BarEntry(0f, 8f),
            BarEntry(1f, 5f),
            BarEntry(2f, 7f)
        )

        val dataSet1 = BarDataSet(barEntries, "Bar 1")
        dataSet1.color = ContextCompat.getColor(requireContext(), R.color.blue)

// Create a separate list of entries for dataSet2
        val dataSet2Entries = listOf(
            BarEntry(0f, 2f),
            BarEntry(1f, 4f),
            BarEntry(2f, 3f)
        )
        val dataSet2 = BarDataSet(dataSet2Entries, "Bar 2")
        dataSet2.color = ContextCompat.getColor(requireContext(), R.color.blue_light)

        val barData = BarData(dataSet1, dataSet2)


// Set the x-axis labels for all 12 months
        val xAxisLabels = ArrayList<String>()
        for (i in months.indices) {
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
        barChart.xAxis.spaceMin = 0.5f // Set the minimum spacing between labels
        barChart.xAxis.spaceMax = 0.5f // Set the maximum spacing between labels
        barChart.animateXY(1000, 1000, Easing.EaseInOutQuad)
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.groupBars(-0.4f, 0.15f, 0.05f) // Adjust the spacing between groups and bars


// Set the range for the y-axis
        val yAxis = barChart.axisLeft
        yAxis.axisMinimum = 0f

        // Disable the y-axis on the right side
        barChart.axisRight.isEnabled = false

        barChart.invalidate()


    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the activity implements the interface
        if (context is BottomNavigationViewListener) {
            bottomNavigationViewListener = context
        } else {
            throw RuntimeException("$context must implement BottomNavigationViewListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        bottomNavigationViewListener = null
    }


    interface BottomNavigationViewListener {
        fun hideBottomNavigationView()
    }


}