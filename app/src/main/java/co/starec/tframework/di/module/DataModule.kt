package co.starec.tframework.di.module

import android.util.Log
import co.starec.tframework.app.TApplication
import co.starec.tframework.data.network.BaseInterceptor
import co.starec.tframework.utils.gson.GsonUTCDateAdapter
import co.starec.tframework.BuildConfig
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataModule(private var mApplication: TApplication) {

    @Provides
    @Singleton
    @Named("REST_ADAPTER")
    internal fun provideLoginRestAdapter(pBaseInterceptor: BaseInterceptor): Retrofit {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
        client.addInterceptor(pBaseInterceptor)
        if (BuildConfig.DEBUG)
            client.addInterceptor(logInterceptor)

        client.hostnameVerifier { pS, pSSLSession -> pS.equals(BuildConfig.HOST_NAME, ignoreCase = true) }
        //        SSLSocketFactory sslSocketFactory = SecurityUtil.getSSLConfig(mApplication);
        //        if (sslSocketFactory != null) {
        //            client.sslSocketFactory(sslSocketFactory, SecurityUtil.systemDefaultTrustManager());
        //        }

        val gson = GsonBuilder()
                .registerTypeAdapter(Date::class.java, GsonUTCDateAdapter())
                .create()!!

        val builder = Retrofit.Builder()
        builder.client(client.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BuildConfig.BASE_URL)

        return builder.build()
    }

    @Provides
    @Singleton
    @Named("TOKEN_REST_ADAPTER")
    internal fun provideRestAdapter(pBaseInterceptor: BaseInterceptor): Retrofit {
        val logInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        val builder = OkHttpClient.Builder()
                .addInterceptor(pBaseInterceptor)
                .addInterceptor(logInterceptor)
                .addInterceptor { chain ->
                    val runtime = mApplication.appComponent?.tPreference
                    val token = runtime?.userToken
                    Log.e("Token:", token)

                    val original = chain.request()

                    val request = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("x-auth-token", token)
                            .method(original.method(), original.body())
                            .build()
                    chain.proceed(request)
                }.readTimeout(3000, TimeUnit.SECONDS)
                .connectTimeout(3000, TimeUnit.SECONDS)
                .writeTimeout(3000, TimeUnit.SECONDS)

        builder.hostnameVerifier { pS, pSSLSession -> pS.equals(BuildConfig.HOST_NAME, ignoreCase = true) }
        //        SSLSocketFactory sslSocketFactory = SecurityUtil.getSSLConfig(mApplication.getApplicationContext());
        //        if (sslSocketFactory != null) {
        //            builder.sslSocketFactory(sslSocketFactory, SecurityUtil.systemDefaultTrustManager());
        //        }


        val gson = GsonBuilder()
                .registerTypeAdapter(Date::class.java, GsonUTCDateAdapter())
                .create()!!

        val retrofit = Retrofit.Builder()
        retrofit.client(builder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BuildConfig.BASE_URL)
        return retrofit.build()
    }


    @Provides
    @Singleton
    internal fun provideBaseInterceptor(): BaseInterceptor {
        return BaseInterceptor()
    }
}
