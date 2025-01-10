package com.example.loanapp.ui.screen.customer

import CustomerDashboardViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loanapp.PrinterApplication
import com.example.loanapp.model.Order
import com.example.loanapp.ui.CircleButtonDashboard
import com.example.loanapp.ui.ErrorScreen
import com.example.loanapp.ui.LoadingScreen
import com.example.loanapp.ui.theme.loanAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDashboardScreen(
    modifier: Modifier = Modifier,
    onNewOrderButton: () -> Unit = {},
    onOrderHistoryButton: () -> Unit = {},
    onExtraButton: () -> Unit = {},
    orderViewModel: CustomerDashboardViewModel? = viewModel(factory = CustomerDashboardViewModel.Factory),
    onOrderClick: (String) -> Unit = {}
) {
    Surface(
        modifier = modifier
            .padding(0.dp, 80.dp, 0.dp, 0.dp)
            .fillMaxHeight()
    ) {


        Column(
            modifier = Modifier.fillMaxHeight()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                CircleButtonDashboard(
                    "New Order",
                    modifier = Modifier.weight(1f),
                    onButtonClick = onNewOrderButton
                )
                CircleButtonDashboard(
                    "Order History",
                    modifier = Modifier.weight(1f),
                    onButtonClick = onOrderHistoryButton
                )
                CircleButtonDashboard(
                    "Extra",
                    modifier = Modifier.weight(1f),
                    onButtonClick = onExtraButton
                )


            }

            orderViewModel.let {
                val name = PrinterApplication.appViewModel.get_user()?.name

                Text("Hello $name test")
                Row() {
                    Text(
                        text = "Active order:",
                        modifier = Modifier.padding(10.dp, 20.dp, 10.dp, 0.dp)
                    )


                }
            }

            orderViewModel?.let {

            val isRefreshing by remember{ mutableStateOf(false)}
            val onRefresh = {orderViewModel.getAllOrdersByCustId(
                PrinterApplication.appViewModel.get_user()?.username ?: ""
            )}

            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = onRefresh,
                modifier = modifier
            ) {

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .height(IntrinsicSize.Max)
                        .background(MaterialTheme.colorScheme.secondaryContainer),


                    ) {

                        LaunchedEffect(Unit) {
                            orderViewModel.getAllOrdersByCustId(
                                PrinterApplication.appViewModel.get_user()?.username ?: ""
                            )
                        }
                        MyActiveOrder(
                            ordersUiState = orderViewModel.ordersUiState,
                            retryAction = {
                                orderViewModel.getAllOrdersByCustId(
                                    PrinterApplication.appViewModel.get_user()?.username ?: ""
                                )
                            },
                            onOrderClick = onOrderClick
                        )
                    }
                }
            }


        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun CustomerDashboardPreview() {
    loanAppTheme {
        CustomerDashboardScreen(orderViewModel = null)
    }
}


@Composable
private fun MyActiveOrder(
    ordersUiState: CustomerDashboardViewModel.OrdersUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onOrderClick: (String) -> Unit
) {
    when (ordersUiState) {
        is CustomerDashboardViewModel.OrdersUiState.Loading -> {
            LoadingScreen(modifier = modifier.fillMaxSize())
        }

        is CustomerDashboardViewModel.OrdersUiState.Success -> {
            if (ordersUiState.orders.isNotEmpty()){
                OrderCards(ordersUiState.orders, onOrderClick = onOrderClick)
            }
            else{
                Text("No Active Order", textAlign = TextAlign.Center, fontSize = 30.sp, modifier = Modifier.fillMaxWidth().height(200.dp))
            }
        }

        is CustomerDashboardViewModel.OrdersUiState.Error -> {
            ordersUiState.exception?.message?.let {
                Text(it)
                ErrorScreen(
                    retryAction = retryAction
                )
            }
        }

        else -> {}
    }
}

@Composable
private fun OrderCards(
    orders: List<Order>,
    onOrderClick: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .verticalScroll(rememberScrollState())

    ) {
        orders.forEach {
            OrderCard(it, onOrderClick = onOrderClick)
        }
    }
}

@Composable
private fun OrderCard(
    order: Order,
    onOrderClick: (String) -> Unit
) {

    val orderDescription =
        "${order.print_detail.paper_type} - ${order.print_detail.paper_width}x${order.print_detail.paper_height}"

    val status = when(order.status){
        "pickup" -> "Ready for pickup"
        "pending" -> "Processing Order"
        "preparing" -> "Preparing Order"
        else -> order.status
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(20.dp, 20.dp, 20.dp, 0.dp)
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                onOrderClick(order.order_id ?: "")
            },
    ) {

        Text(
            text = order.order_name ?: "Test",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(20.dp),
        )

        Text(
            text = orderDescription,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp),
            fontSize = 12.sp
        )
        Text(
            text = status,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            style = MaterialTheme.typography.bodyMedium
        )

    }
}
