package com.example.hazi.model

import kotlinx.serialization.Serializable


@Serializable
data class Weather (
    val id: Long = 0,
    val main: String? = null,
    val description: String? = null,
    val icon: String? = null
)