package com.example.weatherapp

import ConnectivityMonitor
import TemperatureWorker
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), ConnectivityMonitor.ConnectivityMonitorListener {

    private lateinit var connectivityMonitor: ConnectivityMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        connectivityMonitor = ConnectivityMonitor(this, this)

        val workRequest = PeriodicWorkRequestBuilder<TemperatureWorker>(
            1, TimeUnit.HOURS
        ).build()

        // Enviar o WorkRequest para o WorkManager
        WorkManager.getInstance(applicationContext).enqueue(workRequest)


        setContentView(R.layout.activity_main)
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityMonitor.unregister()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        Log.e("connection", "isConnected: $isConnected")

        runOnUiThread {
            if (!isConnected) {
                AlertDialog.Builder(this)
                    .setTitle("Oops!")
                    .setMessage("Você perdeu sua conexão com a internet.")
                    .setPositiveButton("Ok") { _, _ -> }
                    .show()
            }
        }

    }
}


