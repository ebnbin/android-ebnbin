package com.ebnbin.eb.util

import android.util.Base64
import java.security.MessageDigest

fun ByteArray.toMD5String(): String {
    return MessageDigest.getInstance("MD5").digest(this).joinToString("") {
        (it + 0x200).toString(16).substring(1)
    }
}

fun ByteArray.base64EncodeToString(): String {
    return Base64.encodeToString(this, Base64.NO_WRAP)
}

fun ByteArray.base64DecodeToString(): String {
    return String(Base64.decode(this, Base64.NO_WRAP))
}
