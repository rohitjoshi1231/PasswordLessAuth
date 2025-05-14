package com.alpha.anishop.data.repositories


import android.content.Context
import android.util.Base64
import com.alpha.anishop.BuildConfig
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class TwilioRepository {
    private val baseUrl = "https://verify.twilio.com/v2/Services/"
    fun sendOTP(context: Context, phoneNumber: String, callback: (String) -> Unit) {
        val queue = Volley.newRequestQueue(context)
        val url = "$baseUrl${BuildConfig.SERVICE_SID}/Verifications"

        val request = object : StringRequest(Method.POST, url, Response.Listener { response ->
            val json = JSONObject(response)
            callback(json.optString("status", "unknown"))
        }, Response.ErrorListener { error ->
            callback("Error: ${String(error.networkResponse?.data ?: "Unknown error".toByteArray())}")
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                val auth = "${BuildConfig.ACCOUNT_SID}:${BuildConfig.AUTH_TOKEN}"
                val encodedAuth = Base64.encodeToString(auth.toByteArray(), Base64.NO_WRAP)
                headers["Authorization"] = "Basic $encodedAuth"
                return headers
            }

            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["To"] = phoneNumber
                params["Channel"] = "sms"
                return params
            }
        }

        queue.add(request)
    }

    fun verifyOtp(
        context: Context, phoneNumber: String, otp: String, callback: (Boolean, String) -> Unit
    ) {
        val formattedPhoneNumber =
            if (phoneNumber.startsWith("+")) phoneNumber else "+91$phoneNumber"
        val url = "${baseUrl}${BuildConfig.SERVICE_SID}/VerificationCheck"

        println("phone number: $phoneNumber")
        println("formattedPhoneNumber: $formattedPhoneNumber")
        val request = object : StringRequest(Method.POST, url, Response.Listener { response ->
            try {
                val jsonResponse = JSONObject(response)
                val status = jsonResponse.optString("status", "failed")
                if (status == "approved") {
                    callback(true, "OTP verified successfully!")
                } else {
                    callback(false, "Invalid OTP")
                }
            } catch (e: Exception) {
                callback(false, "Response parsing error")
            }
        }, Response.ErrorListener { error ->
            val errorMsg = error.networkResponse?.data?.let { String(it) } ?: "Unknown error"
            callback(false, "Error: $errorMsg")
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val auth = "${BuildConfig.ACCOUNT_SID}:${BuildConfig.AUTH_TOKEN}"
                val encodedAuth = Base64.encodeToString(auth.toByteArray(), Base64.NO_WRAP)
                return mutableMapOf(
                    "Authorization" to "Basic $encodedAuth",
                    "Content-Type" to "application/x-www-form-urlencoded"
                )
            }

            override fun getParams(): MutableMap<String, String> {
                return mutableMapOf(
                    "To" to formattedPhoneNumber,
                    "Code" to otp
                )
            }
        }

        Volley.newRequestQueue(context).add(request)
    }


}
