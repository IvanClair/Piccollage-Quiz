package personal.ivan.piccollagequiz.view_model

import androidx.lifecycle.ViewModel
import personal.ivan.piccollagequiz.repository.GoogleFontRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: GoogleFontRepository) :
    ViewModel() {

}