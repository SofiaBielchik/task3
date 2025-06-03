package com.example.lab3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.lab3.ui.theme.Lab3Theme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab3Theme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AuthorYearSelector(
                        onOpenStorage = {
                            val intent = Intent(this@MainActivity, StorageActivity::class.java)
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AuthorYearSelector(
    onOpenStorage: () -> Unit
) {
    val context = LocalContext.current

    val authors = listOf(
        "Тарас Шевченко",
        "Ліна Костенко",
        "Леся Українка",
        "Іван Франко"
    )
    val years = listOf(
        "1840",
        "1960",
        "1910",
        "1890"
    )

    var selectedAuthor by remember { mutableStateOf("") }
    var selectedYear by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            confirmButton = {
                TextButton(onClick = { showAlert = false }) {
                    Text("OK")
                }
            },
            title = { Text("Увага") },
            text = { Text("Будь ласка, введіть всі дані перед натисканням кнопки 'ОК'.") }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Введення інформації про книгу", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Оберіть автора", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
        ) {
            OutlinedTextField(
                value = selectedAuthor,
                onValueChange = {},
                readOnly = true,
                label = { Text("Автор") },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                authors.forEach { author ->
                    DropdownMenuItem(
                        text = { Text(author) },
                        onClick = {
                            selectedAuthor = author
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Оберіть рік видання", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        years.forEach { year ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (year == selectedYear),
                        onClick = { selectedYear = year },
                        role = Role.RadioButton
                    )
                    .padding(8.dp)
            ) {
                RadioButton(
                    selected = (year == selectedYear),
                    onClick = { selectedYear = year }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = year, style = MaterialTheme.typography.bodyLarge)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (selectedAuthor.isEmpty() || selectedYear.isEmpty()) {
                    showAlert = true
                    resultText = ""
                } else {
                    resultText = "Автор: $selectedAuthor\nРік видання: $selectedYear"

                    // Запис у файл
                    try {
                        val fileName = "storage_data.txt"
                        val file = File(context.filesDir, fileName)
                        file.appendText(resultText + "\n\n")
                        Toast.makeText(context, "Дані успішно збережено", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Помилка запису у файл: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ОК")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onOpenStorage() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Відкрити")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (resultText.isNotEmpty()) {
            Text(resultText, style = MaterialTheme.typography.bodyLarge)
        }
    }
}