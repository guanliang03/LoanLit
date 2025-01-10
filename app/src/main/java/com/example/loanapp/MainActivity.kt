package com.example.loanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import com.example.loanapp.ui.theme.loanAppTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.loanapp.ui.PrinterAppViewModel
import com.example.loanapp.ui.screen.admin.AdminDashboardScreen
import com.example.loanapp.ui.screen.admin.JobListScreen
import com.example.loanapp.ui.screen.admin.OrderListScreen
import com.example.loanapp.ui.screen.admin.PickupListScreen
import com.example.loanapp.ui.screen.customer.CustomerDashboardScreen
import com.example.loanapp.ui.screen.common.LoginScreen
import com.example.loanapp.ui.screen.common.OrderDetailsScreen
import com.example.loanapp.ui.screen.customer.NewOrderScreen

enum class PrinterAppScreen() {
    Login,
    CustomerDashboard,
    AdminDashboard,
    NewOrder,
    OrderHistory,
    OrderDetails,
    OrderList,
    JobList,
    PickupList
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            loanAppTheme {

                PrinterApp()
            }
        }
    }


}

@Composable
private fun PrinterApp(
    printerAppViewModel: PrinterAppViewModel = viewModel()
) {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

        val uiState by printerAppViewModel.uiState.collectAsState()

        PrinterApplication.appViewModel = printerAppViewModel



        NavHost(
            navController = navController,
            startDestination = PrinterAppScreen.Login.name,
            modifier = Modifier
                .padding(innerPadding).fillMaxSize()
//
        ) {
            composable(route = PrinterAppScreen.Login.name) {

                val context = LocalContext.current
                LoginScreen(
                    onLoginSuccess = { userRole ->

                        if (userRole == "customer") {
                            navController.navigate(
                                PrinterAppScreen.CustomerDashboard.name
                            )
                        }
                        if (userRole == "admin") {
                            navController.navigate(
                                PrinterAppScreen.AdminDashboard.name
                            )
                        }

                    },
                    printerAPpViewModel = printerAppViewModel


                )
            }


            composable(PrinterAppScreen.CustomerDashboard.name) {
                CustomerDashboardScreen(
                    onNewOrderButton = { navController.navigate(PrinterAppScreen.NewOrder.name) },
                    onOrderHistoryButton = { navController.navigate(PrinterAppScreen.OrderHistory.name) },
                    onExtraButton = {},
                    onOrderClick = {orderId ->
                        navController.navigate(route = "order/${orderId}")
                    }
                )
            }
            composable(PrinterAppScreen.NewOrder.name) {
                NewOrderScreen(onBackButton = { navController.popBackStack() })
            }
            composable(PrinterAppScreen.OrderHistory.name) {
                /*TODO OrderHistory*/
            }




            composable(PrinterAppScreen.AdminDashboard.name) {
                AdminDashboardScreen(
                    onOrderListClick = {navController.navigate((PrinterAppScreen.OrderList.name))},
                    onJobListClick = {navController.navigate(PrinterAppScreen.JobList.name)},
                    onPickupListClick = {navController.navigate(PrinterAppScreen.PickupList.name)}
                )
            }
            composable(PrinterAppScreen.JobList.name) {
                JobListScreen(
                    onOrderClick = {orderId ->
                        navController.navigate(route = "order/${orderId}")
                    }
                )
            }
            composable(PrinterAppScreen.PickupList.name) {
                PickupListScreen(
                    onOrderClick = {orderId ->
                        navController.navigate(route = "order/${orderId}")
                    }
                )
            }
            composable(PrinterAppScreen.OrderList.name) {
                OrderListScreen(
                    onOrderClick = {orderId ->
                        navController.navigate(route = "order/${orderId}")
                    }
                )
            }

            composable(
                route = "order/{orderId}",
                arguments = listOf(
                    navArgument("orderId") {
                        type = NavType.StringType
                    }
                )
            )
            {
                val orderId = it.arguments?.getString("orderId") ?: ""

                OrderDetailsScreen(
                    orderId = orderId
                )
            }


            composable(PrinterAppScreen.OrderDetails.name) {
                /*TODO OrderDetails*/
            }

        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PrinterAppPreview() {
    loanAppTheme {
        PrinterApp()
    }
}

//@Preview(showBackground = true, widthDp = 360, heightDp = 640)
//@Composable
//fun LoginPreview() {
//    loanAppTheme {
//        LoginScreen()
//    }
//}
//
//@Preview(showBackground = true, widthDp = 360, heightDp = 640)
//@Composable
//fun CustomerDashboardPreview() {
//    loanAppTheme {
//        CustomerDashboardScreen()
//    }
//}
//
//@Preview(showBackground = true, widthDp = 360, heightDp = 640)
//@Composable
//fun AdminDashboardPreview() {
//    loanAppTheme {
//        AdminDashboardScreen()
//    }
//}