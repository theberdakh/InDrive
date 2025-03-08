package com.aralhub.araltaxi.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.PermissionChecker
import com.aralhub.araltaxi.core.common.sharedpreference.DriverSharedPreference
import com.aralhub.araltaxi.core.domain.driver.SendDriverLocationUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var notification: LocationNotification

    @Inject
    lateinit var sendDriverLocationUseCase: SendDriverLocationUseCase

    @Inject
    lateinit var driverSharedPreference: DriverSharedPreference

    private var locationManager: LocationManager? = null
    private var notificationManager: NotificationManager? = null
    private val locationListener = LocationListener { location ->
        val latitude = location.latitude
        val longitude = location.longitude
        updateNotification(latitude, longitude)
        scope.launch {
            sendDriverLocationUseCase.invoke(
                latitude = latitude,
                longitude = longitude,
                driverSharedPreference.distance
            )
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        startForeground()
        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            INTERVAL,
            SMALLEST_DISTANCE,
            locationListener
        )
    }

    private fun updateNotification(latitude: Double, longitude: Double) {
        val newNotification = buildNotification("Location: $latitude, $longitude")
        notificationManager?.notify(ID, newNotification)
    }

    private fun buildNotification(locationText: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Tracking Location...")
            .setContentText(locationText)
            .setSmallIcon(R.drawable.icon)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground()
        return super.onStartCommand(intent, flags, startId)

    }

    private fun startForeground() {
        Log.d("LocationService", "startForeground")
        val locationPermission =
            PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (locationPermission != PermissionChecker.PERMISSION_GRANTED) {
            stopSelf()
            return
        }

        notification.createChannel()

        ServiceCompat.startForeground(
            /* service = */ this,
            /* id = */ ID, // Cannot be 0
            /* notification = */ notification.createNotification(),
            /* foregroundServiceType = */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            } else {
                0
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        locationManager?.removeUpdates(locationListener)
    }

    companion object {
        const val INTERVAL: Long = 1 * 1000
        const val SMALLEST_DISTANCE: Float = 1f
        const val CHANNEL_ID = "location_channel"
        const val ID = 100
    }
}