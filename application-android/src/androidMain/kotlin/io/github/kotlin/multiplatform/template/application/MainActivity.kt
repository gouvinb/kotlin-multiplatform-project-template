package io.github.kotlin.multiplatform.template.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kotlin.multiplatform.template.application.ui.theme.KotlinmultiplatformprojecttemplateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinmultiplatformprojecttemplateTheme {
                // A surface container using the 'background' color from the theme
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Greeting()
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Text(
        text = greetingText(),
        modifier = modifier
    )
}

// add apiLevel = 33 for avoid crash on preview
// See: https://issuetracker.google.com/issues/324732800
@Preview(apiLevel = 33, showBackground = true)
@Composable
fun GreetingPreview() {
    KotlinmultiplatformprojecttemplateTheme {
        Greeting()
    }
}
