package com.ebnbin.eb.util

import android.util.Base64
import java.io.File
import java.security.MessageDigest

object DataHelper {
    fun md5(string: String): String {
        return md5(string.toByteArray())
    }

    fun md5(file: File): String {
        return md5(file.readBytes())
    }

    private fun md5(byteArray: ByteArray): String {
        return MessageDigest.getInstance("MD5").digest(byteArray).joinToString("") {
            (it + 0x200).toString(16).substring(1)
        }
    }

    fun base64Encode(string: String): String {
        return Base64.encodeToString(string.toByteArray(), Base64.NO_WRAP)
    }

    fun base64Decode(string: String): String {
        return String(Base64.decode(string, Base64.NO_WRAP))
    }
}
