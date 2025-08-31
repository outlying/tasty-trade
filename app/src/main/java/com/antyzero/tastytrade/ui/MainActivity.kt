package com.antyzero.tastytrade.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.antyzero.tastytrade.core.auth.AuthUriConfiguration
import com.antyzero.tastytrade.core.auth.AuthUriProvider
import com.antyzero.tastytrade.core.ui.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import io.github.oshai.kotlinlogging.KotlinLogging
import javax.inject.Inject


private val logger = KotlinLogging.logger {  }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null

    @Inject lateinit var authUriProvider: AuthUriProvider

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation(
                        authUriProvider = authUriProvider,
                        onNavControllerReady = { navControllerReady ->
                            navController = navControllerReady
                        }
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if(navController?.handleDeepLink(intent) != true) {
            logger.warn { "Received intent was not handled by NavController, ${intent.data}" }
        }
    }
}
