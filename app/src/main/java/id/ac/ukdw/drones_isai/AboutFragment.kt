package id.ac.ukdw.drones_isai

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.ac.ukdw.drones_isai.databinding.FragmentAboutBinding
import id.ac.ukdw.helper.BottomNavigationHelper


class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_about, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController()
        val bottomNavigationHelper = BottomNavigationHelper(this, navController)

        bottomNavigationHelper.setupWithBottomNavigationView(bottomNavigationView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}