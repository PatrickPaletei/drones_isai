package id.ac.ukdw.helper

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.ac.ukdw.drones_isai.R

class BottomNavigationHelper(
    private val fragment: Fragment,
    private val navController: NavController
) {
    fun setupWithBottomNavigationView(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId != bottomNavigationView.selectedItemId) {
                navigateToDestination(menuItem.itemId)
            }
            true
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val menuItemId = getMenuItemIdForDestination(destination.id)
            bottomNavigationView.selectedItemId = menuItemId
        }
    }

    //    private fun navigateToDestination(itemId: Int) {
//        val action = when (itemId) {
//            R.id.menu_cari_lokasi -> R.id.aboutFragment
//            R.id.menu_tren -> R.id.aboutFragment
//            R.id.menu_kalkulator -> R.id.kalkulatorFragment
//            R.id.menu_about -> R.id.aboutFragment
//            else -> return
//        }
//
//        navController.navigate(action)
//    }

    private fun getMenuItemIdForDestination(destinationId: Int): Int {
        return when (destinationId) {
            R.id.menu_cari_lokasi -> R.id.aboutFragment
            R.id.menu_tren -> R.id.aboutFragment
            R.id.menu_kalkulator -> R.id.kalkulatorFragment
            R.id.menu_about -> R.id.aboutFragment
            else -> 0
        }
    }
    private fun getActionForMenuItem(itemId: Int): Int {
        return when (itemId) {
            R.id.menu_cari_lokasi -> R.id.aboutFragment
            R.id.menu_tren -> R.id.aboutFragment
            R.id.menu_kalkulator -> R.id.kalkulatorFragment
            R.id.menu_about -> R.id.aboutFragment
            else -> 0
        }
    }
    private fun navigateToDestination(itemId: Int) {
        val action = getActionForMenuItem(itemId)
        navController.navigate(action)
    }
}