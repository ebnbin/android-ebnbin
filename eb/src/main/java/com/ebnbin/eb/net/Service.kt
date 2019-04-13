package com.ebnbin.eb.net

import io.reactivex.Observable
import retrofit2.http.GET

interface Service {
    @GET("ebnbin.json")
    fun ebnbin(): Observable<Ebnbin>
}
