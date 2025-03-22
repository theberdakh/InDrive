package com.aralhub.indrive.core.data.model.ride

/**
 * @param DRIVER_WAITING_CLIENT Водитель ожидает вас. После 0.5 минут начнется платное ожидание (100 сум/минута)
 * @param RIDE_STARTED_AFTER_WAITING Платное ожидание началось
 * @param RIDE_STARTED Поездка начата после ожидания. Сумма ожидания: 300.0 сум
 * @param RIDE_COMPLETED Поездка завершена
 * @param DRIVER_ON_THE_WAY Статус поездки обновлен на driver_on_the_way
 * @param CANCELED_BY_DRIVER cancelled_by_driver
 * */
sealed class RideStatus {
    data class DriverWaitingClient(val message: String) : RideStatus()
    data class PaidWaiting(val message: String) : RideStatus()
    data class PaidWaitingStarted(val message: String) : RideStatus()
    data class RideStarted(val message: String) : RideStatus()
    data class RideCompleted(val message: String) : RideStatus()
    data class DriverOnTheWay(val message: String) : RideStatus()
    data class CanceledByDriver(val message: String) : RideStatus()
    data class Unknown(val error: String) : RideStatus()
}
