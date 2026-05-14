package com.heistcorp.heistcraft.screens.reservas

import com.heistcorp.heistcraft.data.BancoApi
import com.heistcorp.heistcraft.data.ReservaApi
import com.heistcorp.heistcraft.data.ReservaCreateBody
import com.heistcorp.heistcraft.network.HeistApi

class ReservasRepository(
    private val api: HeistApi,
) {
    suspend fun getReservas(): List<ReservaApi> = api.getReservas()

    suspend fun getReserva(reservaId: String): ReservaApi = api.getReserva(reservaId)

    suspend fun getBancos(): List<BancoApi> = api.getBancos()

    suspend fun createReserva(body: ReservaCreateBody) = api.createReserva(body)

    suspend fun updateReserva(
        reservaId: String,
        body: ReservaCreateBody,
    ) = api.updateReserva(reservaId, body)

    suspend fun deleteReserva(reservaId: String) = api.deleteReserva(reservaId)
}
