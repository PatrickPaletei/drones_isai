package id.ac.ukdw.drones_isai.ui


import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomsheet.BottomSheetDialog
import id.ac.ukdw.adapter.SpinnerFilterAdapter
import id.ac.ukdw.adapter.ViewPagerAdapter
import id.ac.ukdw.data.PDFExporter
import id.ac.ukdw.data.model.Body
import id.ac.ukdw.drones_isai.R
import id.ac.ukdw.drones_isai.databinding.FilterBottomPopupBinding
import id.ac.ukdw.drones_isai.databinding.FragmentTrenBinding
import id.ac.ukdw.helper.DataExportable
import id.ac.ukdw.viewmodel.MainViewModel
import id.ac.ukdw.viewmodel.SharedFilterViewModel
import java.io.IOException


data class SpinnerItem(val id: Int, val name: String)
class TrendFragment : Fragment() {

    private lateinit var binding: FragmentTrenBinding
    private lateinit var viewPager: ViewPager
    private lateinit var sharedViewModel: SharedFilterViewModel

    private val viewModel: MainViewModel by viewModels()

    private val tahun = mutableListOf<SpinnerItem>()
    private val lokasi = mutableListOf<SpinnerItem>()
    private val komoditas = mutableListOf<SpinnerItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel =
            ViewModelProvider(requireActivity())[SharedFilterViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.Q)
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
        viewPager = binding.viewPager  // Assign the ViewPager to the lateinit variable
        viewPager.adapter = adapter

        // Set up the TabLayout
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        // Set up the button click listener
        binding.filterBtn.setOnClickListener {
            showBottomPopupDialog()
        }
        binding.btnExport.setOnClickListener {
            exportDataToPDF()
        }
        getSpinnerData()


        return view
    }

    private fun getSpinnerData() {

        viewModel.fetchData()

        // Observe the view model's LiveData for changes
        viewModel.responseData.observe(viewLifecycleOwner) { responseData ->
            if (responseData != null) {
                appendDataToSpinnerLists(responseData)
            } else {

            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {

            }
        }
    }

    private fun appendDataToSpinnerLists(body: List<Body>) {
        val addedItems = mutableSetOf<String>() // Track added items to avoid duplicates
        var idCounter = 1 // Start the counter from 1

        for (item in body) {
            val year = item.date.substring(0, 4) // Extract the year from the date

            val tahunItem = SpinnerItem(idCounter, year)
            val lokasiItem = SpinnerItem(idCounter, item.loc)
            val komoditasItem = SpinnerItem(idCounter, item.comodity)

            if (!addedItems.contains(year)) {
                tahun.add(tahunItem)
                addedItems.add(year)
            }

            if (!addedItems.contains(item.loc)) {
                lokasi.add(lokasiItem)
                addedItems.add(item.loc)
            }

            if (!addedItems.contains(item.comodity)) {
                komoditas.add(komoditasItem)
                addedItems.add(item.comodity)
            }

            idCounter++ // Increment the counter after assigning the IDs
        }
    }

    private fun showBottomPopupDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val dialogBinding = FilterBottomPopupBinding.inflate(LayoutInflater.from(requireContext()))
        bottomSheetDialog.setContentView(dialogBinding.root)

        val spinner1 = dialogBinding.spinnerTahun
        val spinner2 = dialogBinding.spinnerDataKarbon
        val spinner3 = dialogBinding.spinnerJenisKomoditas

        val spinner1Adapter =
            SpinnerFilterAdapter(requireContext(), android.R.layout.simple_spinner_item, tahun)
        spinner1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner1.adapter = spinner1Adapter

        val spinner2Adapter =
            SpinnerFilterAdapter(requireContext(), android.R.layout.simple_spinner_item, lokasi)
        spinner2Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = spinner2Adapter

        val spinner3Adapter =
            SpinnerFilterAdapter(requireContext(), android.R.layout.simple_spinner_item, komoditas)
        spinner3Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner3.adapter = spinner3Adapter

        val btn = dialogBinding.filterBtn
        dialogBinding.apply {
            filterBtn.setOnClickListener {
                val selectedTahun = (spinner1.selectedItem as SpinnerItem).name
                val selectedLokasi = (spinner2.selectedItem as SpinnerItem).name
                val selectedComodity = (spinner3.selectedItem as SpinnerItem).name
                btn.showLoading()
                sharedViewModel.setButtonStateAndValue(
                    true,
                    selectedTahun,
                    selectedLokasi,
                    selectedComodity
                )
                bottomSheetDialog.dismiss()
            }
            allData.setOnClickListener {
                sharedViewModel.setButtonClearFilter(false)
                bottomSheetDialog.dismiss()
            }
        }
        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedTahun = (spinner1.selectedItem as SpinnerItem).name
                sharedViewModel.setSelectedTahun(selectedTahun)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLokasi = (spinner2.selectedItem as SpinnerItem).name
                sharedViewModel.setSelectedLokasi(selectedLokasi)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
        }

        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedKomoditas = (spinner3.selectedItem as SpinnerItem).name
                sharedViewModel.setSelectedKomoditas(selectedKomoditas)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle case where nothing is selected
            }
        }
        // Show the bottom sheet dialog
        bottomSheetDialog.show()
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun exportDataToPDF() {
        val dataFragments = mutableListOf<DataExportable>()

        for (i in 0 until (binding.viewPager.adapter?.count ?: 0)) {
            val fragment =
                binding.viewPager.adapter?.instantiateItem(binding.viewPager, i) as? DataExportable
            fragment?.let { dataFragments.add(it) }
        }

        if (dataFragments.isNotEmpty()) {
            val pdfExporter = PDFExporter()

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "my_pdf_file.pdf")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val resolver = requireContext().contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let { pdfUri ->
                try {
                    val outputStream = resolver.openOutputStream(pdfUri)
                    outputStream?.use { outputStream ->
                        pdfExporter.exportToPDF(outputStream, dataFragments)
                        Toast.makeText(
                            requireContext(),
                            "PDF exported successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Open the PDF using a dialog
                        openPDFWithDialog(pdfUri)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Failed to export PDF", Toast.LENGTH_SHORT)
                        .show()
                }
            } ?: run {
                Toast.makeText(requireContext(), "Failed to create PDF file", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun openPDFWithDialog(pdfUri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(pdfUri, "application/pdf")
        }

        val chooserIntent = Intent.createChooser(intent, "Open PDF")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        try {
            requireContext().startActivity(chooserIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "No PDF viewer app found", Toast.LENGTH_SHORT).show()
        }
    }


}