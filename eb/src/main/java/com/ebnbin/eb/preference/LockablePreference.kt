package com.ebnbin.eb.preference

interface LockablePreference {
    fun getLockDelegate(): PreferenceLockDelegate
}
