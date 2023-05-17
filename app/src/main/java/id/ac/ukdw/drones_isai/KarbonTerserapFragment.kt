package id.ac.ukdw.drones_isai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class KarbonTerserapFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_karbon_terserap, container, false)

        val chart: CandleStickChart = view.findViewById(R.id.chart)

        val entries = listOf(
            CandleEntry(0f, 9f, 5f, 7f, 6f),
            CandleEntry(1f, 4f, 3f, 6f, 5f),
            CandleEntry(2f, 6f, 4f, 5f, 5.5f),
            CandleEntry(3f, 8f, 6f, 7f, 7f),
            CandleEntry(4f, 5f, 4f, 4.5f, 4.7f)
        )

        val dataSet = CandleDataSet(entries, "Label")
        val candleData = CandleData(dataSet)

        chart.data = candleData
        chart.invalidate()

        return view
    }


}