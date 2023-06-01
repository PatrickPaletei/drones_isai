package id.ac.ukdw

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.ac.ukdw.drones_isai.NilaiHSTFragment
import id.ac.ukdw.drones_isai.R
import id.ac.ukdw.helper.BottomNavigationHelper

class MainActivity : AppCompatActivity(), NilaiHSTFragment.BottomNavigationViewListener {


    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        //attach bottom nav
        val bottomNavigationHelper = BottomNavigationHelper(navHostFragment, navController)
        bottomNavigationHelper.setupWithBottomNavigationView(navView)

        // Manually set the highlighted item
        val menuItemId = R.id.menu_about
        bottomNavigationHelper.setHighlightedItem(navView, menuItemId)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun hideBottomNavigationView() {
        // Hide the bottom navigation view here
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavigationView() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
    }
}




