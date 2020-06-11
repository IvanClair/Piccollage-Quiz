package personal.ivan.piccollagequiz.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import personal.ivan.piccollagequiz.MainApplication
import personal.ivan.piccollagequiz.R
import personal.ivan.piccollagequiz.io.network.GoogleFontService
import personal.ivan.piccollagequiz.repository.GoogleFontRepository
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
    @JvmStatic
    @MainScope
    @Provides
    fun provideGoogleFontRepository(
        service: GoogleFontService,
        apiKey: String
    ): GoogleFontRepository = GoogleFontRepository(service = service, apiKey = apiKey)

    /**
     * API key for Google font API request
     */
    @JvmStatic
    @MainScope
    @Provides
    fun provideGoogleFontApiKey(application: MainApplication): String =
        application.getString(R.string.api_key)
}

// endregion