package personal.ivan.piccollagequiz.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
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

    // region Live Data + Coroutine
    /**
     * Get Google font list
     */
    fun getGoogleFontList(ioStatus: MutableLiveData<IoStatus>): LiveData<List<FontVhBindingModel>> {

        // processing origin data
        suspend fun processing(dataList: List<GoogleFontDetails>?): List<GoogleFontDetails>? =
            dataList?.apply {
                withContext(Dispatchers.IO) {
                    forEachIndexed { index, googleFontDetails ->
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

    // endregion

    // region Rx

    /**
     * Get Google font list Rx version
     *
     * @return results from database, then request from network to update database
     */
    fun getGoogleFontListRx(): Observable<List<FontVhBindingModel>> {
        return Observable.concatArray(
            fetchFromDb(),
            fetchFromNetwork()
        )
    }

    /**
     * Fetch results from database
     */
    private fun fetchFromDb(): Observable<List<FontVhBindingModel>> =
        dao.loadAllRx()
            .subscribeOn(Schedulers.io())
            // convert
            .map {
                mutableListOf<FontVhBindingModel>()
                    .apply {
                        it.forEach { fontDetails ->
                            fontDetails.variantList?.forEach {
                                add(FontVhBindingModel(data = fontDetails, variant = it))
                            }
                        }
                    }
                    .toList()
            }
            .toObservable()

    /**
     * Fetch results from network, and save to database
     */
    private fun fetchFromNetwork(): Observable<List<FontVhBindingModel>> =
        service
            .getGoogleFontListRx(key = apiKey)
            .subscribeOn(Schedulers.io())
            .map {
                it.fontList?.apply {
                    forEachIndexed { index, googleFontDetails ->
                        googleFontDetails.createPrimaryKey(index = index)
                    }
                }
            }
            .doOnNext { dataList ->
                // save to database
                dataList?.also { dao.insertAllRx(dataList = dataList) }
            }
            .map {
                mutableListOf<FontVhBindingModel>().apply {
                    it.forEach { fontDetails ->
                        fontDetails.variantList?.forEach {
                            add(FontVhBindingModel(data = fontDetails, variant = it))
                        }
                    }
                }
            }

    // endregion
}