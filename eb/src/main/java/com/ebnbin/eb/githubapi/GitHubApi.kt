package com.ebnbin.eb.githubapi

import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.library.Libraries
import com.ebnbin.eb.util.DataHelper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object GitHubApi {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .client(OkHttpClient.Builder()
            .addInterceptor {
                it.proceed(it.request()
                    .newBuilder()
                    .addHeader("Authorization",
                        "Basic ${DataHelper.base64EncodeToString("${BuildConfig.GITHUB_API_TOKEN}:x-oauth-basic")}")
                    .build())
            }
            .build())
        .addConverterFactory(GsonConverterFactory.create(Libraries.gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    val api: GitHubApiService = retrofit.create(GitHubApiService::class.java)
}
