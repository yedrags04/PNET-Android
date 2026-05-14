package com.heistcorp.heistcraft.screens.bancos

import com.heistcorp.heistcraft.data.ReservaCreateBody
import com.heistcorp.heistcraft.network.HeistApi

class BancosRepository(
    private val api: HeistApi,
) {
    suspend fun loadRows(): List<BancoRow> {
        val bancos = api.getBancos()
        val reservas = api.getReservas()

        return bancos.map { banco ->
            val reserva = reservas.firstOrNull { it.bankId == banco.id }
            BancoRow(banco = banco, reserva = reserva)
        }
    }

    suspend fun createReserva(body: ReservaCreateBody) {
        api.createReserva(body)
    }

    suspend fun updateReserva(
        reservaId: String,
        body: ReservaCreateBody,
    ) {
        api.updateReserva(reservaId, body)
    }

    suspend fun deleteReserva(reservaId: String) {
        api.deleteReserva(reservaId)
    }
}
