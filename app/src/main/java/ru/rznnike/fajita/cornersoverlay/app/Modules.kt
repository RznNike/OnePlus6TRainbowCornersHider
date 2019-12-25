import androidx.preference.PreferenceManager
import com.f2prateek.rx.preferences2.RxSharedPreferences
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.rznnike.fajita.cornersoverlay.app.global.event.EventDispatcher
import ru.rznnike.fajita.cornersoverlay.app.global.navigation.AppRouter
import ru.rznnike.fajita.cornersoverlay.app.global.notifier.Notifier
import ru.rznnike.fajita.cornersoverlay.app.global.ui.TopBottomWindowInsetsListener
import ru.rznnike.fajita.cornersoverlay.data.preference.Preferences
import ru.rznnike.fajita.cornersoverlay.domain.global.SchedulersProvider

private val appModule = module {
    factory { androidContext().resources }
    single { Notifier() }
    factory { TopBottomWindowInsetsListener() }
    single {
        object : SchedulersProvider {
            override fun ui(): Scheduler = AndroidSchedulers.mainThread()
            override fun computation(): Scheduler = Schedulers.computation()
            override fun trampoline(): Scheduler = Schedulers.trampoline()
            override fun newThread(): Scheduler = Schedulers.newThread()
            override fun io(): Scheduler = Schedulers.io()
        } as SchedulersProvider
    }
}

private val navigationModule = module {
    val cicerone: Cicerone<AppRouter> = Cicerone.create(AppRouter())
    single { cicerone.router as AppRouter }
    single { cicerone.navigatorHolder }
    single { EventDispatcher() }
}

private val preferenceModule = module {
    single { Preferences(get()) }
    factory { RxSharedPreferences.create(get()) }
    factory { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
}

val appComponent: List<Module> = listOf(
    appModule,
    navigationModule,
    preferenceModule
)