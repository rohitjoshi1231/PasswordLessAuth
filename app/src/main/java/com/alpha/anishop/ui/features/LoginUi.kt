package com.alpha.anishop.ui.features

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavController
import com.alpha.anishop.R
import com.alpha.anishop.data.viewmodels.LoginAction
import com.alpha.anishop.data.viewmodels.LoginViewModel

val poppinsFamily = FontFamily(Font(R.font.poppins_regular))

@Composable
fun LoginUi(navController: NavController, loginViewModel: LoginViewModel) {
    val loginAction by loginViewModel.action.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(loginAction) {
        when (val action = loginAction) {
            is LoginAction.OtpSent -> {
                navController.navigate("otpScreen/${action.phoneNumber}")
            }

            is LoginAction.Error -> {
                Toast.makeText(context, action.message, Toast.LENGTH_SHORT).show()
            }

            is LoginAction.OtpVerified -> {
                Toast.makeText(
                    context,
                    if (action.success) "OTP Verified" else action.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> Unit
        }
    }

    LoginScreen(loginViewModel)
}


@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
    val loginViewModel = LoginViewModel(Application())
    LoginScreen(loginViewModel)
}


@Composable
fun LoginScreen(loginViewModel: LoginViewModel) {
    val context = LocalContext.current
    val text = remember { mutableStateOf("") }
    val formattedPhoneNumber = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFFAFAFA), Color(0xFFE0F7FA))
                )
            )
            .padding(24.dp)
    ) {
        // Profile Image
        Image(
            painter = painterResource(id = R.drawable.profile_picture),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .border(3.dp, Color(0xFF1976D2), CircleShape)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Fit
        )

        // Title
        Text(
            text = "Password-less Login",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D47A1),
            fontFamily = poppinsFamily,
            modifier = Modifier.padding(top = 20.dp)
        )

        // Login Label
        Text(
            text = "Enter your phone number",
            fontSize = 16.sp,
            color = Color.Gray,
            fontFamily = poppinsFamily,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Phone Number Input
        CustomTextField(
            labelText = "Contact Number", textState = text, modifier = Modifier.padding(top = 24.dp)
        )

        // OTP Button
        FilledTonalButton(
            {
                val phoneNumber = text.value.trim()
                if (phoneNumber.isNotEmpty()) {
                    // Format the phone number if it doesn't start with "+"
                    formattedPhoneNumber.value =
                        if (!phoneNumber.startsWith("+")) "+91$phoneNumber" else phoneNumber

                    // Save the phone number to SharedPreferences
                    val sharedPreferences: SharedPreferences =
                        context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
                    sharedPreferences.edit {
                        putString("phone_number", formattedPhoneNumber.value)
                    }

                    // Call the method to send OTP
                    loginViewModel.sendOTP(context, formattedPhoneNumber.value)
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Receive OTP", fontSize = 18.sp, fontFamily = poppinsFamily)
        }
    }
}

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier, labelText: String, textState: MutableState<String>
) {
    OutlinedTextField(
        value = textState.value,
        onValueChange = { newText ->
            if (newText.length <= 10 && newText.all { it.isDigit() }) {
                textState.value = newText
            }
        },
        label = { Text(labelText, fontSize = 18.sp, fontFamily = poppinsFamily) },
        modifier = modifier.fillMaxWidth(),
        textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
        shape = RoundedCornerShape(16.dp),
        leadingIcon = {
            Icon(Icons.Filled.Call, contentDescription = "Call", tint = Color.Black)
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone)
    )
}
