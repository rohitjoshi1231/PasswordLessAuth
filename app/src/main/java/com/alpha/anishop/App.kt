package com.alpha.anishop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alpha.anishop.ui.features.LoginUi
import com.alpha.anishop.ui.theme.AnishopTheme

class App : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val loginUi = LoginUi()
        setContent {
            AnishopTheme {
                loginUi.ConstraintLayoutExample()
            }
        }
    }
}
