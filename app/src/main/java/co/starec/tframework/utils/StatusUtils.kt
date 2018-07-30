package co.starec.tframework.utils

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.location.LocationManager
import android.net.ConnectivityManager

object StatusUtils {

    fun checkNetwork(context: Context): Boolean {
        return isWifiConnected(context) || isMobileConnected(context)
    }

    private fun isWifiConnected(context: Context): Boolean {
        try {
            val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo.type == ConnectivityManager.TYPE_WIFI && netInfo.isConnected
        } catch (e: Exception) {
            //quietly
        }

        return false
    }

    private fun isMobileConnected(context: Context): Boolean {
        try {
            val cm = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return when (netInfo.type) {
                ConnectivityManager.TYPE_WIFI -> false
                ConnectivityManager.TYPE_MOBILE -> true
                ConnectivityManager.TYPE_WIMAX -> false
                ConnectivityManager.TYPE_MOBILE_DUN -> true
                else -> false
            }
        } catch (e: Exception) {
            //quietly
        }
        return false
    }

    fun checkGPS(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true
        }
        return false
    }
}
