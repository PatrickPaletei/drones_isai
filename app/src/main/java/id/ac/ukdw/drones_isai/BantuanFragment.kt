package id.ac.ukdw.drones_isai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.ukdw.adapter.AccordionAdapter
import id.ac.ukdw.data.AccordionItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class BantuanFragment : Fragment() {

    private lateinit var accordionRecyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_bantuan, container, false)

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