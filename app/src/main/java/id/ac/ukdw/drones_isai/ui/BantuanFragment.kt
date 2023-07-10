package id.ac.ukdw.drones_isai.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.ukdw.adapter.AccordionAdapter
import id.ac.ukdw.data.AccordionItem
import id.ac.ukdw.drones_isai.R
import id.ac.ukdw.drones_isai.databinding.FragmentBantuanBinding


class BantuanFragment : Fragment() {

    private lateinit var accordionRecyclerView: RecyclerView
    private var _binding: FragmentBantuanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentBantuanBinding.inflate(inflater,container,false)
        val view = binding.root

        accordionRecyclerView = view.findViewById(R.id.recycler_view)
        accordionRecyclerView.layoutManager = LinearLayoutManager(context)

        val accordionItems = listOf(
            AccordionItem("Deskripsi Kalkulator Karbon", "Kalkulator Karbon merupakan fitur yang dapat digunakan untuk melakukan perhitungan prediksi nilai karbon berdasar foto citra NIR"),
            AccordionItem("Cara Penggunaan", "1. Klik pada form unggah file atau klik pada icon kamera. \n" +
                    "\n" +
                    "2. Pilih foto citra NIR dengan ekstensi .jpg/.jpeg/.png. \n" +
                    "\n" +
                    "3. Klik tombol unggah. \n" +
                    "\n" +
                    "4. Tunggu hingga hasil prediksi nilai karbon muncul pada form Hasil Estimasi Karbon."),
            AccordionItem("Proses Perhitungan Kalkulator Karbon", "Perhitungan dilakukan oleh sistem yang telah dirancang dengan kemampuan machine learning untuk menghitung piksel dari citra NIR yang diunggah oleh pengguna")
        )

        val adapter = AccordionAdapter(accordionItems)
        accordionRecyclerView.adapter = adapter

        return view
    }


}