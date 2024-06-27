package com.example.hazi.model

import kotlinx.serialization.Serializable

@Serializable
data class APODResult(
    val copyright: String,
    val date: String,
    val explanation: String,
    val hdurl: String,
    val media_type: String,
    val service_version: String,
    val title: String,
    val url: String
)