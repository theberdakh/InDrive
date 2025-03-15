package com.aralhub.araltaxi.ride

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.aralhub.araltaxi.client.ride.R
import com.aralhub.araltaxi.core.domain.client.GetClientRideStatusUseCase
import com.aralhub.indrive.core.data.model.ride.RideStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RideService: Service() {
    @Inject lateinit var useCase: GetClientRideStatusUseCase
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var notificationManager: NotificationManager
    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NotificationManager::class.java)
        observeStates()
    }

    private fun observeStates() {
        scope.launch {
            useCase().collect {
                updateNotification(when(it) {
                    is RideStatus.DriverOnTheWay -> "Driver is on the way"
                    is RideStatus.DriverWaitingClient -> "Driver is waiting for you"
                    is RideStatus.PaidWaiting -> "Paid waiting"
                    is RideStatus.PaidWaitingStarted -> "Paid waiting started"
                    is RideStatus.RideCompleted -> "Ride completed"
                    is RideStatus.RideStarted -> "Ride started"
                    is RideStatus.Unknown -> "Unknown"
                })
            }
        }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Ride is going...")
            .setContentText("Updating text")
            .setSmallIcon(R.drawable.notification_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .build()
        val foregroundServiceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC } else { UNSPECIFIED_FOREGROUND_SERVICE_TYPE }
        ServiceCompat.startForeground(this, ID, notification, foregroundServiceType)

        return START_STICKY
    }

    private fun updateNotification(text: String) {
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Ride is going...")
            .setContentText(text)
            .setSmallIcon(R.drawable.notification_icon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .build()

        scope.launch(Dispatchers.Main) {
            notificationManager.notify(ID, notification)
        }
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationManager.createNotificationChannel(channel)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


    companion object {
        const val CHANNEL_ID = "location_channel"
        const val CHANNEL_NAME = "Foreground Service Channel"
        const val UNSPECIFIED_FOREGROUND_SERVICE_TYPE = 0
        const val ID = 100
    }


}