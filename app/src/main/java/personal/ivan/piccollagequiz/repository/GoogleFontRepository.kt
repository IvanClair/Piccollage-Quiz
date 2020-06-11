package personal.ivan.piccollagequiz.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import personal.ivan.piccollagequiz.io.model.GoogleFontApiRs
import personal.ivan.piccollagequiz.io.model.IoStatus
import personal.ivan.piccollagequiz.io.network.GoogleFontService
import personal.ivan.piccollagequiz.io.util.IoUtil
import javax.inject.Inject

class GoogleFontRepository @Inject constructor(
    context: Context,
    service: GoogleFontService
) {

    /**
     * Get Google font list
     */
    suspend fun getGoogleFontList(
        context: Context,
        ioStatus: MutableLiveData<IoStatus>
    ): LiveData<GoogleFontApiRs> =
        object : IoUtil<GoogleFontApiRs, GoogleFontApiRs>(ioStatus = ioStatus) {
            override suspend fun loadFromDb(): GoogleFontApiRs? = null

            override suspend fun loadFromNetwork(): GoogleFontApiRs? {
                TODO("Not yet implemented")
            }

            override suspend fun convertFromSource(source: GoogleFontApiRs?): GoogleFontApiRs? {
                TODO("Not yet implemented")
            }

            override suspend fun saveToDb(data: GoogleFontApiRs) {
                TODO("Not yet implemented")
            }

        }.getLiveData()
}