package com.alpha.anishop.ui.features

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.alpha.anishop.BuildConfig
import com.alpha.anishop.R
import com.alpha.anishop.data.viewmodels.LoginViewModel

class VerifyOtp {

    private val poppinsFamily = FontFamily(Font(R.font.poppins_regular))

    @SuppressLint("MutableCollectionMutableState")
    @Composable
    fun OtpScreen(
        navController: NavController, phoneNumber: String, loginViewModel: LoginViewModel
    ) {
        val context = LocalContext.current


        var otpState by remember { mutableStateOf(MutableList(4) { "" }) }

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFAFAFA), Color(0xFFE0F7FA))
                    )
                )
                .padding(16.dp)
        ) {
            val (title, description, otpTextField, resendOtpTxt, buttonField) = createRefs()

            TitleText(modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = 32.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

            DescriptionText(modifier = Modifier.constrainAs(description) {
                top.linkTo(title.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

            OtpTextField(otpValue = otpState, onOtpChange = { index, newValue ->
                otpState = otpState.toMutableList().apply { this[index] = newValue }
            }, modifier = Modifier.constrainAs(otpTextField) {
                top.linkTo(description.bottom, margin = 100.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

            ResendOtpText(modifier = Modifier.constrainAs(resendOtpTxt) {
                top.linkTo(otpTextField.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

            FilledTonalButton(
                modifier = Modifier
                    .size(width = 200.dp, height = 55.dp)
                    .constrainAs(buttonField) {
                        top.linkTo(resendOtpTxt.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }, onClick = {
                val enteredOtp = otpState.joinToString("").trim()
                if (enteredOtp.length == 4) {
                    loginViewModel.verifyOtp(
                        context, phoneNumber, enteredOtp
                    ) { success, message ->
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        if (success) {
                            navController.navigate("passwordlessAuthScreen") // Navigate after OTP verification
                        }
                    }
                } else {
                    Toast.makeText(context, "Enter a valid OTP", Toast.LENGTH_SHORT).show()
                }
            }, colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color(0, 130, 252), contentColor = Color.White
            )
            ) {
                Text("Continue", fontSize = 18.sp, fontFamily = poppinsFamily)
            }
        }
    }


    @Composable
    fun TitleText(modifier: Modifier) {
        Text(
            text = "Enter 4 Digit Code",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = poppinsFamily,
            textAlign = TextAlign.Start,
            modifier = modifier
        )
    }

    @Composable
    fun DescriptionText(modifier: Modifier) {
        Text(
            text = "Enter the 4-digit code you received on your phone.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Gray,
            fontFamily = poppinsFamily,
            textAlign = TextAlign.Center,
            modifier = modifier
        )
    }

    @Composable
    fun ResendOtpText(modifier: Modifier) {
        Text(
            text = "OTP Not received? Resend",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Blue,
            fontFamily = poppinsFamily,
            textAlign = TextAlign.Center,
            modifier = modifier
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun OtpTextField(
        modifier: Modifier, otpValue: MutableList<String>, onOtpChange: (Int, String) -> Unit
    ) {
        val focusManager = LocalFocusManager.current
        Row(
            modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(4) { index ->
                OutlinedTextField(
                    value = otpValue.getOrNull(index) ?: "",
                    onValueChange = { newValue ->
                        if (newValue.length <= 1) {
                            onOtpChange(index, newValue)

                            // Move to next or previous field based on input
                            if (newValue.isNotEmpty() && index < 3) {
                                focusManager.moveFocus(FocusDirection.Next)
                            } else if (newValue.isEmpty() && index > 0) {
                                focusManager.moveFocus(FocusDirection.Previous)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(50.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 20.sp, textAlign = TextAlign.Center
                    ),
                    colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color(0xFF0078FC),
                        unfocusedBorderColor = Color.Gray,
                        focusedTextColor = Color.Black
                    )
                )
            }
        }
    }


    @Composable
    @Preview(showBackground = true)
    fun PreviewOtpScreen() {
        if (BuildConfig.DEBUG) {
            VerifyOtp().OtpScreen(
                navController = rememberNavController(),
                phoneNumber = "+91 9876543210",
                loginViewModel = LoginViewModel(Application())
            )
        }
    }
}
