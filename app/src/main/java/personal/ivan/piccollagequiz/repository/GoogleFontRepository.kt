package personal.ivan.piccollagequiz.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import personal.ivan.piccollagequiz.binding_model.FontVhBindingModel
import personal.ivan.piccollagequiz.io.db.GoogleFontDao
import personal.ivan.piccollagequiz.io.model.GoogleFontDetails
import personal.ivan.piccollagequiz.io.model.IoStatus
import personal.ivan.piccollagequiz.io.network.GoogleFontService
import personal.ivan.piccollagequiz.io.util.IoUtil
import javax.inject.Inject

class GoogleFontRepository @Inject constructor(
    private val service: GoogleFontService,
    private val apiKey: String,
    private val dao: GoogleFontDao
) {

    /**
     * Get Google font list
     */
    fun getGoogleFontList(ioStatus: MutableLiveData<IoStatus>): LiveData<List<FontVhBindingModel>> {

        // processing origin data
        suspend fun processing(dataList: List<GoogleFontDetails>?): List<GoogleFontDetails>? =
            dataList?.apply {
                withContext(Dispatchers.IO) {
                    dataList.forEachIndexed { index, googleFontDetails ->
                        googleFontDetails.createPrimaryKey(index = index)
                    }
                }
            }

        // convert to binding model list
        suspend fun convert(dataList: List<GoogleFontDetails>?): List<FontVhBindingModel> =
            mutableListOf<FontVhBindingModel>().apply {
                withContext(Dispatchers.IO) {
                    dataList?.forEach { fontDetails ->
                        fontDetails.variantList?.forEach {
                            add(FontVhBindingModel(data = fontDetails, variant = it))
                        }
                    }
                }
            }

        return object :
            IoUtil<List<GoogleFontDetails>, List<FontVhBindingModel>>(ioStatus = ioStatus) {

            override suspend fun loadFromDb(): List<GoogleFontDetails>? =
                dao.loadAll().let { if (it.isNotEmpty()) it else null }

            override suspend fun loadFromNetwork(): List<GoogleFontDetails>? =
                service.getGoogleFontList(key = apiKey).fontList

            override suspend fun processingSource(source: List<GoogleFontDetails>?): List<GoogleFontDetails>? =
                processing(dataList = source)

            override suspend fun convertToResult(source: List<GoogleFontDetails>?): List<FontVhBindingModel>? =
                convert(dataList = source)

            override suspend fun saveToDb(data: List<GoogleFontDetails>) {
                dao.insertAll(dataList = data)
            }


        }.getLiveData()
    }
}