package com.example.loanapp.network

import com.example.loanapp.data.FileResponse
import com.example.loanapp.model.Order
import com.example.loanapp.model.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PrinterApiService {

    @GET("orders")
    suspend fun getAllOrders(@Query("customer_id") customer_id: String,@Query("admin_id") admin_id: String, @Query("status") status: String): List<Order>

    @GET("orders/{id}")
    suspend fun getOrderById(@Path("id") id: String): Order

    @POST("orders")
    @Headers("X-Custom-Header: printerAppSecurity")
    suspend fun createOrder(@Body newOrder: Order): Response<Void>

    @POST("prepare/{id}")
    @Headers("X-Custom-Header: printerAppSecurity")
    suspend fun prepareOrder(@Body user: User,@Path("id") orderId: String, @Query("status") status: String): Response<Void>

    @PUT("orders/{id}")
    suspend fun updateOrder(@Path("id") id: String, @Body updatedOrder: Order): Response<Void>

    @DELETE("orders/{id}")
    suspend fun deleteOrder(@Path("id") id: String): Response<Void>

    @POST("user/login")
    @Headers("X-Custom-Header: printerAppSecurity")
    suspend fun loginUser(@Body user: User): Response<User>

    @Multipart
    @POST("/upload")
    @Headers("X-Custom-Header: printerAppSecurity")
    suspend fun uploadFile(@Part file: MultipartBody.Part): FileResponse

}
