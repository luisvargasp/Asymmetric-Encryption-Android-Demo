package com.lfvp.publickeyappdemo.models

import kotlinx.serialization.Serializable

@Serializable
data class Message (
    val id:String?=null,
    val message: String=""
)