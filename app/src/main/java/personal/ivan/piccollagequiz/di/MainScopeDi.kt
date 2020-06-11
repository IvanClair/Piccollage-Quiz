package personal.ivan.piccollagequiz.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import personal.ivan.piccollagequiz.MainApplication
import personal.ivan.piccollagequiz.io.network.GoogleFontService
import personal.ivan.piccollagequiz.repository.GoogleFontRepository
import personal.ivan.piccollagequiz.view.MainActivity
import personal.ivan.piccollagequiz.view_model.MainViewModel
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScope

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

@Module
object MainModule {

    /**
     * Repository
     */
    @JvmStatic
    @MainScope
    @Provides
    fun provideGoogleFontRepository(
        application: MainApplication,
        service: GoogleFontService
    ): GoogleFontRepository = GoogleFontRepository(context = application, service = service)
}

@Suppress("unused")
@Module
abstract class MainViewModelModule {

    @MainScope
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel
}