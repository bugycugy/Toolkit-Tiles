package com.wstxda.toolkit.tiles.counter

import android.service.quicksettings.Tile
import com.wstxda.toolkit.base.BaseTileService
import com.wstxda.toolkit.manager.counter.CounterModule
import com.wstxda.toolkit.ui.icon.CounterIconProvider
import com.wstxda.toolkit.ui.label.CounterLabelProvider
import kotlinx.coroutines.flow.Flow

class CounterResetTileService : BaseTileService() {

    private val counterManager by lazy { CounterModule.getInstance(applicationContext) }
    private val labelProvider by lazy { CounterLabelProvider(applicationContext) }
    private val iconProvider by lazy { CounterIconProvider(applicationContext) }

    override fun onClick() {
        counterManager.reset()
        updateTile()
    }

    override fun flowsToCollect(): List<Flow<*>> = listOf(
        counterManager.count,
    )

    override fun updateTile() {
        setTileState(
            state = Tile.STATE_INACTIVE,
            label = labelProvider.getResetLabel(),
            subtitle = labelProvider.getResetSubtitle(),
            icon = iconProvider.getResetIcon(),
        )
    }
}