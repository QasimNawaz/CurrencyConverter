package com.demo.currencyconvertertest.presentation.ui.screens.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.demo.currencyconvertertest.presentation.ui.navgraph.RootNavigationGraph
import com.demo.currencyconvertertest.presentation.ui.navgraph.Routes
import com.demo.currencyconvertertest.presentation.ui.theme.CurrencyConverterTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.loading.value
            }
        }
        setContent {
            val error: String? by viewModel.error.collectAsStateWithLifecycle()
            if (!viewModel.loading.collectAsStateWithLifecycle().value) {
                val startDestination =
                    if (error != null) Routes.ERROR_DIALOG else Routes.MAIN_SCREEN
                CurrencyConverterTheme {
                    RootNavigationGraph(
                        navController = rememberNavController(),
                        startDestination = startDestination,
                        errorMessage = error,
                        onPositiveClick = { finish() }
                    )
                }
            }
        }
    }
}