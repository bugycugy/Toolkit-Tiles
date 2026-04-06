package com.wstxda.toolkit.ui.icon

import android.content.Context
import android.graphics.drawable.Icon
import com.wstxda.toolkit.R
import com.wstxda.toolkit.manager.battery.BatteryChargingSource
import com.wstxda.toolkit.manager.battery.BatteryInfo

class BatteryIconProvider(private val context: Context) {

    fun getIcon(info: BatteryInfo): Icon {
        val resId = when {
            info.isPowerSave -> R.drawable.ic_battery_saver

            info.isFull -> R.drawable.ic_battery_full

            info.isCharging -> when (info.chargingSource) {
                BatteryChargingSource.AC -> R.drawable.ic_battery_charging_ac
                BatteryChargingSource.USB -> R.drawable.ic_battery_charging_usb
                BatteryChargingSource.WIRELESS -> R.drawable.ic_battery_charging_wireless
                BatteryChargingSource.DOCK -> R.drawable.ic_battery_charging_dock
                BatteryChargingSource.NONE -> R.drawable.ic_battery_charging_ac
            }

            info.isLow -> R.drawable.ic_battery_low

            else -> R.drawable.ic_battery
        }
        return Icon.createWithResource(context, resId)
    }
}