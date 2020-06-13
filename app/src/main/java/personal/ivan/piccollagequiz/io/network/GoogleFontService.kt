package personal.ivan.piccollagequiz.io.network

import io.reactivex.Observable
import personal.ivan.piccollagequiz.io.model.GoogleFontApiRs
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleFontService {

    @GET("webfonts")
    suspend fun getGoogleFontList(@Query("key") key: String): GoogleFontApiRs

    @GET("webfonts")
    fun getGoogleFontListRx(@Query("key") key: String): Observable<GoogleFontApiRs>
}