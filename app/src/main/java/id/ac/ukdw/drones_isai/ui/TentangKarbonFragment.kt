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
            AccordionItem("Apa itu karbon tanah?","Karbon tanah organik (SOC=soil organic carbon) menurut Nishina, et al. (2013) merupakan stok karbon terbesar di ekosistem darat dan memainkan peran kunci dalam umpan balik biosfir untuk peningkatan karbon dioksida atmosfer di dunia, sehingga atmosfir bumi akan menjadi lebih hangat.") ,
            AccordionItem("Mengapa karbon itu peting?","Karbon merupakan unsur dasar dari molekul-molekul penting penyusun kehidupan seperti karbohidrat, protein, lemak dan DNA. Karbon berperan sebagai thermostat alami dalam kehidupan dan salah satu penyusun gas-gas rumah kaca di atmosfer.")
        )
        val adapter = AccordionAdapter(accordionItem)
        accordionRecyclerView.adapter = adapter

        return view
    }


}