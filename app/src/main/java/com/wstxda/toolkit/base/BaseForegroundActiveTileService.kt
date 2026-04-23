package com.wstxda.toolkit.base

import android.app.NotificationManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.CallSuper
import com.wstxda.toolkit.R
import com.wstxda.toolkit.services.foreground.channel
import com.wstxda.toolkit.services.foreground.notification
import com.wstxda.toolkit.services.foreground.startForegroundCompat

abstract class BaseForegroundActiveTileService : BaseTileService() {

    private val notificationId: Int by lazy { javaClass.name.hashCode() }

    private val canStartForegroundFromLifecycle =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE

    protected abstract fun isFeatureActive(): Boolean
    protected abstract fun isFeatureSupported(): Boolean
    protected abstract fun toggleFeature()
    protected abstract fun stopFeature()

    protected open fun onFeatureNotSupported() {
        Toast.makeText(this, R.string.not_supported, Toast.LENGTH_SHORT).show()
    }

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        getSystemService(NotificationManager::class.java)?.createNotificationChannel(channel())
    }

    @CallSuper
    override fun onStartListening() {
        if (isFeatureActive() && canStartForegroundFromLifecycle) {
            startForegroundSafely()
        }
        super.onStartListening()
    }

    @CallSuper
    override fun onDestroy() {
        stopFeature()
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }

    @CallSuper
    override fun onTileRemoved() {
        stopFeature()
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onTileRemoved()
    }

    final override fun onClick() {
        if (!isFeatureSupported()) {
            onFeatureNotSupported()
            return
        }
        toggleFeature()
        if (isFeatureActive()) {
            startForegroundSafely()
        } else {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
        updateTile()
    }

    private fun startForegroundSafely() {
        try {
            startForegroundCompat(notificationId, notification())
        } catch (e: Exception) {
            when {
                e is SecurityException -> stopFeature()
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && e is IllegalStateException -> stopFeature()

                else -> throw e
            }
        }
    }
}
