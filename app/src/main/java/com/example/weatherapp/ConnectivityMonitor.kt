import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class ConnectivityMonitor(context: Context, private val listener: ConnectivityMonitorListener) {
    private val connectivityManager: ConnectivityManager?
    private val networkCallback: NetworkCallback

    init {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = NetworkCallbackImpl()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        if (connectivityManager != null) {
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
    }

    fun unregister() {
        connectivityManager!!.unregisterNetworkCallback(networkCallback)
    }

    private inner class NetworkCallbackImpl : NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            listener.onNetworkConnectionChanged(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            listener.onNetworkConnectionChanged(false)
        }
    }

    interface ConnectivityMonitorListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }
}