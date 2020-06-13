package personal.ivan.piccollagequiz.view_model

import android.graphics.Typeface
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import personal.ivan.piccollagequiz.binding_model.FontVhBindingModel
import personal.ivan.piccollagequiz.io.model.IoStatus
import personal.ivan.piccollagequiz.repository.GoogleFontRepository
import personal.ivan.piccollagequiz.util.DownloadableFontUtil
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: GoogleFontRepository,
    private val util: DownloadableFontUtil
) : ViewModel() {

    // IO Status
    val ioStatus = MutableLiveData<IoStatus>()

    // region Google Font List

    // Google Font List
//    val googleFontList: LiveData<List<FontVhBindingModel>> =
//        repository.getGoogleFontList(ioStatus = ioStatus)

    /**
     * Get google font list by Rx
     */
    fun getGoogleFontListRx(): Observable<List<FontVhBindingModel>> =
        repository.getGoogleFontListRx()

    // endregion

    // region Download Font

    // Downloaded Typeface
    val downloadedTypeface = MutableLiveData<Typeface>()

    /**
     * Start download font from [DownloadableFontUtil]
     *
     * @param model the model selected by user
     */
    fun downloadFont(model: FontVhBindingModel) {
        util.start(
            fontFamily = model.fontFamily,
            weight = model.weight,
            italic = model.italic,
            ioStatus = ioStatus,
            downloadedTypeface = downloadedTypeface
        )
    }

    // endregion

    // region Rx version

    /**
     * Start download font from [DownloadableFontUtil], Rx version
     *
     * @param model the model selected by user
     */
    fun downloadFontRx(model: FontVhBindingModel): Single<Typeface> =
        util.startRx(
            fontFamily = model.fontFamily,
            weight = model.weight,
            italic = model.italic
        )

    // endregion
}