package personal.ivan.piccollagequiz.view_model

import android.content.Context
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

    fun aaa(context: Context) {
//        val request = FontRequest(
//            "com.google.android.gms.fonts",
//            "com.google.android.gms",
//            "my font",
//            0
//        )
//        val callback = object : FontsContractCompat.FontRequestCallback() {
//
//            override fun onTypefaceRetrieved(typeface: Typeface) {
//            }
//
//            override fun onTypefaceRequestFailed(reason: Int) {
//            }
//        }
//        FontsContractCompat.requestFont(context, request, )
    }
}