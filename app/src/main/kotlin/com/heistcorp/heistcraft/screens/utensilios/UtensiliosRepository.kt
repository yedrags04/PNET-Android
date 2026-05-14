package com.heistcorp.heistcraft.screens.utensilios

import com.heistcorp.heistcraft.data.UtensilioApi
import com.heistcorp.heistcraft.network.HeistApi

class UtensiliosRepository(
    private val api: HeistApi,
) {
    suspend fun getUtensilios(): List<UtensilioApi> = api.getUtensilios()
}
