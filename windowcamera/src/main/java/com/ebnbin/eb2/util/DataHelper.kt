package com.ebnbin.eb2.util

import android.util.Base64
import java.io.File
import java.security.MessageDigest

object DataHelper {
    fun md5ToString(byteArray: ByteArray): String {
        return MessageDigest.getInstance("MD5").digest(byteArray).joinToString("") {
            (it + 0x200).toString(16).substring(1)
        }
    }

    fun md5ToString(string: String): String {
        return md5ToString(string.toByteArray(Charsets.UTF_8))
    }

    fun md5ToString(file: File): String {
        return md5ToString(file.readBytes())
    }

    fun base64EncodeToString(byteArray: ByteArray): String {
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    fun base64EncodeToString(string: String): String {
        return base64EncodeToString(string.toByteArray(Charsets.UTF_8))
    }

    fun base64DecodeToByteArray(string: String): ByteArray {
        return Base64.decode(string, Base64.NO_WRAP)
    }

    fun base64DecodeToString(string: String): String {
        return String(base64DecodeToByteArray(string))
    }
}
