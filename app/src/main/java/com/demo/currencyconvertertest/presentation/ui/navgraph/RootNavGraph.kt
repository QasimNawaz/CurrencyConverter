package com.demo.currencyconvertertest.presentation.ui.navgraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.demo.currencyconvertertest.R
import com.demo.currencyconvertertest.presentation.ui.components.AlertMessageDialog
import com.demo.currencyconvertertest.presentation.ui.screens.main.MainScreen

@Composable
fun RootNavigationGraph(
    navController: NavHostController,
    startDestination: String,
    errorMessage: String?,
    onPositiveClick: () -> Unit = {},
) {
    NavHost(
        navController = navController, startDestination = startDestination
    ) {
        composable(route = Routes.MAIN_SCREEN) {
            MainScreen(navController)
        }
        dialog(route = Routes.ERROR_DIALOG) {
            AlertMessageDialog(
                title = "Something went wrong !",
                message = errorMessage,
                drawable = R.drawable.ic_error_dialog,
                positiveButtonText = "Ok",
                onPositiveClick = {
                    onPositiveClick.invoke()
                },
            )
        }
    }
}

object Routes {
    const val ERROR_DIALOG = "ErrorDialog"
    const val MAIN_SCREEN = "MainScreen"
}