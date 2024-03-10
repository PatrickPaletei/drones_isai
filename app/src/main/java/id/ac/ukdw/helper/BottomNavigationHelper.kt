package id.ac.ukdw.helper

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.ac.ukdw.drones_isai.R

class BottomNavigationHelper(private val fragment: Fragment, private val navController: NavController) {

    // Add a property to store the current destination ID
    private var currentDestinationId: Int = 0
    fun setupWithBottomNavigationView(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            navigateToDestination(menuItem.itemId)
            true
        }

//         Set the current selected item initially
        val navGraph = navController.navInflater.inflate(R.navigation.main_navigation)
        val firstDestination = navGraph.startDestinationId + 2
        bottomNavigationView.selectedItemId = firstDestination


        // Listen for changes in the selected destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigationView.selectedItemId = destination.id
            currentDestinationId = destination.id

            // Set the highlighted item based on the current destination
            setHighlightedItem(bottomNavigationView, currentDestinationId)
        }
    }
    fun setHighlightedItem(bottomNavigationView: BottomNavigationView, destinationId: Int) {
        bottomNavigationView.menu.findItem(getMenuItemId(destinationId))?.isChecked = true
    }

    private fun navigateToDestination(itemId: Int) {
        val action = when (itemId) {
            R.id.menu_cari_lokasi -> R.id.locationFragment
            R.id.menu_tren -> R.id.trenFragment
            R.id.menu_bantuan -> R.id.tentangKarbonFragment
            R.id.menu_about -> R.id.tentangFragment
            else -> return
        }

        fragment.findNavController().navigate(action)
    }
    private fun getMenuItemId(destinationId: Int): Int {
        return when (destinationId) {
            R.id.locationFragment -> R.id.menu_cari_lokasi
            R.id.trenFragment -> R.id.menu_tren
            R.id.tentangKarbonFragment -> R.id.menu_bantuan
            R.id.tentangFragment -> R.id.menu_about
            else -> 0 // Handle other cases if needed
        }
    }

}
