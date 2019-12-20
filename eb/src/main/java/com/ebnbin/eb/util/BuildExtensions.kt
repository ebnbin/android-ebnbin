package com.ebnbin.eb.util

import android.os.Build
import com.ebnbin.eb.EBApp

private fun sdk(buildVersionCode: Int): Boolean {
    return Build.VERSION.SDK_INT >= buildVersionCode
}

fun sdk24N(): Boolean {
    return sdk(Build.VERSION_CODES.N)
}

fun sdk25N1(): Boolean {
    return sdk(Build.VERSION_CODES.N_MR1)
}

fun sdk26O(): Boolean {
    return sdk(Build.VERSION_CODES.O)
}

fun sdk27O1(): Boolean {
    return sdk(Build.VERSION_CODES.O_MR1)
}

fun sdk28P(): Boolean {
    return sdk(Build.VERSION_CODES.P)
}

fun sdk29Q(): Boolean {
    return sdk(Build.VERSION_CODES.Q)
}

//*********************************************************************************************************************

val applicationId: String
    get() = EBApp.instance.packageName
