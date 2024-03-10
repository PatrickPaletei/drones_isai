package id.ac.ukdw

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.ac.ukdw.services.BackgroundCheckingLocationService
import id.ac.ukdw.drones_isai.R
import id.ac.ukdw.helper.BottomNavigationHelper
import id.ac.ukdw.services.LocationEvent
import id.ac.ukdw.services.LocationService
import id.ac.ukdw.viewmodel.SharedFilterViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {


    private lateinit var navController: NavController
    private lateinit var sharedViewModel: SharedFilterViewModel
    private lateinit var bottomNavigationHelper: BottomNavigationHelper

    private val locationServiceIntent by lazy { Intent(this, LocationService::class.java) }
    private val backgroundServiceIntent by lazy { Intent(this, BackgroundCheckingLocationService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedViewModel = ViewModelProvider(this)[SharedFilterViewModel::class.java]

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Initialize bottomNavigationHelper
        bottomNavigationHelper = BottomNavigationHelper(navHostFragment, navController)
        bottomNavigationHelper.setupWithBottomNavigationView(navView)


        handleIntent(intent)
        startServices()
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == "OPEN_FRAGMENT_ACTION") {
            val locationId = intent.getStringExtra("locationId")
            val date = intent.getStringExtra("dateService")
            // Redirect to the desired fragment using locationId
            Log.d("locationId", "handleIntent: $locationId $date")

            // Create a bundle to pass data to the fragment
            val bundle = Bundle()
            bundle.putString("locationId", locationId)
            bundle.putString("dateService",date)
            // Set the bundle as arguments for the fragment
            val action = R.id.locationFragment
            navController.navigate(action, bundle)
        }
    }

    private fun startServices(){
        // Start both services
        startService(locationServiceIntent)
        startService(backgroundServiceIntent)
    }


    override fun onStart() {
        super.onStart()
        checkPermissions()

    }

    private val backgroundLocation =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }

    private val locationPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            when {
                it.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        if (ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            backgroundLocation.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        }
                    }

                }

                it.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {

                }
            }
        }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                locationPermissions.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            } else {
                startService(locationServiceIntent)
            }
        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }

    //disable buat onbackground
//    override fun onDestroy() {
//        super.onDestroy()
//        stopService(locationServiceIntent)
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this)
//        }
//    }



}




