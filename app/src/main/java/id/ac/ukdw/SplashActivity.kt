package id.ac.ukdw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import id.ac.ukdw.drones_isai.R
import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi

class SplashActivity : AppCompatActivity() {

    private lateinit var requestLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        requestLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if (it){
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                },2000)
            }else{
                Toast.makeText(this, "Permision Not Granted", Toast.LENGTH_SHORT).show()
            }
        }
        requestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

}