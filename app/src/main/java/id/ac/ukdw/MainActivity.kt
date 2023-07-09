package id.ac.ukdw

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.ac.ukdw.drones_isai.R
import id.ac.ukdw.helper.BottomNavigationHelper
import id.ac.ukdw.viewmodel.SharedFilterViewModel

class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    private lateinit var sharedViewModel: SharedFilterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedViewModel = ViewModelProvider(this).get(SharedFilterViewModel::class.java)

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

}




