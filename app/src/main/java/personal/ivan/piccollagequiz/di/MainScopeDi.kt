package personal.ivan.piccollagequiz.di

import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import personal.ivan.piccollagequiz.MainApplication
import personal.ivan.piccollagequiz.R
import personal.ivan.piccollagequiz.io.db.AppDb
import personal.ivan.piccollagequiz.io.db.GoogleFontDao
import personal.ivan.piccollagequiz.io.network.GoogleFontService
import personal.ivan.piccollagequiz.repository.GoogleFontRepository
import personal.ivan.piccollagequiz.util.DownloadableFontUtil
import personal.ivan.piccollagequiz.view.MainActivity
import personal.ivan.piccollagequiz.view_model.MainViewModel
import javax.inject.Scope

// region Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScope

// endregion

// region SubComponent

@Suppress("unused")
@Module
abstract class MainActivityModule {

    @MainScope
    @ContributesAndroidInjector(
        modules = [
            MainModule::class,
            MainViewModelModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity
}

// endregion

// region View Model

@Suppress("unused")
@Module
abstract class MainViewModelModule {

    @MainScope
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}

// endregion

// region Main Module

@Module
object MainModule {

    /**
     * Repository
     */
    @MainScope
    @Provides
    fun provideGoogleFontRepository(
        service: GoogleFontService,
        apiKey: String,
        dao: GoogleFontDao
    ): GoogleFontRepository = GoogleFontRepository(
        service = service,
        apiKey = apiKey,
        dao = dao
    )

    /**
     * API key for Google font API request
     */
    @MainScope
    @Provides
    fun provideGoogleFontApiKey(application: MainApplication): String =
        application.getString(R.string.api_key)

    /**
     * Google font DAO
     */
    @MainScope
    @Provides
    fun provideGoogleFontDao(db: AppDb): GoogleFontDao =
        db.googleFontDao()

    /**
     * Download font util
     */
    @MainScope
    @Provides
    fun provideDownloadableFontUtil(
        application: MainApplication,
        handler: Handler
    ): DownloadableFontUtil = DownloadableFontUtil(context = application, handler = handler)


    /**
     * Handler for download font
     */
    @MainScope
    @Provides
    fun provideDownloadFontHandler(): Handler =
        Handler(HandlerThread("fonts").apply { start() }.looper)

}

// endregion