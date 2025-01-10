package com.example.loanapp.ui.screen.customer

import NewOrderViewModel
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loanapp.PrinterApplication
import com.example.loanapp.model.Order
import com.example.loanapp.model.PrintDetail
import com.example.loanapp.ui.DropDown
import com.example.loanapp.ui.ErrorScreen
import com.example.loanapp.ui.LoadingScreen
import com.example.loanapp.ui.MinimalDialog
import com.example.loanapp.ui.theme.loanAppTheme

val paperSizes = listOf(
    "Custom" to ("8.27" to "11.69"),
    "A4" to ("8.27" to "11.69"),
    "A3" to ("11.69" to "16.54"),
    "Letter" to ("8.5" to "11.0")
)

@Composable
fun NewOrderScreen(
    modifier: Modifier = Modifier,
    onBackButton: () -> Unit = {},
    orderViewModel: NewOrderViewModel? = viewModel(factory = NewOrderViewModel.Factory)
) {

    Surface(
        modifier = modifier,
    ) {
        var documentName by remember { mutableStateOf("") }
        var noOfPage by remember { mutableStateOf("1") }
        val paperType = listOf("Matte", "Gloss", "Cardstock")
        val paperTypeIndex = remember { mutableStateOf(0) }
        val paperSizeIndex = remember { mutableStateOf(0) }
        var paperWidth by remember { mutableStateOf("") }
        var paperHeight by remember { mutableStateOf("") }
        var isColorPrint by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 30.dp),
            verticalArrangement = Arrangement.Center

        ) {

            CustomInputRow {
                OutlinedTextField(
                    label = { Text("Document Name") },
                    value = documentName,
                    singleLine = true,
                    onValueChange = { documentName = it },
                    modifier = Modifier.weight(3f)
                )

                /* TODO location choice*/
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 20.dp),
                    onClick = {},
                    shape = MaterialTheme.shapes.small,
                    contentPadding = PaddingValues(0.dp),

                    ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn, contentDescription = "Location"
                    )
                }
            }

            CustomInputRow {
                OutlinedTextField(label = { Text("No of copy") },
                    value = noOfPage,
                    onValueChange = { noOfPage = it },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
            }

            CustomInputRow {
                DropDown(paperType,
                    paperTypeIndex,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(top = 4.dp),
                    label = {
                        Text("Paper type", fontSize = 10.sp)
                    })
            }

            CustomInputRow {

                DropDown(paperSizes.map { it.first },
                    paperSizeIndex,
                    modifier = Modifier
                        .weight(7f)
                        .padding(top = 4.dp),
                    label = {
                        Text("Size", fontSize = 10.sp)
                    })
                OutlinedTextField(label = { Text("Width") },
                    value = paperWidth,
                    onValueChange = { paperWidth = it },
                    readOnly = (paperSizeIndex.value != 0),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.weight(5f)
                )
                OutlinedTextField(label = { Text("Height") },
                    value = paperHeight,
                    onValueChange = { paperHeight = it },
                    readOnly = (paperSizeIndex.value != 0),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.weight(5f)
                )
            }

            LaunchedEffect(paperSizeIndex.value) {
                paperWidth = paperSizes[paperSizeIndex.value].second.second
                paperHeight = paperSizes[paperSizeIndex.value].second.second
            }

            CustomInputRow {

                Text(
                    "Color Print", fontSize = 19.sp
                )

                Switch(checked = isColorPrint, onCheckedChange = {
                    isColorPrint = it
                }, thumbContent = if (isColorPrint) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                })
            }

            val context = LocalContext.current




            if (orderViewModel != null) {

                var fileUri by remember { mutableStateOf<Uri?>(null) }

                val filePickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.GetContent()
                ) { uri: Uri? ->
                    fileUri = uri
                    uri?.let{
                        orderViewModel.uploadFile(uri,context)
                    }
                }

                Button(onClick = {
                    filePickerLauncher.launch("*/*")
                }) {
                    Text("Choose File")
                }

                fileUri?.let {
                    Text("Selected File: $it")

                    when(orderViewModel.fileUiState){
                        is NewOrderViewModel.FileUiState.Loading -> {
                            LoadingScreen()
                        }
                        is NewOrderViewModel.FileUiState.Success -> {
                            Text("Successfully upload")
                        }
                        is NewOrderViewModel.FileUiState.Error -> {
                            Text(orderViewModel.fileUiState.toString())
                        }
                        else -> {

                        }
                    }

                }
                var showDialog by remember { mutableStateOf(false) }
                if (showDialog) {


                    MinimalDialog(onDismissRequest = {
                        showDialog = false
                        onBackButton()
                    }) {
                        when (orderViewModel.ordersUiState) {
                            is NewOrderViewModel.OrdersUiState.Loading -> {
                                LoadingScreen()
                            }

                            is NewOrderViewModel.OrdersUiState.OrderModificationSuccess -> {
                                Text(
                                    text = "Order Placed",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth(),
                                    style = MaterialTheme.typography.displaySmall
                                )
                            }

                            is NewOrderViewModel.OrdersUiState.Error -> {
                                (orderViewModel.ordersUiState as NewOrderViewModel.OrdersUiState.Error).exception?.message?.let {
                                    Text(it)
                                    ErrorScreen(
                                        retryAction = {
                                            showDialog = false
                                        }, modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            else -> {}
                        }
                    }
                }

                if(orderViewModel.fileUiState is NewOrderViewModel.FileUiState.Success){
                    Button(
                        onClick = {
                            createNewOrder(
                                documentName = documentName,
                                noOfPage = noOfPage,
                                paperType = paperType,
                                paperTypeIndex = paperTypeIndex,
                                paperWidth = paperWidth,
                                paperHeight = paperHeight,
                                isColorPrint = isColorPrint,
                                orderViewModel = orderViewModel,
                                fileId = (orderViewModel.fileUiState as NewOrderViewModel.FileUiState.Success).fileId
                            )
                            showDialog = true
                        },
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        )

                    ) {
                        Text("Place Order")
                    }

                }
                else{
                    Button(
                        enabled = false,
                        onClick = {},
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        )

                    ) {
                        Text("Place Order")
                    }
                }

            }

        }

    }

}

const val REQUEST_IMAGE_GET = 1





private fun createNewOrder(
    documentName: String,
    noOfPage: String,
    paperType: List<String>,
    paperTypeIndex: MutableState<Int>,
    paperWidth: String,
    paperHeight: String,
    isColorPrint: Boolean,
    orderViewModel: NewOrderViewModel,
    fileId: String
) {
    val order = Order(
        order_name = documentName,
        order_id = "",
        location = "123 Main St",
        status = "pending",
        customer_id = PrinterApplication.instance.getGlobalValue("username") ?: "customer_1",
        admin_id = "",
        orderDate = "2024-12-12",
        finishedDate = "",
        print_detail = PrintDetail(
            print_detail_id = "",
            no_of_copy = noOfPage.toIntOrNull() ?: 1,
            paper_type = paperType[paperTypeIndex.value],
            paper_width = paperWidth.toDouble(),
            paper_height = paperHeight.toDouble(),
            is_color = if (isColorPrint) 1 else 0,
            file_id = fileId
        )
    )
    orderViewModel.createOrder(order)

}

@Composable
fun CustomInputRow(
    modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = modifier
            .padding(vertical = 10.dp)
            .height(60.dp),

        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}


@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun NewOrderPreview() {
    loanAppTheme {
        NewOrderScreen(orderViewModel = null)
    }
}