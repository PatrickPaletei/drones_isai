package id.ac.ukdw.helper

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.ac.ukdw.drones_isai.R

class BottomNavigationHelper(private val fragment: Fragment, private val navController: NavController) {
    fun setupWithBottomNavigationView(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
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
        }
    }
    fun setHighlightedItem(bottomNavigationView: BottomNavigationView, itemId: Int) {
        bottomNavigationView.menu.findItem(itemId)?.isChecked = true
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
}
