package id.ac.ukdw.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import id.ac.ukdw.MainActivity
import id.ac.ukdw.drones_isai.R
import id.ac.ukdw.viewmodel.NotificationViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlin.math.pow

class BackgroundCheckingLocationService : Service() {

    private val notificationViewModel: NotificationViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(NotificationViewModel::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        notificationViewModel.fetchDataLocation()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @Subscribe
    fun receiveLocationEvent(locationEvent: LocationEvent) {
        val lat = locationEvent.latitude
        val long = locationEvent.longitude
        Log.d("latlongskrng", "receiveLocationEvent: $lat $long")
        if (lat != null && long != null) {
            notificationViewModel.filteredData.observeForever { filteredData ->

                for (location in filteredData) {
                    //dibalik karena dari database kebalik :)
                    val distance = distance(lat, long, location.longitude, location.lattitude)
//                    val distance = distance(-7.767577291842697, 110.47681335135879,location.longitude, location.lattitude)
                    Log.d("disSer", "receiveLocationEvent: $distance")


                    val distanceThreshold = 1 // distance threshold here in kilometers

                    if (distance <= distanceThreshold) {
                        // Location is within range, create and show a notification
                        showNotification(location.idSample,location.date)
                    }
                }
            }
        }
    }

    private fun showNotification(locationId: String, date: String) {
        val notificationContent =
            "Anda Berada Di Dekat Lokasi Sample: $locationId Pada Tanggal $date"

        // Create an intent to open the app and redirect to a specific fragment
        val intent = Intent(this, MainActivity::class.java)
        intent.action = "OPEN_FRAGMENT_ACTION"
        intent.putExtra("locationId", locationId)
        intent.putExtra("dateService", date)

        // Generate a unique requestCode based on locationId and date
        val requestCode = (locationId + date).hashCode()

        // Create a PendingIntent
        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        val notification = NotificationCompat.Builder(this, LocationService.CHANNEL_ID)
            .setContentTitle("Beehive Drones")
            .setContentText(notificationContent)
            .setSmallIcon(R.drawable.icon_beehive)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent) // Set the PendingIntent
            .setAutoCancel(true) // Automatically dismiss the notification when clicked

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(LocationService.CHANNEL_ID)
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Use a unique notification ID based on the current timestamp
        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notification.build())
        Log.d("Notification", "Notification shown for locationId: $locationId")
    }


    private fun distance(latFrom: Double, lonFrom: Double, latTo: Double, lonTo: Double): Double {
        val earthRadiusKm = 6371.0

        val dLat = Math.toRadians(latTo - latFrom)
        val dLon = Math.toRadians(lonTo - lonFrom)

        val sinDLat = kotlin.math.sin(dLat / 2)
        val sinDLon = kotlin.math.sin(dLon / 2)

        val a = sinDLat * sinDLat +
                kotlin.math.cos(Math.toRadians(latFrom)) * kotlin.math.cos(
            Math.toRadians(latTo)
        ) * sinDLon * sinDLon

        val c = 2 * kotlin.math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))

        return earthRadiusKm * c
    }


}