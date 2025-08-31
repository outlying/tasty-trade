package com.antyzero.tastytrade.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.antyzero.tastytrade.core.auth.AuthUriProvider
import com.antyzero.tastytrade.core.auth.launchAuthCustomTab
import com.antyzero.tastytrade.feature.authentication.ui.AuthenticationScreen
import com.antyzero.tastytrade.feature.authentication.ui.AuthenticationViewModel
import com.antyzero.tastytrade.feature.login.ui.LoginScreen
import com.antyzero.tastytrade.feature.instrumentscryptocurrencies.ui.InstrumentsCryptocurrenciesScreen

object Routes {
    const val Login = "login"
    const val OAuthCallback = "oauth-callback?code={code}"
    const val Main = "main"
}

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    authUriProvider: AuthUriProvider,
    onNavControllerReady: (NavHostController) -> Unit = {}
) {

    val context = LocalContext.current
    val navController = rememberNavController()

    LaunchedEffect(navController) {
        onNavControllerReady.invoke(navController)
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Login,
    ) {
        composable(Routes.Login) {
            LoginScreen(
                onLogin = {
                    launchAuthCustomTab(context, authUriProvider.loginUri())
                },
                onIsAuthenticated = {
                    navController.navigate(Routes.Main) {
                        popUpTo(0)
                        launchSingleTop = true
                        restoreState = false
                    }
                }
            )
        }

        composable(
            route = Routes.OAuthCallback,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "${authUriProvider.redirectUri()}?code={code}"
                }
            )
        ) { navBackStackEntry ->

            val code: String? = navBackStackEntry.arguments?.getString("code")

            val authenticationViewModel: AuthenticationViewModel = hiltViewModel()
            AuthenticationScreen(authenticationViewModel) {
                navController.navigate(Routes.Main) {
                    popUpTo(0)
                    launchSingleTop = true
                    restoreState = false
                }
            }

            LaunchedEffect(code) { // Once per code
                // A better validation would be great here, but there is not doc about code format
                if(!code.isNullOrBlank()) {
                    authenticationViewModel.processCode(code)
                }
            }
        }

        composable(Routes.Main) {
            InstrumentsCryptocurrenciesScreen(modifier = modifier.padding(16.dp))
        }

    }
}
