package id.ac.ukdw.drones_isai.ui.tentang

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.ac.ukdw.drones_isai.databinding.FragmentTentang5Binding


class Tentang5Fragment : Fragment() {

    private lateinit var binding: FragmentTentang5Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentTentang5Binding.inflate(inflater, container, false)
        val view = binding.root

        binding.kedaireka.setOnClickListener {
            val websiteUrl = "https://kedaireka.id/"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }
        binding.beehive.setOnClickListener {
            val websiteUrl = "  //beehivedrones.com/"
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
        binding.maps.setOnClickListener {
            val websiteUrl = "https://goo.gl/maps/U9PuKwfjW5f2rBy3A"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }
        binding.email.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_EMAIL, "contact@beehivedrones.com")
            intent.putExtra(Intent.EXTRA_SUBJECT, "subject")
            intent.putExtra(Intent.EXTRA_TEXT, "message")
            intent.type = "message/rfc822"
            startActivity(Intent.createChooser(intent, "Select email"))
        }
        binding.call.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + "+6281 8999 7715")
            startActivity(dialIntent)
        }
        binding.ig.setOnClickListener {
            val websiteUrl = "https://www.instagram.com/beehivedrones/?hl=id"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }
        binding.fb.setOnClickListener {

        }
        binding.lk.setOnClickListener {
            val websiteUrl = "https://id.linkedin.com/company/beehivedrones"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }

        return view
    }

}