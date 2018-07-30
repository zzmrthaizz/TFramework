package co.starec.tframework.data.local

import co.starec.runningapp.runther.framework.utils.security.ObscuredSharedPreferences

import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TPreference @Inject
constructor(private val mSharedPreferences: ObscuredSharedPreferences) {
    var userToken: String
        get() = mSharedPreferences.getString(USER_TOKEN, "") as String
        set(token) = mSharedPreferences.edit().putString(USER_TOKEN, token).apply()

    companion object {
        private val USER_TOKEN = "user_token"
        }
}
