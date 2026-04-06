package com.wstxda.toolkit.tiles.battery

import android.service.quicksettings.Tile
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.battery.BatteryModule
import com.wstxda.toolkit.ui.icon.BatteryIconProvider
import com.wstxda.toolkit.ui.label.BatteryLabelProvider
import kotlinx.coroutines.flow.Flow

class BatteryTileService : BaseTileService() {

    private val batteryManager by lazy { BatteryModule.getInstance(applicationContext) }
    private val batteryLabelProvider by lazy { BatteryLabelProvider(applicationContext) }
    private val batteryIconProvider by lazy { BatteryIconProvider(applicationContext) }

    override fun onStartListening() {
        super.onStartListening()
        batteryManager.setListening(true)
    }

    override fun onStopListening() {
        super.onStopListening()
        batteryManager.setListening(false)
    }

    override fun onClick() {
        batteryManager.toggle()
    }

    override fun flowsToCollect(): List<Flow<*>> {
        return listOf(
            batteryManager.batteryInfo,
            batteryManager.displayState,
        )
    }

    override fun updateTile() {
        val info = batteryManager.batteryInfo.value
        val state = batteryManager.displayState.value

        setTileState(
            state = if (info.isCharging) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE,
            label = batteryLabelProvider.getLabel(info, state),
            subtitle = batteryLabelProvider.getSubtitle(info, state),
            icon = batteryIconProvider.getIcon(info),
        )
    }
}