package com.example.lab3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab3.ui.theme.Lab3Theme
import java.io.File
import androidx.compose.ui.platform.LocalContext


class StorageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab3Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    StorageScreen()
                }
            }
        }
    }
}

@Composable
fun StorageScreen() {
    val context = LocalContext.current
    var fileContent by remember { mutableStateOf("") }
    val fileName = "storage_data.txt"

    LaunchedEffect(Unit) {
        try {
            val file = File(context.filesDir, fileName)
            if (file.exists() && file.length() > 0) {
                fileContent = file.readText()
            } else {
                fileContent = "Файл порожній або дані відсутні."
            }
        } catch (e: Exception) {
            fileContent = "Помилка читання файлу: ${e.message}"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Збережені дані:", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text(fileContent, style = MaterialTheme.typography.bodyLarge)
    }
}