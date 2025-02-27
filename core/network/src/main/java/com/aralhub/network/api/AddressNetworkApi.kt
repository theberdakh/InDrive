package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.address.NetworkAddress
import com.aralhub.network.models.address.NetworkDeletedAddress
import com.aralhub.network.requests.address.NetworkAddressRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AddressNetworkApi {

    @POST("/address")
    suspend fun address(@Body networkAddressRequest: NetworkAddressRequest): Response<ServerResponse<NetworkAddress>>

    @GET("/address/get_adress_by_user_id")
    suspend fun getAddressByUserId(@Query("user_id") userId: Int): Response<ServerResponse<List<NetworkAddress>>>

    @GET("/address/get_adress_by_id")
    suspend fun getAddressById(@Query("address_id") addressId: Int): Response<ServerResponse<NetworkAddress>>

    @PUT("/address/update_adress")
    suspend fun updateAddress(@Query("address_id") addressId: Int, @Body updatedAddress: NetworkAddressRequest): Response<ServerResponse<NetworkAddress>>

    @DELETE("address/delete_adress")
    suspend fun deleteAddress(@Query("address_id") addressId: Int): Response<ServerResponse<NetworkDeletedAddress>>
}