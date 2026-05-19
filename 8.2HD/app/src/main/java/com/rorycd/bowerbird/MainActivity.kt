package com.rorycd.bowerbird

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rorycd.bowerbird.ui.main.MainAppScreen
import com.rorycd.bowerbird.ui.theme.BowerbirdTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BowerbirdTheme {
                MainAppScreen()
            }
        }
    }
}
