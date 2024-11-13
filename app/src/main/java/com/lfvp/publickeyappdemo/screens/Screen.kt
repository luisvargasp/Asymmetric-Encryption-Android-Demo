package com.lfvp.publickeyappdemo.screens

import kotlinx.serialization.Serializable

sealed class Screen {


    @Serializable
    data object  Auth: Screen()



    @Serializable
    data object  Home: Screen()



    @Serializable
    data object  ChatList: Screen()



    @Serializable
    data class Chat(val id:String,val email:String,val publicKey:String): Screen()


}