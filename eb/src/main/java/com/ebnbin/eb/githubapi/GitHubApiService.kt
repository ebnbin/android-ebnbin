package com.ebnbin.eb.githubapi

import com.ebnbin.eb.githubapi.model.Content
import com.ebnbin.eb.githubapi.model.PutContentsRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface GitHubApiService {
    @GET("$PREFIX/{path}")
    fun getContentsFile(@Path("path") path: String): Observable<Content>

    @GET("$PREFIX/{path}")
    suspend fun coroutinesGetContentsFile(@Path("path") path: String): Content

    @GET("$PREFIX/{path}")
    fun getContentsDirectory(@Path("path") path: String): Observable<List<Content>>

    @PUT("$PREFIX/{path}")
    fun putContents(@Path("path") path: String, @Body request: PutContentsRequest): Observable<Unit>

    companion object {
        private const val PREFIX = "/repos/ebnbin/api-android-ebnbin/contents"
    }
}
