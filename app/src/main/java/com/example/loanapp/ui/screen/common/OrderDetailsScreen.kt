package com.example.loanapp.ui.screen.common

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.loanapp.model.Order
import com.example.loanapp.model.PrintDetail
import com.example.loanapp.ui.ErrorScreen
import com.example.loanapp.ui.LoadingScreen
import com.example.loanapp.ui.theme.loanAppTheme
import java.net.URL


@Composable
fun OrderDetailsScreen(
    modifier: Modifier = Modifier,
    orderId: String,
    viewModel: OrderDetailsViewModel? = viewModel(factory = OrderDetailsViewModel.Factory),
) {

    viewModel?.let{

        LaunchedEffect(Unit) {
            viewModel.getOrder(orderId)
        }

        val orderUiState = viewModel.orderUiState

        when (orderUiState) {
            is OrderDetailsViewModel.OrderUiState.Loading -> {
                LoadingScreen(modifier = modifier.fillMaxSize())
            }

            is OrderDetailsViewModel.OrderUiState.Success -> {
                OrderDetails(orderUiState.order)
            }

            is OrderDetailsViewModel.OrderUiState.Error -> {
                orderUiState.exception?.message?.let {
                    Text(it)
                    ErrorScreen(
                        retryAction = {viewModel.getOrder(orderId)}
                    )
                }
            }

            else -> {}
        }

    }


}

@Composable
fun OrderDetails(
    order: Order
){
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = order.order_name ?: "No Document Name",
            style = MaterialTheme.typography.headlineLarge
        )


        AsyncImage(
            "http://157.245.204.192:3000/download/${order.print_detail.file_id}/thumbnail",
            "Test",
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.FillHeight
        )

        val status = when(order.status){
            "pickup" -> "Ready for pickup"
            "pending" -> "Processing Order"
            "preparing" -> "Preparing Order"
            else -> order.status
        }

        Text(
            text = "orderer by: ${order.customer_id}",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Status: ${status}",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Location: ${order.location}",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Date ordered ${order.orderDate}",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = order.print_detail.paper_type,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "${order.print_detail.paper_width} x ${order.print_detail.paper_height}",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Colored:" + when{
                (order.print_detail.is_color == 1) ->  "Yes"
                else -> "No"
            },
            style = MaterialTheme.typography.headlineMedium
        )

        order.admin_id?.let{
            Text(
                text = "Order prepare by: ${order.admin_id}",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        val context = LocalContext.current

        Button(
            onClick = {
                openFile(
                    "http://157.245.204.192:3000/download/${order.print_detail.file_id}",
                    context = context
                )
            }
        )
        {
            Text("Open File")
        }





    }
}

fun openFile(
    uri: String,
    context: Context
){
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    context.startActivity(browserIntent)

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

@Preview(showBackground = true)
@Composable
fun OrderDetailsScreenPreview(){
    loanAppTheme {
        OrderDetails(order = orderSample)
    }
}