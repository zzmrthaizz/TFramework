package co.starec.tframework.utils.security

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi

import java.io.IOException
import java.io.InputStream
import java.security.GeneralSecurityException
import java.security.KeyManagementException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.util.Arrays
import javax.net.ssl.*


object SecurityUtil {
    /**
     * generate a sha256 string from base string

     * @param base input string
     * *
     * @return sha256 generated string
     */
    fun sha256(base: String): String {
        try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(base.toByteArray(charset("UTF-8")))
            val hexString = StringBuilder()

            for (aHash in hash) {
                val hex = Integer.toHexString(0xff and aHash.toInt())
                if (hex.length == 1) hexString.append('0')
                hexString.append(hex)
            }

            return hexString.toString()
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }

    }

    fun configureSSL(keystorePath: InputStream, password: String): SSLContext? {
        val ks = KeyStore.getInstance("PKCS12")
        ks.load(keystorePath, password.toCharArray())

        val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
        kmf.init(ks, password.toCharArray())

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(ks)

        val sc = SSLContext.getInstance("TLS")
        val trustManagers = tmf.trustManagers
        sc.init(kmf.keyManagers, trustManagers, null)
        return sc
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun getSSLConfig(context: Context, file: Int): SSLSocketFactory? {

        try {
            // Loading CAs from an InputStream
            val cf: CertificateFactory? = CertificateFactory.getInstance("X.509")


            var ca: Certificate? = null
            // I'm using Java7. If you used Java6 close it manually with finally.
            try {
                context.resources.openRawResource(file).use { cert -> ca = cf!!.generateCertificate(cert) }
            } catch (pE: IOException) {
                pE.printStackTrace()
            }

            // Creating a KeyStore containing our trusted CAs
            val keyStoreType = KeyStore.getDefaultType()
            val keyStore = KeyStore.getInstance(keyStoreType)
            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", ca)

            // Creating a TrustManager that trusts the CAs in our KeyStore.
            val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
            val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
            tmf.init(keyStore)

            // Creating an SSLSocketFactory that uses our TrustManager
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, tmf.trustManagers, null)

            return sslContext.socketFactory

        } catch (pE: CertificateException) {
            pE.printStackTrace()
            return null
        } catch (pE: NoSuchAlgorithmException) {
            pE.printStackTrace()
            return null
        } catch (pE: KeyStoreException) {
            pE.printStackTrace()
            return null
        } catch (pE: KeyManagementException) {
            pE.printStackTrace()
            return null
        } catch (pE: IOException) {
            pE.printStackTrace()
            return null
        }

    }

    fun systemDefaultTrustManager(): X509TrustManager {
        try {
            val trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                throw IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers))
            }
            return trustManagers[0] as X509TrustManager
        } catch (e: GeneralSecurityException) {
            throw AssertionError() // The system has no TLS. Just give up.
        }

    }
}
