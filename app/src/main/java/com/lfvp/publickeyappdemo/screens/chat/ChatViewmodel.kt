package com.lfvp.publickeyappdemo.screens.chat

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lfvp.publickeyappdemo.models.RequestState
import com.lfvp.publickeyappdemo.data.RemoteRepository
import com.lfvp.publickeyappdemo.models.Message
import com.lfvp.publickeyappdemo.models.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewmodel  @Inject constructor(val repository: RemoteRepository,val savedStateHandle: SavedStateHandle)  :ViewModel() {

    var sendingMessageLoading by mutableStateOf(false)
        private set
    var userToChat by mutableStateOf(UserInfo("","",""))
        private set
    var showError by mutableStateOf<String?>(null)
        private set




    var message by mutableStateOf("")
        private set






    private val _receivedMessages =
        MutableStateFlow<RequestState<List<Message>>>(RequestState.Idle)
    val receivedMessages: StateFlow<RequestState<List<Message>>> = _receivedMessages
    init {
        userToChat= UserInfo(savedStateHandle["id"],savedStateHandle.get<String>("email")!!,
            savedStateHandle["publicKey"]!!)
        getAddresseeInfo()
        getMessages()
    }


    private fun getAddresseeInfo() {
        try {
            val id=userToChat.id
            viewModelScope.launch(Dispatchers.IO) {
                    repository.getAddresseeUserInfo(id!!).collect{
                        if(it is RequestState.Success){
                           userToChat=it.data
                        }
                    }
            }

        }catch (e:Exception){
            //showNothing

        }

    }


    private fun getMessages(){
        _receivedMessages.value= RequestState.Loading
        try {
            val id=userToChat.id

            viewModelScope.launch(Dispatchers.IO) {
                delay(1500)
                repository.getReceivedMessages(id!!).collect{
                    if(it is RequestState.Success){
                        _receivedMessages.value= it
                    }

                }

            }
        }catch (e:Exception){
            showError=e.message

        }
    }

    fun updateMessage(value: String) {
        message=value

    }

    fun sendMessage(){
        sendingMessageLoading=true
        viewModelScope.launch (Dispatchers.IO){
            delay(1000)
            try {
                val result=repository.sendMessage(userToChat,message.trim())
                if(result ){
                    message=""

                }
            }catch (e:Exception){
                showError=e.message

            }
            sendingMessageLoading=false
        }

    }

    fun resetOneTimeEvents() {
        showError=null
    }


}