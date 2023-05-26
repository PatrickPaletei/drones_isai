package id.ac.ukdw.drones_isai

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import id.ac.ukdw.drones_isai.databinding.FragmentHstLandBinding


class HstLandFragment : Fragment() {

    private var _binding: FragmentHstLandBinding? = null
    private val binding get() = _binding!!

    private lateinit var barData: BarData


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHstLandBinding.inflate(inflater, container, false)
        val view = binding.root

        // Force landscape orientation
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        binding.closeLand.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Use the barData to set up your chart
//        setupBarChart(barData)

        return view
    }




    override fun onResume() {
        super.onResume()
        val mainActivity = activity as? MainActivity
        mainActivity?.hideBottomNavigationView()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // Reset the orientation when the fragment is destroyed
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        // Show the bottom navigation view when the fragment is destroyed
        val mainActivity = activity as? MainActivity
        mainActivity?.showBottomNavigationView()
    }


}