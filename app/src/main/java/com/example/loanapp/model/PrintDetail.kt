package com.example.loanapp.model

import kotlinx.serialization.Serializable

@Serializable
data class PrintDetail(
    val print_detail_id: String,
    val no_of_copy: Int,
    val paper_type: String,
    val paper_width: Double,
    val paper_height: Double,
    val is_color: Int,
    val file_id: String
)