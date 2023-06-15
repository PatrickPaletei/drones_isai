package id.ac.ukdw.drones_isai


import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import id.ac.ukdw.adapter.SpinnerFilterAdapter
import id.ac.ukdw.adapter.ViewPagerAdapter
import id.ac.ukdw.data.PDFExporter
import id.ac.ukdw.data.model.Body
import id.ac.ukdw.drones_isai.databinding.FilterBottomPopupBinding
import id.ac.ukdw.drones_isai.databinding.FragmentTrenBinding
import id.ac.ukdw.helper.DataExportable
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


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
        binding.viewPager.adapter = adapter

        // Set up the TabLayout
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        // Set up the button click listener
        binding.filterBtn.setOnClickListener {
            showBottomPopupDialog()
        }
        binding.btnExport.setOnClickListener {
            exportDataToPDF()
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun exportDataToPDF() {
        val dataFragments = mutableListOf<DataExportable>()

        for (i in 0 until (binding.viewPager.adapter?.count ?: 0)) {
            val fragment = binding.viewPager.adapter?.instantiateItem(binding.viewPager, i) as? DataExportable
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
                        Toast.makeText(requireContext(), "PDF exported successfully", Toast.LENGTH_SHORT).show()

                        // Open the PDF using a dialog
                        openPDFWithDialog(pdfUri)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Failed to export PDF", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(requireContext(), "Failed to create PDF file", Toast.LENGTH_SHORT).show()
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