package com.ebnbin.eb

import android.util.Base64
import java.security.MessageDigest

/**
 * [ByteArray] 数据计算 md5 返回十六进制 32 位长度字符串.
 */
fun ByteArray.md5ToString(): String {
    return MessageDigest.getInstance("MD5").digest(this).joinToString("") {
        (it + 0x200).toString(16).substring(1)
    }
}

/**
 * [ByteArray] 数据编码为 base64 字符串.
 */
fun ByteArray.base64EncodeToString(): String {
    return Base64.encodeToString(this, Base64.NO_WRAP)
}

/**
 * Base64 字符串解码为 [ByteArray] 数据.
 */
fun String.base64Decode(): ByteArray {
    return Base64.decode(this.toByteArray(), Base64.NO_WRAP)
}
