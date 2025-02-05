package com.example.tatatechproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tatatechproject.repository.ContentProviderRepository
import com.example.tatatechproject.ui.theme.TataTechProjectTheme
import com.example.tatatechproject.viemodel.ContentProviderViewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: ContentProviderViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Create Repository Instance
        val repository = ContentProviderRepository(this)

        // Get the ViewModel
        viewModel = ContentProviderViewModel(repository)
        viewModel.randomText.observe(this) { text ->
            Log.d("UI", "Fetched Text: $text")
        }

        enableEdgeToEdge()
        setContent {

            TataTechProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AppLayout()

                }
                    }
            }
        }
    }

    @Composable
    fun AppLayout() {
        var text by remember { mutableStateOf("") }

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Label") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                //request of length
                viewModel.loadData(text)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate random string.")
        }
    }

}


