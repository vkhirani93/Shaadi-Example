package shaadi.example.vrajesh.hirani.ui

import android.app.Application
import shaadi.example.vrajesh.hirani.util.ConnectionMonitor

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ConnectionMonitor(this).startNetworkCallback()
    }

    override fun onTerminate() {
        super.onTerminate()

        ConnectionMonitor(this).stopNetworkCallback()
    }
}