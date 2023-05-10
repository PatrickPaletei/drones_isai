package id.ac.ukdw.drones_isai

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import id.ac.ukdw.drones_isai.databinding.FragmentAboutBinding


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Perform any additional setup or operations for your splash screen here

        // Simulate a delay for the splash screen (e.g., 2 seconds)
        Handler().postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_aboutFragment)
        }, 2000)
    }
}