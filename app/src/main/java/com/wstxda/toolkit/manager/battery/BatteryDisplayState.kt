package com.wstxda.toolkit.manager.battery

enum class BatteryDisplayState {
    PERCENTAGE, ELECTRICAL, TEMPERATURE, VOLTAGE;

    fun next(): BatteryDisplayState {
        val values = entries
        return values[(ordinal + 1) % values.size]
    }
}