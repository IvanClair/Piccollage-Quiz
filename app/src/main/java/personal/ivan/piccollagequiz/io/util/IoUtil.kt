package personal.ivan.piccollagequiz.io.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import personal.ivan.piccollagequiz.io.model.IoStatus

/**
 * @param S origin data (source from network or database)
 * @param R converted data (result of output)
 */
abstract class IoUtil<S, R>(private val ioStatus: MutableLiveData<IoStatus>) {

    // Result LiveData
    private val resultLiveData = liveData<R> {

        // start with loading status
        ioStatus.value = IoStatus.LOADING

        // TODO Retrofit throws exception if network is unavailable,
        //     maybe they will update in future, keep an eye on new release
        try {

            // load from database
            val dataFromDb = loadFromDb()
            if (dataFromDb != null) {
                convertToNeeded(source = dataFromDb)
                    ?.also {
                        emit(it)
                        ioStatus.value = IoStatus.SUCCESS
                    }
            }

            // load from network
            val dataFromNetwork = loadFromNetwork()
            val afterProcessing = processingSource(source = dataFromNetwork)

            // convert API response to actual needed model
            val convertedData = convertToNeeded(source = afterProcessing)
            when {
                // load from network succeed
                convertedData != null -> {
                    afterProcessing?.also { saveToDb(data = it) }
                    ioStatus.value = IoStatus.SUCCESS
                    emit(convertedData)
                }

                // load from network failed, and did not save in database before
                dataFromDb == null -> ioStatus.value = IoStatus.FAIL
            }
        } catch (e: Exception) {
            // set fail when exception happened
            ioStatus.value = IoStatus.FAIL
        }
    }

    // region Public Function

    fun getLiveData() = resultLiveData

    // endregion

    // region Override Functions

    protected abstract suspend fun loadFromDb(): S?

    protected abstract suspend fun loadFromNetwork(): S?

    protected abstract suspend fun processingSource(source: S?): S?

    protected abstract suspend fun convertToNeeded(source: S?): R?

    protected abstract suspend fun saveToDb(data: S)

    // end region
}