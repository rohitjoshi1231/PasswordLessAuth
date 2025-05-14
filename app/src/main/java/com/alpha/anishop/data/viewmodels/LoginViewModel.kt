package com.alpha.anishop.data.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alpha.anishop.data.repositories.TwilioRepository

sealed class LoginAction {
    data class OtpSent(val phoneNumber: String) : LoginAction()
    data class OtpVerified(val success: Boolean, val message: String) : LoginAction()
    data class Error(val message: String) : LoginAction()
    object Loading : LoginAction()
}


class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val twilioRepository = TwilioRepository()

    private val _otpStatus = MutableLiveData<String>()
    val otpStatus: LiveData<String> get() = _otpStatus

    private val _action = MutableLiveData<LoginAction>()
    val action: LiveData<LoginAction> get() = _action

    fun sendOTP(context: Context, phoneNumber: String) {
        _action.value = LoginAction.Loading

        twilioRepository.sendOTP(context, phoneNumber) { status ->
            _otpStatus.postValue(status)

            if (status.contains("pending", ignoreCase = true)) {
                _action.postValue(LoginAction.OtpSent(phoneNumber))
            } else {
                _action.postValue(LoginAction.Error("Failed to send OTP"))
            }
        }
    }

    fun verifyOtp(
        context: Context, phoneNumber: String, otp: String, callback: (Boolean, String) -> Unit
    ) {
        _action.value = LoginAction.Loading

        twilioRepository.verifyOtp(context, phoneNumber, otp) { success, message ->
            _action.postValue(LoginAction.OtpVerified(success, message))
            callback(success, message)
        }
    }
}

