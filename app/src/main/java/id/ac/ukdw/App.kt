package id.ac.ukdw

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import id.ac.ukdw.services.LocationWorker
import java.util.concurrent.TimeUnit

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        // Schedule the WorkManager task
        scheduleLocationWork()
    }

    private fun scheduleLocationWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<LocationWorker>(10, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "LocationWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }
}