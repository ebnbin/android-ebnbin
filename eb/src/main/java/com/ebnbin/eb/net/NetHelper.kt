package com.ebnbin.eb.net

import android.util.Base64
import com.ebnbin.eb.library.gson
import com.ebnbin.eb.util.ebApp
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetHelper {
    private const val TOKEN: String = "ab4eec4c17b45de7376b41fb7b393df27d727bfb"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/ebnbin/api/master/${ebApp.packageName}/")
        .client(OkHttpClient.Builder()
            .addInterceptor {
                it.proceed(it.request()
                    .newBuilder()
                    .addHeader("Authorization",
                        "Basic ${Base64.encodeToString("$TOKEN:x-oauth-basic".toByteArray(), Base64.NO_WRAP)}")
                    .build())
            }
            .build())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val ebService: EBService = retrofit.create(EBService::class.java)
}
