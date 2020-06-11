package personal.ivan.piccollagequiz.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import personal.ivan.piccollagequiz.io.model.GoogleFontApiRs
import personal.ivan.piccollagequiz.io.model.IoStatus
import personal.ivan.piccollagequiz.io.network.GoogleFontService
import personal.ivan.piccollagequiz.io.util.IoUtil
import javax.inject.Inject

class GoogleFontRepository @Inject constructor(
    private val service: GoogleFontService,
    private val apiKey: String
) {

    /**
     * Get Google font list
     */
    fun getGoogleFontList(ioStatus: MutableLiveData<IoStatus>): LiveData<GoogleFontApiRs> =
        object : IoUtil<GoogleFontApiRs, GoogleFontApiRs>(ioStatus = ioStatus) {
            override suspend fun loadFromDb(): GoogleFontApiRs? = null

            override suspend fun loadFromNetwork(): GoogleFontApiRs? =
                service.getGoogleFontList(key = apiKey)

            override suspend fun convertFromSource(source: GoogleFontApiRs?): GoogleFontApiRs? =
                source

            override suspend fun saveToDb(data: GoogleFontApiRs) {
            }

        }.getLiveData()
}