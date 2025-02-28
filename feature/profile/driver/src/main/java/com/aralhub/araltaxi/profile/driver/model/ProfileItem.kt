package com.aralhub.araltaxi.profile.driver.model

import com.aralhub.indrive.core.data.model.driver.DriverProfile
import com.aralhub.ui.model.profile.ProfileItem
import com.aralhub.ui.model.profile.ProfileItemCategory


fun DriverProfile.toProfileItemList(): List<ProfileItem>{
    return listOf(
        ProfileItem(fullName, "Atı", ProfileItemCategory.NAME),
        ProfileItem(phoneNumber, "Telefon", ProfileItemCategory.PHONE),
        ProfileItem(vehicleType, "Avtomobil", ProfileItemCategory.CAR),
        ProfileItem(color, "Reńi", ProfileItemCategory.CAR_COLOR),
        ProfileItem(plateNumber, "Avtomobil nomeri", ProfileItemCategory.CAR_NUMBER)
    )
}



