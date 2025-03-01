package com.aralhub.araltaxi.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.PermissionChecker

class LocationService : Service() {
    private var locationManager: LocationManager? = null
    private var notificationManager: NotificationManager? = null
    private val locationListener =  LocationListener { location ->
        val latitude = location.latitude
        val longitude = location.longitude
        updateNotification(latitude, longitude)
    }

    override fun onBind(p0: Intent?): IBinder? = null

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        startForeground()
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL, SMALLEST_DISTANCE, locationListener)
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
        return START_STICKY
    }

    private fun startForeground() {
        val locationPermission = PermissionChecker.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (locationPermission != PermissionChecker.PERMISSION_GRANTED) {
            stopSelf()
            return
        }
        notificationManager?.createNotificationChannel(NotificationChannel(CHANNEL_ID, "Location Service", NotificationManager.IMPORTANCE_HIGH))
        val notification = buildNotification("Waiting for location...")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
        } else {
            startForeground(ID, notification)
        }
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