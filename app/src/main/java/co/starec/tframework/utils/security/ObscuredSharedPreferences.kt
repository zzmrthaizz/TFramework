package co.starec.runningapp.runther.framework.utils.security

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import co.starec.tframework.utils.security.Base64Support

import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.PBEParameterSpec


open class ObscuredSharedPreferences
/**
 * Constructor

 * @param context
 * *
 * @param delegate - SharedPreferences object from the system
 */
(protected var context: Context, protected var delegate: SharedPreferences) : SharedPreferences {

    init {
        SEKRIT = KEY.toCharArray()
    }

    override fun edit(): Editor {
        return Editor()
    }

    override fun getAll(): Map<String, *> {
        throw UnsupportedOperationException() // left as an exercise to the reader
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        //if these weren't encrypted, then it won't be a string
        val v: String?
        try {
            v = delegate.getString(key, null)
        } catch (e: Exception) {
            return delegate.getBoolean(key, defValue)
        }

        return if (v != null) java.lang.Boolean.parseBoolean(decrypt(v)) else defValue
    }

    override fun getFloat(key: String, defValue: Float): Float {
        val v: String
        try {
            v = delegate.getString(key, null)
        } catch (e: Exception) {
            return delegate.getFloat(key, defValue)
        }

        try {
            return java.lang.Float.parseFloat(decrypt(v))
        } catch (e: NumberFormatException) {
            //could not decrypt the number.  Maybe we are using the wrong key?
            decryptionErrorFlag = true
        }

        return defValue
    }

    override fun getInt(key: String, defValue: Int): Int {
        val v: String
        try {
            v = delegate.getString(key, null)
        } catch (e: Exception) {
            return delegate.getInt(key, defValue)
        }

        try {
            return Integer.parseInt(decrypt(v))
        } catch (e: NumberFormatException) {
            //could not decrypt the number.  Maybe we are using the wrong key?
            decryptionErrorFlag = true
        }

        return defValue
    }

    override fun getLong(key: String, defValue: Long): Long {
        val v: String
        try {
            v = delegate.getString(key, null)
        } catch (e: Exception) {
            return delegate.getLong(key, defValue)
        }

        try {
            return java.lang.Long.parseLong(decrypt(v))
        } catch (e: NumberFormatException) {
            //could not decrypt the number.  Maybe we are using the wrong key?
            decryptionErrorFlag = true
        }

        return defValue
    }

    override fun getString(key: String, defValue: String?): String? {
        val v = delegate.getString(key, null)
        return if (v != null) decrypt(v) else defValue
    }

    override fun contains(s: String): Boolean {
        return delegate.contains(s)
    }

    override fun registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        delegate.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    override fun unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        delegate.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener)
    }

    override fun getStringSet(key: String, defValues: Set<String>?): Set<String>? {
        throw RuntimeException("This class does not work with String Sets.")
    }

    private //        if (BuildConfig.DEBUG)
            //            return DEBUG_SALT;
    val encryptionSalt: String
        get() = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    protected fun encrypt(value: String?): String {
        try {
            val bytes = value?.toByteArray(charset(UTF8)) ?: ByteArray(0)
            val keyFactory = SecretKeyFactory.getInstance(ALGORITHM)
            val key = keyFactory.generateSecret(PBEKeySpec(SEKRIT))
            val pbeCipher = Cipher.getInstance(ALGORITHM)
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, PBEParameterSpec(encryptionSalt.toByteArray(charset(UTF8)), 20))
            return String(Base64Support.encode(pbeCipher.doFinal(bytes), Base64Support.NO_WRAP))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    protected fun decrypt(value: String?): String {
        try {
            val bytes = if (value != null) Base64Support.decode(value, Base64Support.DEFAULT) else ByteArray(0)
            val keyFactory = SecretKeyFactory.getInstance(ALGORITHM)
            val key = keyFactory.generateSecret(PBEKeySpec(SEKRIT))
            val pbeCipher = Cipher.getInstance(ALGORITHM)
            pbeCipher.init(Cipher.DECRYPT_MODE, key, PBEParameterSpec(encryptionSalt.toByteArray(charset(UTF8)), 20))
            return String(pbeCipher.doFinal(bytes))
        } catch (e: Exception) {
            return value as String
        }

    }

    inner class Editor : SharedPreferences.Editor {
        protected var delegate: SharedPreferences.Editor

        init {
            this.delegate = this@ObscuredSharedPreferences.delegate.edit()
        }

        override fun putBoolean(key: String, value: Boolean): Editor {
            delegate.putString(key, encrypt(java.lang.Boolean.toString(value)))
            return this
        }

        override fun putFloat(key: String, value: Float): Editor {
            delegate.putString(key, encrypt(java.lang.Float.toString(value)))
            return this
        }

        override fun putInt(key: String, value: Int): Editor {
            delegate.putString(key, encrypt(Integer.toString(value)))
            return this
        }

        override fun putLong(key: String, value: Long): Editor {
            delegate.putString(key, encrypt(java.lang.Long.toString(value)))
            return this
        }

        override fun putString(key: String, value: String?): Editor {
            delegate.putString(key, encrypt(value))
            return this
        }

        override fun apply() {
            //to maintain compatibility with android level 7
            delegate.commit()
        }

        override fun clear(): Editor {
            delegate.clear()
            return this
        }

        override fun commit(): Boolean {
            return delegate.commit()
        }

        override fun remove(s: String): Editor {
            delegate.remove(s)
            return this
        }

        override fun putStringSet(key: String, values: Set<String>?): SharedPreferences.Editor {
            throw RuntimeException("This class does not work with String Sets.")
        }
    }

    companion object {
        protected val UTF8 = "UTF-8"
        private val KEY = "d5bacc77-e6a7-471e-9406-s815fced99ad"
        private val ALGORITHM = "PBEWithMD5AndDES"
        private val DEBUG_SALT = "a234b567"
        //Set to true if a decryption error was detected
        //in the case of float, int, and long we can tell if there was a parse error
        //this does not detect an error in strings or boolean - that requires more sophisticated checks
        var decryptionErrorFlag = false
        private var SEKRIT: CharArray? = null
        private var prefs: ObscuredSharedPreferences? = null

        /**
         * Only used to change to a ic_new key during runtime.
         * If you don't want to use the default per-device key for example

         * @param key
         */
        fun setNewKey(key: String) {
            SEKRIT = key.toCharArray()
        }

        /**
         * Accessor to grab the preferences in a singleton.  This stores the reference in a singleton so it can be accessed repeatedly with
         * no performance penalty

         * @param c           - the context used to access the preferences.
         * *
         * @param appName     - domain the shared preferences should be stored under
         * *
         * @param contextMode - Typically Context.MODE_PRIVATE
         * *
         * @return ic_new ObscuredSharedPreference
         */
        @Synchronized fun getPrefs(c: Context, appName: String, contextMode: Int): ObscuredSharedPreferences {
            if (prefs == null) {
                //make sure to use application context since preferences live outside an Activity
                //use for objects that have global scope like: prefs or starting services
                prefs = ObscuredSharedPreferences(
                        c.applicationContext, c.applicationContext.getSharedPreferences(appName, contextMode))
            }
            return prefs as ObscuredSharedPreferences
        }
    }
}
