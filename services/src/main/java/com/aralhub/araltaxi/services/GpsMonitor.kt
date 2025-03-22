package com.aralhub.araltaxi.services

interface GpsMonitor {
    fun startMonitoring()
    fun stopMonitoring()
    fun isGpsEnabled(): Boolean
}