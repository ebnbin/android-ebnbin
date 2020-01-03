package com.ebnbin.ebapp.api

import android.os.Parcelable
import com.ebnbin.eb.base64EncodeToString
import com.ebnbin.eb.library.gson
import com.ebnbin.ebapp.BuildConfig
import kotlinx.android.parcel.Parcelize
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubApi {
    @GET("$PREFIX/{repo}/contents/{path}")
    suspend fun getContentsFile(
        @Path("repo") repo: String,
        @Path("path") path: String,
        @Query("ref") ref: String
    ): Content

    @Parcelize
    class Content(
        var content: String? = null
    ) : Parcelable

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

        val instance: GitHubApi = retrofit.create(GitHubApi::class.java)
    }
}
