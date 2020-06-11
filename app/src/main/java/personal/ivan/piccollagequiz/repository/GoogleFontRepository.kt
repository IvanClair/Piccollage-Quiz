package personal.ivan.piccollagequiz.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import personal.ivan.piccollagequiz.binding_model.FontVhBindingModel
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
    fun getGoogleFontList(ioStatus: MutableLiveData<IoStatus>): LiveData<List<FontVhBindingModel>> {

        // convert to binding model list
        suspend fun convert(data: GoogleFontApiRs?): List<FontVhBindingModel> =
            mutableListOf<FontVhBindingModel>().apply {
                withContext(Dispatchers.IO) {
                    data?.fontList?.forEach { fontDetails ->
                        fontDetails.variantList?.forEachIndexed { index, _ ->
                            add(FontVhBindingModel(data = fontDetails, variantIndex = index))
                        }
                    }
                }
            }

        return object : IoUtil<GoogleFontApiRs, List<FontVhBindingModel>>(ioStatus = ioStatus) {
            override suspend fun loadFromDb(): GoogleFontApiRs? = null

            override suspend fun loadFromNetwork(): GoogleFontApiRs? =
                service.getGoogleFontList(key = apiKey)

            override suspend fun convertFromSource(source: GoogleFontApiRs?): List<FontVhBindingModel>? =
                convert(data = source)

            override suspend fun saveToDb(data: GoogleFontApiRs) {
            }

        }.getLiveData()
    }
}