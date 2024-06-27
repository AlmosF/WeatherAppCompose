package com.example.hazi.model

import kotlinx.serialization.Serializable

@Serializable
data class CitiesItem(
    val name: String,
    val state_code: String,
    val state_name: String,
    val country_code: String,
    val country_name: String
)