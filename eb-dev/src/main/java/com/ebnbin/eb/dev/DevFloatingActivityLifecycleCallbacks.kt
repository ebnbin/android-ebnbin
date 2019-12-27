package com.ebnbin.eb.dev

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.collection.ArrayMap

internal object DevFloatingActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    private val onAttachStateChangeListeners: ArrayMap<Activity, DevFloatingOnAttachStateChangeListener> = ArrayMap()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val onAttachStateChangeListener = DevFloatingOnAttachStateChangeListener(activity)
        onAttachStateChangeListeners[activity] = onAttachStateChangeListener
        activity.window.decorView.addOnAttachStateChangeListener(onAttachStateChangeListener)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        onAttachStateChangeListeners[activity]?.invalidatePopupWindow()
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        onAttachStateChangeListeners.remove(activity)
    }
}
