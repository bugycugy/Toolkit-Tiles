package com.wstxda.toolkit.tiles.sos

import android.service.quicksettings.Tile
import com.wstxda.toolkit.base.BaseForegroundActiveTileService
import com.wstxda.toolkit.manager.sos.SosModule
import com.wstxda.toolkit.ui.icon.SosIconProvider
import com.wstxda.toolkit.ui.label.SosLabelProvider
import kotlinx.coroutines.flow.Flow

class SosTileService : BaseForegroundActiveTileService() {

    private val sosManager by lazy { SosModule.getInstance(applicationContext) }
    private val labelProvider by lazy { SosLabelProvider(applicationContext) }
    private val iconProvider by lazy { SosIconProvider(applicationContext) }

    override fun isFeatureSupported(): Boolean = sosManager.hasFlashHardware()
    override fun isFeatureActive(): Boolean = sosManager.isActive.value
    override fun toggleFeature() {
        if (!sosManager.isFlashAvailable.value) return
        sosManager.toggle()
    }

    override fun stopFeature() = sosManager.cleanup()

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        sosManager.isActive,
        sosManager.isFlashAvailable,
    )

    override fun updateTile() {
        val isActive = sosManager.isActive.value
        val isHardwareAvailable = sosManager.hasFlashHardware()
        val isSystemAvailable = sosManager.isFlashAvailable.value
        val isFullyAvailable = isHardwareAvailable && isSystemAvailable

        setTileState(
            state = when {
                !isFullyAvailable -> Tile.STATE_UNAVAILABLE
                isActive -> Tile.STATE_ACTIVE
                else -> Tile.STATE_INACTIVE
            },
            label = labelProvider.getLabel(),
            subtitle = labelProvider.getSubtitle(isActive, isFullyAvailable),
            icon = iconProvider.getIcon(),
        )
    }
}