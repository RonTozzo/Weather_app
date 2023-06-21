import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherapp.R

class TemperatureWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        // Simulando uma chamada assíncrona para obter a temperatura
        val temperature = fetchTemperatureFromApi()

        exibirNotificacao()

        return Result.success()
    }

    private suspend fun fetchTemperatureFromApi(): Double {
        return 25.5
    }

    private fun exibirNotificacao() {
        val channelId = "meuCanal"
        val channelName = "Meu Canal"
        val notificationId = 1

        val notificationManager = NotificationManagerCompat.from(applicationContext)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, channelId).setSmallIcon(
                R.drawable.humidity_icon
            )
                .setContentTitle("Título da notificação")
                .setContentText("Conteúdo da notificação")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("showNotifi", "showNotifi")
            notificationManager.notify(notificationId, notificationBuilder.build())

        } else {
            Log.e("noShowNotifi", "noShowNotifi")
        }
    }
}
