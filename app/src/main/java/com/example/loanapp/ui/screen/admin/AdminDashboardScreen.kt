package com.example.loanapp.ui.screen.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.loanapp.ui.CircleButtonDashboard
import com.example.loanapp.ui.ErrorScreen
import com.example.loanapp.ui.LoadingScreen
import com.example.loanapp.ui.theme.loanAppTheme

@Composable
fun AdminDashboardScreen(
    modifier: Modifier = Modifier,
    onOrderListClick: () -> Unit = {},
    onJobListClick: () -> Unit = {},
    onPickupListClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier.padding(0.dp, 80.dp, 0.dp, 0.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                CircleButtonDashboard("Order List", modifier = Modifier.weight(1f), onButtonClick = onOrderListClick)
                CircleButtonDashboard("Job List", modifier = Modifier.weight(1f), onButtonClick = onJobListClick)
                CircleButtonDashboard("Pick up List", modifier = Modifier.weight(1f), onButtonClick = onPickupListClick)

            }
        }
    }
}
@Preview(showBackground = true,widthDp = 360, heightDp = 640)
@Composable
fun AdminDashboardPreview() {
    loanAppTheme {
        AdminDashboardScreen()
    }
}

