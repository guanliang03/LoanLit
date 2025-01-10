package com.example.loanapp.ui.screen.admin

import OrderListViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loanapp.PrinterApplication
import com.example.loanapp.model.Order
import com.example.loanapp.model.PrintDetail
import com.example.loanapp.ui.ErrorScreen
import com.example.loanapp.ui.LoadingScreen
import com.example.loanapp.ui.theme.loanAppTheme

@Composable
fun OrderListScreen(
    modifier: Modifier = Modifier,
    viewModel: OrderListViewModel = viewModel(factory = OrderListViewModel.Factory),
    onOrderClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.padding(0.dp, 80.dp, 0.dp, 0.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {


        viewModel.let {

            val user = PrinterApplication.appViewModel.get_user()

            val refreshData = { viewModel.getAllOrdersByStatus("pending") }

            CustomPullToRefreshBox(
                onRefresh = refreshData
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .height(IntrinsicSize.Max)
                        .heightIn(min =  800.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                ) {
                    LaunchedEffect(Unit) {
                        refreshData()
                    }

                    MyPendingOrder(
                        ordersUiState = viewModel.ordersUiState,
                        retryAction = {},
                        onOrderClick = onOrderClick,
                        onPrepareClick = { id ->
                            viewModel.prepareOrder(user, id, "preparing")
                            refreshData()
                        },
                    )
                }


            }

        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun OrderListScreenPreview() {
    loanAppTheme {
        OrderListScreen()
    }
}

@Composable
private fun OrderCard(
    order: Order,
    onOrderClick: (String) -> Unit,
    onPrepareClick: (String) -> Unit
) {

    val orderDescription =
        "${order.print_detail.paper_type} - ${order.print_detail.paper_width}x${order.print_detail.paper_height}"


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(20.dp, 0.dp, 20.dp, 0.dp)
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                onOrderClick(order.order_id ?: "")
            },
    ) {
        HorizontalDivider()
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

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 15.dp)
                .size(40.dp),
        ) {
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(40.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
            ) {

            }
            Text("X", modifier = Modifier.align(Alignment.Center))
        }



        Button(
            modifier = Modifier.align(Alignment.BottomEnd),
            shape = RoundedCornerShape(10.dp),
            onClick = { onPrepareClick(order.order_id ?: "") }
        ) {
            Text("Prepare")
        }

    }
}

private val orderSample: Order = Order(
    order_id = "id_rjqw1l87r",
    order_name = "Test Document",
    location = "123 Main St",
    status = "pending",
    customer_id = "customer_1",
    admin_id = null,
    orderDate = "2024-12-12",
    finishedDate = null,
    print_detail = PrintDetail(
        print_detail_id = "id_rkusze03k",
        no_of_copy = 1,
        paper_type = "Glossy",
        paper_width = 11.69,
        paper_height = 11.69,
        is_color = 0,
        file_id = "file-01"
    )
)

@Composable
private fun MyPendingOrder(
    ordersUiState: OrderListViewModel.OrdersUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onOrderClick: (String) -> Unit,
    onPrepareClick: (String) -> Unit
) {
    when (ordersUiState) {
        is OrderListViewModel.OrdersUiState.Loading -> {
            LoadingScreen(modifier = modifier.fillMaxSize())
        }

        is OrderListViewModel.OrdersUiState.Success -> {
            if (ordersUiState.orders.isNotEmpty()){

                OrderCards(
                    ordersUiState.orders,
                    onOrderClick = onOrderClick,
                    onPrepareClick = onPrepareClick
                )
            }else{
                Text("No Active Order", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }

        is OrderListViewModel.OrdersUiState.Error -> {
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
    onOrderClick: (String) -> Unit,
    onPrepareClick: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .verticalScroll(rememberScrollState())

    ) {
        orders.forEach {
            OrderCard(it, onOrderClick = onOrderClick, onPrepareClick = onPrepareClick)
        }
    }
}