package co.starec.tframework.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import co.starec.tframework.listener.IAnimationCompleteListener
import co.starec.tframework.utils.animation.Flip3dAnimation
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object CommonUtils {

    /**
     * System info
     */

    fun getDeviceWidth(activity: Activity): Int {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels

    }

    fun getDeviceHeight(activity: Activity): Int {
        val metrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.heightPixels

    }

    /**
     * Distance
     */
    fun speedFormat(speed: Int, isMile: Boolean): String {
        return if (isMile) {
            if (speed * 3.28084 > 50)
                numberFormat(((speed * 3.6) * 0.621371))
            else numberFormat(speed * 3.28084)
        } else {
            if (speed > 15) numberFormat(speed * 3.6)
            else numberFormat(speed.toLong())
        }
    }

    fun speedUnit(speed: Int, isMile: Boolean): String {
        return if (isMile) {
            if (speed * 3.28084 > 50)
                "mile/h"
            else "ft/s"
        } else {
            if (speed > 15) "km/h"
            else "m/s"
        }
    }

    /**
     * Location
     */
    fun convertDistance(metDistance: Double): Double {
        return metDistance * 0.621
    }
    fun convertToMile(meterDistance: Long): Long {
        return ((meterDistance/1000) * 0.621).toLong()
    }
    fun distanceFormat(distance: Long, isMile: Boolean): String {
        return distanceNumber(distance, isMile) + " " + distanceUnit(distance, isMile)
    }

    fun distanceNumber(distance: Long, isMile: Boolean): String {
        return if (isMile) {
            if (distance * 3.28084 > 1000) {
                numberFormat(((distance / 1000.0) * 0.621371))
            } else {
                numberFormat((distance * 3.28084))
            }
        } else {
            if (distance > 1000) {
                numberFormat(distance / 1000.0)
            } else {
                numberFormat(distance)
            }
        }
    }

    fun distanceUnit(distance: Long, isMile: Boolean): String {
        return if (isMile) {
            if (distance * 3.28084 > 1000) {
                "miles"
            } else {
                "ft"
            }
        } else {
            if (distance > 1000) {
                "km"
            } else {
                "m"
            }
        }
    }

    fun distanceUnitFull(distance: Long, isMile: Boolean): String {
        return if (isMile) {
            if (distance * 3.28084 > 1000) {
                "miles"
            } else {
                "feet"
            }
        } else {
            if (distance > 1000) {
                "kilometers"
            } else {
                "meters"
            }
        }
    }

    fun distanceMeterUnit(isMile: Boolean): String {
        return if (isMile) return "ft"
        else "m"
    }

    /**
     * Format number
     */
    fun numberFormat(number: Long?): String {
        val formatter = DecimalFormat("###,###,###,###,###.#")
        return formatter.format(number)
    }

    fun numberFormat(number: Double?): String {
        val formatter = DecimalFormat("###,###,###,###,###.#")
        return formatter.format(number)
    }

    fun priceFormat(price: Float): String {
        return String.format(Locale.US, "%,.0f $", price).replace(",".toRegex(), ".")
    }

    fun numberLargeFormat(number: Long): String {
        return if (number > 1000000) {
            numberFormat(number / 1000000.0)
        } else {
            numberFormat(number)
        }
    }

    fun numberLargeUnit(number: Long): String {
        return if (number > 1000000) {
            "million"
        } else ""
    }


    /**
     * Format time
     */

    fun isNight(date: Date): Boolean {
        if (date.hours in 6..18)
            return false
        return true
    }

    fun formatCountTime(time_count: Long): String {
        val hour = time_count / 60 / 60
        val minute = (time_count / 60) % 60
        val second = time_count % 60

        return if (time_count < 60 * 60) {
            String.format("%02d", minute) + ":" + String.format("%02d", second)
        } else {
            String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second)
        }
    }

    fun periodTimeLong(total_time: Long): String {
        val period = total_time / 1000
        if (period < 60)
            return period.toString() + " sec"
        else if (period < 60 * 60)
            return (period / 60).toString() + "m " + period % 60 + "s"
        else
            return (period / 60 / 60).toString() + "h " + period % (60 * 60) / 60 + "m " + period % 60 + "s"
    }

    fun timeFormat(total_time: Long): String {
        val period = total_time / 1000
        return when {
            period < 60 -> period.toString() + " sec"
            period < 60 * 60 -> (period / 60).toString() + "m " + period % 60 + "s"
            else -> (period / 60 / 60).toString() + "h " + period % (60 * 60) / 60 + "m"
        }
    }

    fun timeFormatShort(total_time: Long): String {
        return timeNumberShort(total_time) + timeUnit(total_time)
    }

    fun timeNumberShort(total_time: Long): String {
        val period = total_time / 1000
        if (period < 60)
            return period.toString()
        else if (period < 60 * 60)
            return (period / 60).toString()
        else if (period < 60 * 60 * 24)
            return (period / 60 / 60).toString()
        else
            return (period / 60 / 60 / 24).toString()
    }

    fun timeUnit(total_time: Long): String {
        val period = total_time / 1000
        return when {
            period < 60 -> "seconds"
            period < 60 * 60 -> "minutes"
            period < 60 * 60 * 24 -> "hours"
            else -> "days"
        }
    }

    fun getDayOfMonth(value: Float): String {
        val c = Calendar.getInstance()
        //Set time in milliseconds
        c.timeInMillis = value.toLong()
        val date = c.get(Calendar.DAY_OF_MONTH).toString()
        val month = (c.get(Calendar.MONTH) + 1).toString()
        return date + "/" + month
    }

    @SuppressLint("SimpleDateFormat")
    fun getDateMillis(currentTime: Long): Long {
        val dfDate = SimpleDateFormat("yyyy-MM-dd")
        val dateString = dfDate.format(currentTime)
        var d: Date
        try {
            d = dfDate.parse(dateString)
            return d.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0L
    }

    @SuppressLint("SimpleDateFormat")
    fun formStringToMillis(createdAt: String): Float {
        val dfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        var d: Date? = null
        try {
            d = dfDate.parse(createdAt)
            return d!!.time.toFloat()
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return 0f
    }

    @SuppressLint("SimpleDateFormat")
    fun checkDifferenceDate(createdAt: String, createdAt1: String): Boolean {
        val dfDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var d: Date? = null
        var d1: Date? = null
        try {
            d = dfDate.parse(createdAt)
            d1 = dfDate.parse(createdAt1)//Returns 15/10/2012
        } catch (e: java.text.ParseException) {
            e.printStackTrace()
            return true
        } catch (e: NullPointerException) {
            e.printStackTrace()
            return true
        }

        return d!!.date - d1!!.date > 0
    }

    /**
     * Format String
     */
    fun validateString(string: String?): Boolean {
        if (string != null && !string.isEmpty())
            return true
        return false
    }

    /**
     * Animation
     */

    fun applyFlipAnimation(view: View, duration: Long, fromDegree: Float, toDegree: Float,
                           listener: IAnimationCompleteListener) {
        val flip = Flip3dAnimation(view, listener, duration, fromDegree, toDegree)
        flip.applyPropertiesInRotation()
        view.startAnimation(flip)
        flip.setAnimationListener(object : AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                listener.onCompleteAnimation()
            }

            override fun onAnimationStart(p0: Animation?) {

            }
        })
    }

    fun applyCardBoadAnimation(view: View, listener: IAnimationCompleteListener?) {
        applyFlipAnimation(view, 200, -90f, 5f, object : IAnimationCompleteListener {
            override fun onCompleteAnimation() {
                listener?.onCompleteAnimation()
                applyFlipAnimation(view, 100, 5f, -5f, object : IAnimationCompleteListener {
                    override fun onCompleteAnimation() {
                        applyFlipAnimation(view, 20, -5f, 0f, object : IAnimationCompleteListener {
                            override fun onCompleteAnimation() {

                            }
                        })
                    }
                })
            }
        })
    }

    fun md5(s: String): String {
        try {
            // Create MD5 Hash
            val digest = java.security.MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuffer()
            for (i in messageDigest.indices) {
                var h = Integer.toHexString(0xFF and messageDigest[i].toInt())
                while (h.length < 2)
                    h = "0" + h
                hexString.append(h)
            }
            return hexString.toString()

        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return ""
    }

    fun getAppVersionName(packageName: String, packageManager: PackageManager): String {
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            return pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return ""
    }

    fun getAppVersionCode(packageName: String, packageManager: PackageManager): Int {
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            return pInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 1
    }

    @SuppressLint("SimpleDateFormat")
    fun millisecondToDate(currentTimeMillis: Long, format: String): String {
        val formatPattern = SimpleDateFormat(format)
        return formatPattern.format(currentTimeMillis)
    }
}
