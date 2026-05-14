package com.heistcorp.heistcraft.data

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class BancoApi(
    @SerializedName("_id") val idRaw: JsonElement,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("reward") val reward: Int,
    @SerializedName("available") val available: Boolean,
    @SerializedName("image") val image: String,
) {
    val id: String get() = idRaw.asMongoIdString()
}

data class ReservaApi(
    @SerializedName("_id") val idRaw: JsonElement,
    @SerializedName("leaderName") val leaderName: String? = null,
    @SerializedName("leaderEmail") val leaderEmail: String? = null,
    @SerializedName("experience") val experience: Int? = null,
    @SerializedName("bankId") val bankId: String? = null,
    @SerializedName("operationDate") val operationDate: String? = null,
    @SerializedName("operationTime") val operationTime: String? = null,
    @SerializedName("teamSize") val teamSize: Int? = null,
    @SerializedName("riskLevel") val riskLevel: String? = null,
    @SerializedName("budget") val budget: Double? = null,
    @SerializedName("equipment") val equipment: String? = null,
    @SerializedName("plan") val plan: String? = null,
    @SerializedName("specialties") val specialties: List<String>? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
) {
    val id: String get() = idRaw.asMongoIdString()
}

data class UtensilioApi(
    @SerializedName("_id") val idRaw: JsonElement,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("precio") val precio: Int,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("imagen") val imagen: String? = null,
) {
    val id: String get() = idRaw.asMongoIdString()
}

data class ReservaCreateBody(
    @SerializedName("leaderName") val leaderName: String,
    @SerializedName("leaderEmail") val leaderEmail: String,
    @SerializedName("experience") val experience: Int,
    @SerializedName("bankId") val bankId: String,
    @SerializedName("operationDate") val operationDate: String,
    @SerializedName("operationTime") val operationTime: String,
    @SerializedName("teamSize") val teamSize: Int,
    @SerializedName("riskLevel") val riskLevel: String,
    @SerializedName("budget") val budget: Double,
    @SerializedName("equipment") val equipment: String,
    @SerializedName("plan") val plan: String,
    @SerializedName("specialties") val specialties: List<String>,
    @SerializedName("status") val status: String,
    @SerializedName("createdAt") val createdAt: String,
)
