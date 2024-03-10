package id.ac.ukdw.drones_isai.ui.tren


import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import id.ac.ukdw.adapter.SpinnerFilterAdapter
import id.ac.ukdw.adapter.ViewPagerAdapter
import id.ac.ukdw.helper.PDFExporter
import id.ac.ukdw.data.model.Body
import id.ac.ukdw.drones_isai.R
import id.ac.ukdw.drones_isai.databinding.FilterBottomPopupBinding
import id.ac.ukdw.drones_isai.databinding.FragmentTrenBinding
import id.ac.ukdw.drones_isai.utils.GraphCaptureUtils
import id.ac.ukdw.helper.DataExportable
import id.ac.ukdw.viewmodel.MainViewModel
import id.ac.ukdw.viewmodel.SharedFilterViewModel
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class SpinnerItem(val id: Int, val name: String)
class TrendFragment : Fragment() {

    private lateinit var binding: FragmentTrenBinding
    private lateinit var viewPager: ViewPager
    private lateinit var sharedViewModel: SharedFilterViewModel

    private val viewModel: MainViewModel by viewModels()

    private val tahun = mutableListOf<SpinnerItem>()
    private val lokasi = mutableListOf<SpinnerItem>()
    private val komoditas = mutableListOf<SpinnerItem>()
    private var selectedTahun: String? = null
    private var selectedLokasi: String? = null
    private var selectedComodity: String? = null


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
        sharedViewModel.buttonState.observe(viewLifecycleOwner){isPressed ->
            if (isPressed){
                selectedTahun = sharedViewModel.tahunValue.value
                selectedLokasi = sharedViewModel.lokasiValue.value
                selectedComodity = sharedViewModel.comodityValue.value
                val filter = "$selectedTahun $selectedLokasi $selectedComodity"
                binding.filterAktif.text = filter
            }else{
                binding.filterAktif.text = "Semua"
            }
        }


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

        // Set the selected values of the spinners if they were previously selected
        sharedViewModel.tahunValue.value?.let { selectedTahun ->
            val selectedTahunIndex = tahun.indexOfFirst { it.name == selectedTahun }
            if (selectedTahunIndex != -1) {
                spinner1.setSelection(selectedTahunIndex)
            }
        }

        sharedViewModel.lokasiValue.value?.let { selectedLokasi ->
            val selectedLokasiIndex = lokasi.indexOfFirst { it.name == selectedLokasi }
            if (selectedLokasiIndex != -1) {
                spinner2.setSelection(selectedLokasiIndex)
            }
        }

        sharedViewModel.comodityValue.value?.let { selectedKomoditas ->
            val selectedKomoditasIndex = komoditas.indexOfFirst { it.name == selectedKomoditas }
            if (selectedKomoditasIndex != -1) {
                spinner3.setSelection(selectedKomoditasIndex)
            }
        }

        val btn = dialogBinding.filterBtn
        dialogBinding.apply {
            filterBtn.setOnClickListener {
                val selectedTahun = (spinner1.selectedItem as SpinnerItem).name
                val selectedLokasi = (spinner2.selectedItem as SpinnerItem).name
                val selectedKomoditas = (spinner3.selectedItem as SpinnerItem).name
                btn.showLoading()
                sharedViewModel.setButtonStateAndValue(
                    true,
                    selectedTahun,
                    selectedLokasi,
                    selectedKomoditas
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

            val inputFileName = "my_pdf_file.pdf" // Default file name

            val builder = AlertDialog.Builder(requireContext())

            // Inflate the custom layout with padding
            val customLayout = LayoutInflater.from(requireContext()).inflate(
                R.layout.save_dialog_layout,
                null
            )

            val input = customLayout.findViewById<EditText>(R.id.editTextFileName)

            builder.setTitle("Save As")
                .setView(customLayout)
                .setPositiveButton("Save") { _, _ ->
                    val userInputFileName = input.text.toString()
                    val pdfFileName = if (userInputFileName.isNotBlank()) {
                        "$userInputFileName.pdf"
                    } else {
                        inputFileName
                    }

                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, pdfFileName)
                        put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                    }

                    val resolver = requireContext().contentResolver

                    try {
                        // Use ContentResolver to create a new PDF file
                        val pdfUri =
                            resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                        pdfUri?.let { uri ->
                            resolver.openFileDescriptor(uri, "w")?.use { descriptor ->
                                // Open an OutputStream using the file descriptor
                                val outputStream = FileOutputStream(descriptor.fileDescriptor)

                                pdfExporter.exportToPDF(outputStream, dataFragments)
                                Toast.makeText(
                                    requireContext(),
                                    "PDF exported successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Open the PDF using a dialog
                                openPDFWithDialog(uri)
                            }
                        } ?: run {
                            Toast.makeText(
                                requireContext(),
                                "Failed to create PDF file",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Failed to export PDF", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.cancel()
                }

            builder.show()
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