package com.lfvp.publickeyappdemo.data

interface AuthRepository {
    fun isAuthenticated():Boolean
    fun getUserId():String
    suspend fun login(email:String,password:String):Boolean
    suspend fun signUp(email:String,password:String):Boolean
}