package id.ac.ukdw.drones_isai

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import id.ac.ukdw.adapter.ViewPagerAdapter
import id.ac.ukdw.drones_isai.databinding.FragmentTrenBinding


class TrendFragment : Fragment() {


    private lateinit var binding: FragmentTrenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrenBinding.inflate(inflater, container, false)
        val view = binding.root

        // Set up the Toolbar
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        // Set up the ViewPager adapter
        val adapter = ViewPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = adapter

        // Set up the TabLayout
        binding.tabLayout.setupWithViewPager(binding.viewPager)



        return view
    }




}