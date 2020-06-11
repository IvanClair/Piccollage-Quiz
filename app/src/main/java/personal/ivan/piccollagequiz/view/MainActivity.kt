package personal.ivan.piccollagequiz.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerAppCompatActivity
import personal.ivan.piccollagequiz.R
import personal.ivan.piccollagequiz.binding_model.FontVhBindingModel
import personal.ivan.piccollagequiz.databinding.ActivityMainBinding
import personal.ivan.piccollagequiz.di.AppViewModelFactory
import personal.ivan.piccollagequiz.io.model.IoStatus
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
        initRecyclerView()
        observeLiveData()
    }

    // endregion

    // region Observe LiveData

    private fun observeLiveData() {
        viewModel.apply {

            // IO status
            ioStatus.observe(
                this@MainActivity,
                Observer {
                    updateProgressBar(enable = it == IoStatus.LOADING)
                    if (it == IoStatus.FAIL) {
                        showFailAlert()
                    }
                })

            // data list
            googleFontList.observe(
                this@MainActivity,
                Observer { updateRecyclerView(dataList = it) }
            )
        }
    }

    // endregion

    // region Progress Bar

    private fun updateProgressBar(enable: Boolean) {
        binding.progressBar.visibility = if (enable) View.VISIBLE else View.GONE
    }

    // endregion

    // region Alert

    private fun showFailAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.alert_title)
            .setMessage(R.string.alert_message)
            .setPositiveButton(R.string.label_ok, null)
            .show()
    }

    // endregion

    // region Recycler View

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = MainAdapter(listener = View.OnClickListener {
                // todo
            })
        }
    }

    private fun updateRecyclerView(dataList: List<FontVhBindingModel>) {
        (binding.recyclerView.adapter as? MainAdapter)?.submitList(dataList)
    }

    // endregion
}