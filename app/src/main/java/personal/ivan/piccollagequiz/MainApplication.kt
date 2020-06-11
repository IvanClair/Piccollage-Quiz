package personal.ivan.piccollagequiz

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import personal.ivan.piccollagequiz.di.DaggerAppComponent

class MainApplication : DaggerApplication() {

    // region Dagger

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.factory().create(application = this)

    // endregion
}