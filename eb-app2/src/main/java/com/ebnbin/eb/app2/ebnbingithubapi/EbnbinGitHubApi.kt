package com.ebnbin.eb.app2.ebnbingithubapi

import com.ebnbin.eb.app2.BuildConfig
import com.ebnbin.eb.base64EncodeToString
import com.ebnbin.eb.library.gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface EbnbinGitHubApi {
    @GET("$PREFIX/{repo}/contents/{path}")
    suspend fun getContentsFile(@Path("repo") repo: String, @Path("path") path: String): Content

    class Content {
        var content: String? = null
            private set
    }

    companion object {
        private const val PREFIX: String = "/repos/ebnbin"

        private val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor {
                        it.proceed(
                            it.request()
                                .newBuilder()
                                .addHeader(
                                    "Authorization",
                                    "Basic ${"${BuildConfig.GITHUB_API_TOKEN}:x-oauth-basic"
                                        .toByteArray()
                                        .base64EncodeToString()}"
                                )
                                .build()
                        )
                    }
                    .build()
            )
            .addConverterFactory(
                GsonConverterFactory.create(gson)
            )
            .build()

        val instance: EbnbinGitHubApi = retrofit.create(
            EbnbinGitHubApi::class.java)
    }
}
