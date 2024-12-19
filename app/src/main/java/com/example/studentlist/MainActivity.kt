package com.example.studentlist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.studentlist.ui.theme.StudentListTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudentListTheme {
                StudentListScreen()
            }
        }
    }
}

data class Student(val name: String, val favorite: Boolean)

@Composable
fun StudentListScreen() {
    var students by remember { mutableStateOf((1..20).map { Student("Pies $it",false) }.toMutableList()) }
    var searchQuery by remember { mutableStateOf(TextFieldValue()) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(WindowInsets.statusBars.asPaddingValues())
        .padding(16.dp)) {
        SearchAndAddBar(
            searchQuery = searchQuery.text,
            onSearchChange = { searchQuery = TextFieldValue(it) },
            onAdd = {
                if (it.isNotBlank()) {
                    students.add(Student(name = it,favorite = false))
                }
            }
        )
        val favoriteCount = students.count { it.favorite }
        val notfavoriteCount = students.count { !it.favorite }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Search icon",
            )

            Text(
                text = ": " +favoriteCount
            )

            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Search icon"
            )

            Text(
                text = ": " +notfavoriteCount
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
            items(students.filter { it.name.contains(searchQuery.text, ignoreCase = true) }) { student ->
                if (student.favorite == true) {
                    StudentItem(
                        student = student,
                        onDelete = {
                            students = students.filter { it != student }.toMutableList()
                        },
                        onFavorite = {
                            students = students.map { s ->
                                if (s.name == student.name) {
                                    s.copy(favorite = !student.favorite)
                                } else {
                                    s
                                }
                            }.toMutableList()
                        }
                    )
                }
            }
            items(students.filter { it.name.contains(searchQuery.text, ignoreCase = true) }) { student ->
                if (student.favorite == false) {
                    StudentItem(
                        student = student,
                        onDelete = {
                            students = students.filter { it != student }.toMutableList()
                        },
                        onFavorite = {
                            students = students.map { s ->
                                if (s.name == student.name) {
                                    s.copy(favorite = !student.favorite)
                                } else {
                                    s
                                }
                            }.toMutableList()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchAndAddBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onAdd: (String) -> Unit
) {
    var inputText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Poszukaj lub dodaj pieska") }
        )

        IconButton(onClick = { onSearchChange(inputText) }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon",
            )
        }

        IconButton(onClick = {
            onAdd(inputText)
            inputText = ""
        }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add icon"
            )
        }
    }
}


@Composable
fun StudentItem(student: Student, onDelete: () -> Unit, onFavorite: () -> Unit) {
    Column {

        Divider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val gradientBrush = Brush.linearGradient(
            colors = listOf(Color.Red, Color.Blue),
            start = Offset(0f, 0f),
            end = Offset(100f, 100f)
        )

        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "Search icon",
            tint = Color.White,
            modifier = Modifier
                .background(gradientBrush)
                .size(40.dp)
                .padding(2.dp)

        )
        Text(
            modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
            text = student.name,
            style = MaterialTheme.typography.labelLarge)

        if (student.favorite == true)
        {
            IconButton(onClick = onFavorite) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Delete icon"
                )
            }
        }
        else
        {
            IconButton(onClick = onFavorite) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "Delete icon"
                )
            }
        }

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete icon"
            )
        }
    }
    }
}

