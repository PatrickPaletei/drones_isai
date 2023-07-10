package id.ac.ukdw.drones_isai.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.ac.ukdw.adapter.AccordionAdapter
import id.ac.ukdw.data.AccordionItem
import id.ac.ukdw.drones_isai.R
import id.ac.ukdw.drones_isai.databinding.FragmentBantuanBinding
import id.ac.ukdw.drones_isai.databinding.FragmentTentangKarbonBinding


class TentangKarbonFragment : Fragment() {

    private lateinit var accordionRecyclerView:RecyclerView
    private var _binding:FragmentTentangKarbonBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentTentangKarbonBinding.inflate(inflater,container,false)
        val view = binding.root
        accordionRecyclerView = view.findViewById(R.id.recycler_view)
        accordionRecyclerView.layoutManager = LinearLayoutManager(context)
        val accordionItem = listOf(
            AccordionItem("Apa yang dimaksud dengan karbon?","Karbon berasal dari bahasa Latin yaitu carbo yang artinya arang, batu bara. Secara kimia, carbon adalah unsur kimia yang memiliki simbol C dengan urutan nomor atom 6 pada tabel periodik. Definisi lain menyebutkan, carbon adalah suatu unsur yang diserap dari atmosfer melalui proses fotosintesis dan disimpan di dalam bentuk biomassa suatu vegetasi.") ,
            AccordionItem("Apa yang dimaksud dengan emisi karbon?","Emisi karbon merupakan proses pelepasan gas-gas yang mengandung karbon ke atmosfer yang terjadi secara alami maupun dipicu aktivitas manusia, seperti deforestasi, konsumsi listrik, hingga kegiatan industri manufaktur."),
            AccordionItem("Apa dampak positif karbon?","1. Dasar Kehidupan: Karbon membentuk molekul organik yang penting untuk kehidupan, seperti protein, karbohidrat, lemak, dan asam nukleat. Tanpa karbon, kehidupan seperti yang kita kenal tidak akan mungkin ada.\n" +
                    "2. Sumber Energi: Bahan bakar fosil, yang terdiri dari karbon, telah menjadi sumber energi utama selama berabad-abad. Penggunaan bahan bakar fosil telah memberikan energi yang diperlukan untuk menggerakkan kendaraan, pembangkit listrik, dan industri."),
            AccordionItem("Apa dampak negatif karbon?","1. Pemanasan Global: Emisi karbon, terutama dalam bentuk karbon dioksida (CO2) dari pembakaran bahan bakar fosil dan deforestasi, menyebabkan peningkatan konsentrasi gas rumah kaca di atmosfer.\n" +
                    "2. Pencemaran Udara: Pembakaran bahan bakar fosil menghasilkan emisi karbon monoksida (CO) dan partikel-partikel lain yang dapat mencemari udara. Polusi udara ini dapat menyebabkan masalah kesehatan manusia, seperti penyakit pernapasan dan masalah kardiovaskular.\n" +
                    "3. Kerusakan Lingkungan: Ekstraksi dan penggunaan bahan bakar fosil, yang mengandung karbon, dapat menyebabkan kerusakan lingkungan. Ini termasuk kerusakan ekosistem, deforestasi, degradasi tanah, dan kerusakan habitat alami, yang memiliki dampak negatif pada keanekaragaman hayati dan keberlanjutan lingkungan.")
        )
        val adapter = AccordionAdapter(accordionItem)
        accordionRecyclerView.adapter = adapter

        return view
    }


}