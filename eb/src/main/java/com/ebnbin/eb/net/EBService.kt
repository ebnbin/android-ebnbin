package com.ebnbin.eb.net

import com.ebnbin.eb.net.model.eb.Update
import io.reactivex.Observable
import retrofit2.http.GET

interface EBService {
    @GET("${PREFIX}update.json")
    fun update(): Observable<Update>

    companion object {
        private const val PREFIX: String = "eb/"
    }
}
