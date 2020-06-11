package personal.ivan.piccollagequiz.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import personal.ivan.piccollagequiz.binding_model.FontVhBindingModel
import personal.ivan.piccollagequiz.io.model.IoStatus
import personal.ivan.piccollagequiz.io.model.SingleLiveEvent
import personal.ivan.piccollagequiz.repository.GoogleFontRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: GoogleFontRepository) :
    ViewModel() {

    // IO Status
    val ioStatus = SingleLiveEvent<IoStatus>()

    // Google Font List
    val googleFontList: LiveData<List<FontVhBindingModel>> =
        repository.getGoogleFontList(ioStatus = ioStatus)
}