package com.wstxda.toolkit.tiles.caffeine

import android.service.quicksettings.Tile
import com.wstxda.toolkit.activity.WriteSettingsPermissionActivity
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.caffeine.CaffeineModule
import com.wstxda.toolkit.manager.caffeine.CaffeineState
import com.wstxda.toolkit.ui.icon.CaffeineIconProvider
import com.wstxda.toolkit.ui.label.CaffeineLabelProvider
import kotlinx.coroutines.flow.Flow

class CaffeineTileService : BaseTileService() {

    private val caffeineManager by lazy { CaffeineModule.getInstance(applicationContext) }
    private val labelProvider by lazy { CaffeineLabelProvider(applicationContext) }
    private val iconProvider by lazy { CaffeineIconProvider(applicationContext) }

    override fun onStartListening() {
        caffeineManager.synchronizeState()
        super.onStartListening()
    }

    override fun onClick() {
        if (caffeineManager.isPermissionGranted()) {
            caffeineManager.cycleState()
            updateTile()
        } else {
            startActivityAndCollapse(WriteSettingsPermissionActivity::class.java)
        }
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        caffeineManager.currentState,
    )

    override fun updateTile() {
        val state = caffeineManager.currentState.value
        val hasPermission = caffeineManager.isPermissionGranted()

        setTileState(
            state = if (state != CaffeineState.Off && hasPermission) Tile.STATE_ACTIVE
            else Tile.STATE_INACTIVE,
            label = labelProvider.getLabel(state, hasPermission),
            subtitle = labelProvider.getSubtitle(state, hasPermission),
            icon = iconProvider.getIcon(state),
        )
    }
}