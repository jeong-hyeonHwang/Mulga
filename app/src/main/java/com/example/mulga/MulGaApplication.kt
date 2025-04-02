package com.ilm.mulga

import android.app.Application
import com.ilm.mulga.di.appModule
import com.ilm.mulga.domain.usecase.NotificationPublisherUseCase
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MulGaApplication : Application() {
    private val publisherUseCase: NotificationPublisherUseCase by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MulGaApplication)
            modules(appModule)
        }

        // 앱이 포그라운드에 진입할 때마다 메시지 큐로 이동
//        ProcessLifecycleOwner.get().lifecycle.addObserver(
//            LifecycleEventObserver { _, event ->
//                if (event == Lifecycle.Event.ON_START) {
//                    Log.d("📲AppLifecycle", "App moved to foreground")
//                    CoroutineScope(Dispatchers.IO).launch {
//                        if (publisherUseCase.connect()) {
//                            publisherUseCase.publishAllNotifications()
//                        }
//                    }
//                }
//            }
//        )
    }
}