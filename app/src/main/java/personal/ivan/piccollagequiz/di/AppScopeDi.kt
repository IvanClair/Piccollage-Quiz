package personal.ivan.piccollagequiz.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import dagger.*
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import personal.ivan.piccollagequiz.BuildConfig
import personal.ivan.piccollagequiz.MainApplication
import personal.ivan.piccollagequiz.R
import personal.ivan.piccollagequiz.io.db.AppDb
import personal.ivan.piccollagequiz.io.network.GoogleFontService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton
import kotlin.reflect.KClass

// region Component

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ViewModelFactoryModule::class,
        RetrofitModule::class,
        DbModule::class,
        MainActivityModule::class
    ]
)
interface AppComponent : AndroidInjector<MainApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: MainApplication): AppComponent
    }
}

// endregion

// region View Model

@Suppress("unused")
@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}

@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

// endregion

// region Retrofit Module

@Module
object RetrofitModule {

    /**
     * Create Retrofit for API call
     */
    @Singleton
    @Provides
    fun provideGoogleFontService(
        application: MainApplication,
        okHttpClient: OkHttpClient
    ): GoogleFontService =
        Retrofit.Builder()
            .baseUrl(application.getString(R.string.base_url_google_font))
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(GoogleFontService::class.java)

    /**
     * Setting HTTP configs
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .apply {
                // log on debug mode
                if (BuildConfig.DEBUG) {
                    addInterceptor(interceptor)
                }
            }
            .build()

    /**
     * Choose to log HTTP detailed information
     */
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
}

// endregion

// region Database

@Module
object DbModule {

    @Singleton
    @Provides
    fun provideAppDb(application: MainApplication) =
        Room
            .databaseBuilder(
                application,
                AppDb::class.java,
                application.packageName
            )
            .fallbackToDestructiveMigration()
            .build()
}

// endregion