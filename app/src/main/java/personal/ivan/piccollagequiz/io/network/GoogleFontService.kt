package personal.ivan.piccollagequiz.io.network

import personal.ivan.piccollagequiz.io.model.GoogleFontApiRs
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleFontService {

    @GET("webfonts")
    suspend fun getGoogleFontList(@Query("key") key: String): GoogleFontApiRs
}