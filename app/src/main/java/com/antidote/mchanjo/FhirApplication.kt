package com.antidote.mchanjo

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Telephony.TextBasedSmsColumns.BODY
import android.util.Log
import android.widget.Toast
import com.google.android.fhir.BuildConfig
import com.google.android.fhir.DatabaseErrorStrategy
import com.google.android.fhir.FhirEngine
import com.google.android.fhir.FhirEngineConfiguration
import com.google.android.fhir.FhirEngineProvider
import com.google.android.fhir.ServerConfiguration
import com.google.android.fhir.sync.remote.HttpLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class FhirApplication : Application() {

    private val fhirEngine: FhirEngine by lazy { FhirEngineProvider.getInstance(this) }

    override fun onCreate() {
        super.onCreate()

        // Initialize FHIR Engine
        FhirEngineProvider.init(
            FhirEngineConfiguration(
                enableEncryptionIfSupported = true,
                DatabaseErrorStrategy.RECREATE_AT_OPEN,
                ServerConfiguration(
 
                    baseUrl = "https://5943-197-232-57-160.ngrok-free.app/fhir/",
 
                    httpLogger =
                    HttpLogger(
                        HttpLogger.Configuration(
                            if (BuildConfig.DEBUG) HttpLogger.Level.BODY else HttpLogger.Level.BASIC
                        )
                    ) { Log.d("App-HttpLog", it) },
                ),
            )
        )

        // Check for successful connection to the server
        CoroutineScope(Dispatchers.IO).launch {
            checkServerConnection()
        }
    }

    // Convenience method to access the FHIR Engine instance
    companion object {
        fun fhirEngine(context: Context) = (context.applicationContext as FhirApplication).fhirEngine
    }

    private suspend fun checkServerConnection() {
        // actual ngrok URL or server endpoint.
        val ngrokUrl = "https://5943-197-232-57-160.ngrok-free.app"

        // Perform a sample request to check the connection
        val isConnected = performConnectionCheck(ngrokUrl)

        // Show a toast message and log the connection status
        withContext(Dispatchers.Main) {
            if (isConnected) {
                Toast.makeText(this@FhirApplication, "\uD83C\uDF1ESuccessfully connected to the server\uD83D\uDCE1\uD83D\uDCE1", Toast.LENGTH_SHORT).show()
                Log.d("App-Connection", "Successfully connected to the server")
            } else {
                Toast.makeText(this@FhirApplication, "\uD83C\uDF1A\uD83C\uDF1AFailed to connect to the server\uD83E\uDD26\uD83C\uDFFF\u200D♂️\uD83E\uDD26\uD83C\uDFFF\u200D♂️", Toast.LENGTH_SHORT).show()
                Log.d("App-Connection", "Failed to connect to the server")
            }
        }
    }

    private suspend fun performConnectionCheck(serverUrl: String): Boolean {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(serverUrl)
            .build()

        return try {
            val response = client.newCall(request).execute()
            response.isSuccessful
        } catch (e: IOException) {
            false
        }
    }
}
