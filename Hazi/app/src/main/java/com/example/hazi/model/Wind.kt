package com.example.hazi.model

import kotlinx.serialization.Serializable

@Serializable
class Wind (
    val speed: Float = 0f,
    val deg: Float = 0f,
    val gust: Float = 0f
)