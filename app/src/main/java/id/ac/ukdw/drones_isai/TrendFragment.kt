package id.ac.ukdw.drones_isai


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.ac.ukdw.adapter.SpinnerFilterAdapter
import id.ac.ukdw.adapter.ViewPagerAdapter
import id.ac.ukdw.drones_isai.databinding.FilterBottomPopupBinding
import id.ac.ukdw.drones_isai.databinding.FragmentTrenBinding


data class SpinnerItem(val id: Int, val name: String)
class TrendFragment : Fragment() {


    private lateinit var binding: FragmentTrenBinding

    private val spinner1Items = listOf(
        SpinnerItem(1, "2020"),
        SpinnerItem(2, "2021"),
        SpinnerItem(3, "2022")
    )
    private val spinner2Items = listOf(
        SpinnerItem(1, "2"),
        SpinnerItem(2, "3"),
        SpinnerItem(3, "4")
    )
    private val spinner3Items = arrayOf("Padi", "Cabe", "Coklat")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        // Set up the button click listener
        binding.filterBtn.setOnClickListener {
            showBottomPopupDialog()
        }

        return view
    }



    private fun showBottomPopupDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogBinding = FilterBottomPopupBinding.inflate(LayoutInflater.from(requireContext()))
        bottomSheetDialog.setContentView(dialogBinding.root)

        val spinner1 = dialogBinding.spinnerTahun
        val spinner2 = dialogBinding.spinnerDataKarbon
        val spinner3 = dialogBinding.spinnerJenisKomoditas

        val spinner1Adapter = SpinnerFilterAdapter(requireContext(), android.R.layout.simple_spinner_item, spinner1Items)
        spinner1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = spinner1Adapter

        val spinner2Adapter = SpinnerFilterAdapter(requireContext(), android.R.layout.simple_spinner_item, spinner2Items)
        spinner2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = spinner2Adapter

        val spinner3Adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinner3Items)
        spinner3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner3.adapter = spinner3Adapter

        // Show the bottom sheet dialog
        bottomSheetDialog.show()
    }


}