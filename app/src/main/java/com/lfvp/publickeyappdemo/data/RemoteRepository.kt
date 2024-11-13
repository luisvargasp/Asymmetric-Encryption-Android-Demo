package com.lfvp.publickeyappdemo.data

import com.lfvp.publickeyappdemo.models.RequestState
import com.lfvp.publickeyappdemo.models.Message
import com.lfvp.publickeyappdemo.models.UserInfo
import kotlinx.coroutines.flow.Flow
import java.security.PublicKey

interface RemoteRepository {
    suspend fun createUserInDB(userInfo: UserInfo):Boolean
    suspend fun updatePublicKey(publicKey: String):Boolean

    fun getUsers():Flow<RequestState<List<UserInfo>>>

    suspend fun sendMessage(addresseeUser:UserInfo,message:String):Boolean
    fun getReceivedMessages(userId: String):Flow<RequestState<List<Message>>>
    fun getAddresseeUserInfo(userId: String):Flow<RequestState<UserInfo>>

}