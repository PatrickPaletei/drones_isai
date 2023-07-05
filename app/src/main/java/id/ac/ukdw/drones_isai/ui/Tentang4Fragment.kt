package id.ac.ukdw.drones_isai.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.ac.ukdw.drones_isai.R
import id.ac.ukdw.drones_isai.databinding.FragmentKarbonTerserapBinding
import id.ac.ukdw.drones_isai.databinding.FragmentTentang4Binding


class Tentang4Fragment : Fragment() {

    private lateinit var binding:FragmentTentang4Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTentang4Binding.inflate(inflater, container, false)
        val view = binding.root

        binding.website.setOnClickListener {
            val websiteUrl = "https://www.google.com"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }

        return view
    }

}