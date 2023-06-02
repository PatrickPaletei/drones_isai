package id.ac.ukdw.drones_isai

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.ac.ukdw.MainActivity
import id.ac.ukdw.drones_isai.databinding.FragmentTerserapLandBinding


class TerserapLandFragment : Fragment() {


    private var _binding: FragmentTerserapLandBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTerserapLandBinding.inflate(inflater, container, false)
        val view = binding.root
        // Force landscape orientation
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        binding.fabClose.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        val mainActivity = activity as? MainActivity
        mainActivity?.hideBottomNavigationView()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // Reset the orientation when the fragment is destroyed
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        // Show the bottom navigation view when the fragment is destroyed
        val mainActivity = activity as? MainActivity
        mainActivity?.showBottomNavigationView()
    }


}