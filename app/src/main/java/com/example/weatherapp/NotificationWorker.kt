import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weatherapp.R


class TemperatureWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        exibirNotificacao()
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun exibirNotificacao() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        val channelId = "weatherTemp"
        val channelName = "Temperatura"
        val notificationId = 1

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }


        var builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.humidity_icon)
            .setContentTitle("A temperatura atual é de:")
            .setContentText("Mostrar a nova temperatura ")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(notificationId, builder.build())
    }
}
