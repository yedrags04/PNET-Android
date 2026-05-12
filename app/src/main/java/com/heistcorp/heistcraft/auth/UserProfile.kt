package com.heistcorp.heistcraft.auth

/** Datos de ficha del operativo (demo en memoria). */
data class UserProfile(
    val fullName: String,
    val nickname: String,
    val yearsExperience: Int,
    val mainSkill: String,
    val otherSkills: String,
)

data class UserSession(
    val email: String,
    val password: String,
    val profile: UserProfile,
)
