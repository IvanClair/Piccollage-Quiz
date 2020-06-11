package personal.ivan.piccollagequiz.view

import android.os.Bundle
import androidx.activity.viewModels
import dagger.android.support.DaggerAppCompatActivity
import personal.ivan.piccollagequiz.databinding.ActivityMainBinding
import personal.ivan.piccollagequiz.di.AppViewModelFactory
import personal.ivan.piccollagequiz.view_model.MainViewModel
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    // View Binding
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // View Model
    @Inject
    lateinit var factory: AppViewModelFactory
    private val viewModel by viewModels<MainViewModel> { factory }

    // region Life Cycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    // endregion
}