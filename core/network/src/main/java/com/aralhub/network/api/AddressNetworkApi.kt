package com.aralhub.network.api

import com.aralhub.network.models.ServerResponse
import com.aralhub.network.models.address.NetworkAddressByIdResponse
import com.aralhub.network.models.address.NetworkAddressByUserIdResponse
import com.aralhub.network.models.address.NetworkAddressRequest
import com.aralhub.network.models.address.NetworkAddressResponse
import com.aralhub.network.models.address.NetworkDeleteAddressResponse
import com.aralhub.network.models.address.NetworkUpdateAddressResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AddressNetworkApi {
    /** Create address */
    @POST("/address")
    suspend fun address(@Body networkAddressRequest: NetworkAddressRequest): Response<ServerResponse<NetworkAddressResponse>>

    @GET("/address/get_adress_by_user_id")
    suspend fun getAddressByUserId(@Query("user_id") userId: Int): Response<ServerResponse<NetworkAddressByUserIdResponse>>

    @GET("/address/get_adress_by_id")
    suspend fun getAddressById(@Query("address_id") addressId: Int): Response<ServerResponse<NetworkAddressByIdResponse>>

    @PUT("/address/update_adress")
    suspend fun updateAddress(@Query("address_id") addressId: Int): Response<ServerResponse<NetworkUpdateAddressResponse>>

    @DELETE("address/delete_adress")
    suspend fun deleteAddress(@Query("address_id") addressId: Int): Response<ServerResponse<NetworkDeleteAddressResponse>>
}