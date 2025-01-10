package com.example.loanapp.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Order(
    @SerialName("order_id")
    val order_id: String?,
    val order_name: String?,
    val location: String,
    val status: String,
    val customer_id: String,
    val admin_id: String?,
    val orderDate: String,
    val finishedDate: String?,
    val print_detail: PrintDetail
)

