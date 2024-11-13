package com.lfvp.publickeyappdemo.screens.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lfvp.publickeyappdemo.models.RequestState
import com.lfvp.publickeyappdemo.data.RemoteRepository
import com.lfvp.publickeyappdemo.models.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatListViewmodel @Inject constructor(val repository: RemoteRepository):ViewModel() {



    private val _users =
        MutableStateFlow<RequestState<List<UserInfo>>>(RequestState.Idle)
    val users: StateFlow<RequestState<List<UserInfo>>> = _users
    init {
        getUsers()

    }


    private fun getUsers(){
        _users.value= RequestState.Loading
        try {
            viewModelScope.launch(Dispatchers.IO) {
                delay(1000)

                repository.getUsers().collect{
                    if(it is RequestState.Success){
                        _users.value=it
                    }

                }
            }
        }catch (e:Exception){

        }


    }





}