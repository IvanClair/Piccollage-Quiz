package personal.ivan.piccollagequiz.view_model

import android.graphics.Typeface
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import personal.ivan.piccollagequiz.binding_model.FontVhBindingModel
import personal.ivan.piccollagequiz.io.model.IoStatus
import personal.ivan.piccollagequiz.repository.GoogleFontRepository
import personal.ivan.piccollagequiz.util.DownloadableFontUtil
import javax.inject.Inject

class MainViewModel @Inject constructor(
    repository: GoogleFontRepository,
    private val util: DownloadableFontUtil
) : ViewModel() {

    // IO Status
    val ioStatus = MutableLiveData<IoStatus>()

    // Google Font List
    val googleFontList: LiveData<List<FontVhBindingModel>> =
        repository.getGoogleFontList(ioStatus = ioStatus)

    // Downloaded Typeface
    val downloadedTypeface = MutableLiveData<Typeface>()

    // region Download Font

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
            succeedCallback = { typeface ->
                typeface?.also { downloadedTypeface.value = it }
            })
    }

    // endregion
}