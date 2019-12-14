package com.ebnbin.eb2.preference

interface LockablePreference {
    fun getLockDelegate(): PreferenceLockDelegate
}
