package com.alpha.anishop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alpha.anishop.data.viewmodels.LoginViewModel
import com.alpha.anishop.ui.features.LoginUi
import com.alpha.anishop.ui.features.PasswordlessAuthScreen
import com.alpha.anishop.ui.features.VerifyOtp
import com.alpha.anishop.ui.theme.AnishopTheme

class App : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            enableEdgeToEdge()
            AnishopTheme {
                AppNavigation(loginViewModel)
            }
        }
    }

    @Composable
    fun AppNavigation(loginViewModel: LoginViewModel) {
        val navController = rememberNavController()

        NavHost(navController, startDestination = "loginScreen") {
            composable("loginScreen") {
                LoginUi(
                    navController, loginViewModel
                )
            }

            composable(
                route = "otpScreen/{phoneNumber}",
                arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
            ) { backStackEntry ->
                val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
                VerifyOtp().OtpScreen(navController, phoneNumber, loginViewModel)
            }

            composable("passwordlessAuthScreen") {
                PasswordlessAuthScreen(navController)
            }

        }
    }
}
