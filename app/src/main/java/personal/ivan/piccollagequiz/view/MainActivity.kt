package personal.ivan.piccollagequiz.view

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import personal.ivan.piccollagequiz.R
import personal.ivan.piccollagequiz.binding_model.FontVhBindingModel
import personal.ivan.piccollagequiz.databinding.ActivityMainBinding
import personal.ivan.piccollagequiz.di.AppViewModelFactory
import personal.ivan.piccollagequiz.io.model.IoStatus
import personal.ivan.piccollagequiz.util.getTypeface
import personal.ivan.piccollagequiz.view_model.MainViewModel
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    // View Binding
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // View Model
    @Inject
    lateinit var factory: AppViewModelFactory
    private val viewModel by viewModels<MainViewModel> { factory }

    // region Rx Observer

    private val googleFontApiObserver =
        object : io.reactivex.Observer<List<FontVhBindingModel>> {
            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                updateProgressBar(enable = true)
            }

            override fun onNext(t: List<FontVhBindingModel>) {
                updateRecyclerView(dataList = t)
                updateProgressBar(enable = false)
            }

            override fun onError(e: Throwable) {
                updateProgressBar(enable = false)
                showFailAlert()
            }
        }

    private val downloadedTypefaceObserver =
        object : io.reactivex.SingleObserver<Typeface> {
            override fun onSuccess(t: Typeface) {
                updateDemoTextViewTypeface(typeface = t)
                updateProgressBar(enable = false)
            }

            override fun onSubscribe(d: Disposable) {
                updateProgressBar(enable = true)
            }

            override fun onError(e: Throwable) {
                updateProgressBar(enable = false)
                showFailAlert()
            }
        }

    // endregion

    // region Life Cycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initRecyclerView()
        observeLiveData()

        // get api
        viewModel
            .getGoogleFontListRx()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(googleFontApiObserver)
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
//            googleFontList.observe(
//                this@MainActivity,
//                Observer { updateRecyclerView(dataList = it) }
//            )

            // downloaded font
            downloadedTypeface.observe(
                this@MainActivity,
                Observer { updateDemoTextViewTypeface(typeface = it) }
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
            adapter = MainAdapter(listener = View.OnClickListener { view ->
                (view.tag as? FontVhBindingModel)?.also { model ->
                    // preview the font
                    model.fontFamily
                        .getTypeface(context = this@MainActivity)
                        ?.also { updateDemoTextViewTypeface(typeface = it) }

                    // download the font
//                    viewModel.downloadFont(model = model)
                    viewModel.downloadFontRx(model = model).subscribe(downloadedTypefaceObserver)
                }
            })
        }
    }

    private fun updateRecyclerView(dataList: List<FontVhBindingModel>) {
        (binding.recyclerView.adapter as? MainAdapter)?.submitList(dataList)
    }

    // endregion

    // region Demo Text

    private fun updateDemoTextViewTypeface(typeface: Typeface) {
        binding.textViewDemo.typeface = typeface
    }

    // endregion
}