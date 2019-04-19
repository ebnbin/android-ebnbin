package com.ebnbin.eb.util

import android.view.OrientationEventListener
import android.view.Surface

/**
 * 监听屏幕旋转 180 度.
 */
object RotationDetector {
    private var rotation: Int = Surface.ROTATION_0

    private val orientationEventListener: OrientationEventListener = object : OrientationEventListener(ebApp) {
        override fun onOrientationChanged(orientation: Int) {
            if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) return
            val oldRotation = rotation
            rotation = WindowHelper.displayRotation
            if (oldRotation == rotation) return
            listeners.forEach {
                it.onRotationChanged(oldRotation, rotation)
            }
        }
    }

    fun register(listener: Listener) {
        if (listeners.isEmpty()) {
            orientationEventListener.enable()
        }
        listeners.add(listener)
    }

    fun unregister(listener: Listener) {
        listeners.remove(listener)
        if (listeners.isEmpty()) {
            orientationEventListener.disable()
            rotation = Surface.ROTATION_0
        }
    }

    private val listeners: LinkedHashSet<Listener> = LinkedHashSet()

    interface Listener {
        fun onRotationChanged(oldRotation: Int, newRotation: Int)
    }
}
