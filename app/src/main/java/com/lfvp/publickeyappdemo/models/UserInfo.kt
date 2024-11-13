package com.lfvp.publickeyappdemo.models

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo (
    val id:String?=null,
    val email:String="",
    val publicKey:String=""
)