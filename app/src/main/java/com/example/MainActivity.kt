package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.screens.GameScreen
import com.example.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    com.example.sound.TtsManager.init(this)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        val gameViewModel: GameViewModel = viewModel()
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          GameScreen(
              viewModel = gameViewModel,
              modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    com.example.sound.TtsManager.shutdown()
  }
}
