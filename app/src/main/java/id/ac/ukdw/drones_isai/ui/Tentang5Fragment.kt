package id.ac.ukdw.drones_isai.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.ac.ukdw.drones_isai.R
import id.ac.ukdw.drones_isai.databinding.FragmentTentang4Binding
import id.ac.ukdw.drones_isai.databinding.FragmentTentang5Binding


class Tentang5Fragment : Fragment() {

    private lateinit var binding: FragmentTentang5Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTentang5Binding.inflate(inflater, container, false)
        val view = binding.root

        binding.kedaireka.setOnClickListener {
            val websiteUrl = "https://kedaireka.id/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }
        binding.beehive.setOnClickListener {
            val websiteUrl = "https://beehivedrones.com/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }
        binding.ukdw.setOnClickListener {
            val websiteUrl = "https://www.ukdw.ac.id/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }
        binding.fti.setOnClickListener {
            val websiteUrl = "https://www.ukdw.ac.id/akademik/fakultas-teknologi-informasi/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }

        return view
    }

}