package com.ebnbin.eb.dev.floating

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.collection.ArrayMap
import com.ebnbin.eb.EBApplication

internal object DevFloatingActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    var isFloatingVisible: Boolean = true
        set(value) {
            if (field == value) return
            field = value
            val currentActivity = currentActivity ?: return
            onAttachStateChangeListeners[currentActivity]?.invalidatePopupWindow()
        }

    private var currentActivity: Activity? = null

    private val onAttachStateChangeListeners: ArrayMap<Activity, DevFloatingOnAttachStateChangeListener> = ArrayMap()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (EBApplication.instance.devFloatingExceptActivityClassNames.contains(activity::class.java.name)) return
        val onAttachStateChangeListener = DevFloatingOnAttachStateChangeListener(activity)
        onAttachStateChangeListeners[activity] = onAttachStateChangeListener
        activity.window.decorView.addOnAttachStateChangeListener(onAttachStateChangeListener)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        if (EBApplication.instance.devFloatingExceptActivityClassNames.contains(activity::class.java.name)) return
        currentActivity = activity
        onAttachStateChangeListeners[activity]?.invalidatePopupWindow()
    }

    override fun onActivityPaused(activity: Activity) {
        if (EBApplication.instance.devFloatingExceptActivityClassNames.contains(activity::class.java.name)) return
        currentActivity = null
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (EBApplication.instance.devFloatingExceptActivityClassNames.contains(activity::class.java.name)) return
        onAttachStateChangeListeners.remove(activity)
    }
}
