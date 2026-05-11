package com.heistcorp.heistcraft.network

import com.heistcorp.heistcraft.data.AnimalApi
import com.heistcorp.heistcraft.data.AnimalCreateBody
import com.heistcorp.heistcraft.data.BancoApi
import com.heistcorp.heistcraft.data.ReservaApi
import com.heistcorp.heistcraft.data.ReservaCreateBody
import com.heistcorp.heistcraft.data.SalaApi
import com.heistcorp.heistcraft.data.UtensilioApi
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface HeistApi {
    @GET("api/bancos")
    suspend fun getBancos(): List<BancoApi>

    @GET("api/reservas")
    suspend fun getReservas(): List<ReservaApi>

    @GET("api/reservas/{id}")
    suspend fun getReserva(@Path("id") id: String): ReservaApi

    @POST("api/reservas")
    suspend fun createReserva(@Body body: ReservaCreateBody)

    @PUT("api/reservas/{id}")
    suspend fun updateReserva(@Path("id") id: String, @Body body: ReservaCreateBody)

    @DELETE("api/reservas/{id}")
    suspend fun deleteReserva(@Path("id") id: String)

    @GET("api/utensilios")
    suspend fun getUtensilios(): List<UtensilioApi>

    @GET("api/salas")
    suspend fun getSalas(): List<SalaApi>

    @GET("api/animales")
    suspend fun getAnimales(): List<AnimalApi>

    @POST("api/animales")
    suspend fun createAnimal(@Body body: AnimalCreateBody): AnimalApi

    @PUT("api/animales/{id}")
    suspend fun updateAnimal(@Path("id") id: Int, @Body body: AnimalCreateBody)

    @DELETE("api/animales/{id}")
    suspend fun deleteAnimal(@Path("id") id: Int)
}
