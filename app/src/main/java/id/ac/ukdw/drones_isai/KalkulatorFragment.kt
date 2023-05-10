package id.ac.ukdw.drones_isai

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import id.ac.ukdw.drones_isai.databinding.FragmentAboutBinding
import id.ac.ukdw.drones_isai.databinding.FragmentKalkulatorBinding


class KalkulatorFragment : Fragment() {

    private var _binding: FragmentKalkulatorBinding? = null
    private val binding get() = _binding!!



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.top_toolbar)
        val toolbarTitle = toolbar.findViewById<TextView>(R.id.toolbar_title)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        // Set the title text alignment
        toolbarTitle.gravity = Gravity.CENTER
        toolbarTitle.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

        binding.icHelp.setOnClickListener {
            Toast.makeText(requireContext(), "Icon clicked", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_nav_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_icon -> {
                // Handle click on the custom icon here
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentKalkulatorBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }



}