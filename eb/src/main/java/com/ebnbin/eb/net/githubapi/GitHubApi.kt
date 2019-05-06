package com.ebnbin.eb.net.githubapi

import com.ebnbin.eb.BuildConfig
import com.ebnbin.eb.library.gson
import com.ebnbin.eb.net.githubapi.model.Content
import com.ebnbin.eb.net.githubapi.model.PutContents
import com.ebnbin.eb.net.githubapi.model.PutContentsRequest
import com.ebnbin.eb.util.AppHelper
import com.ebnbin.eb.util.BuildHelper
import com.ebnbin.eb.util.ebApp
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object GitHubApi {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(OkHttpClient.Builder()
            .addInterceptor {
                it.proceed(it.request()
                    .newBuilder()
                    .addHeader("Authorization",
                        "Basic ${AppHelper.base64Encode("${BuildConfig.GITHUB_API_TOKEN}:x-oauth-basic")}")
                    .build())
            }
            .build())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    private val api: GitHubApiService = retrofit.create(GitHubApiService::class.java)

    fun getContentsFile(path: String): Observable<Content> {
        return api.getContentsFile("${ebApp.packageName}$path")
    }

    fun getContentsDirectory(path: String): Observable<List<Content>> {
        return api.getContentsDirectory("${ebApp.packageName}$path")
    }

    fun <T> putContents(path: String, json: T, oldContent: Content? = null): Observable<PutContents> {
        val putContentsRequest = PutContentsRequest()
        putContentsRequest.message = "${ebApp.packageName} ${BuildHelper.versionCode}"
        putContentsRequest.content = AppHelper.base64Encode(gson.toJson(json))
        putContentsRequest.sha = oldContent?.sha
        return api.putContents("${ebApp.packageName}$path", putContentsRequest)
    }
}
