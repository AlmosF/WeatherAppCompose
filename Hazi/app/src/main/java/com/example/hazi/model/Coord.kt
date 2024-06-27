package com.example.hazi.model

import kotlinx.serialization.Serializable

@Serializable
data class Coord(
    var lon: Float = 0f,
    var lat: Float = 0f
)