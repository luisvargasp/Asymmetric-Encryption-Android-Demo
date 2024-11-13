package com.lfvp.publickeyappdemo.screens.chatlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lfvp.publickeyappdemo.models.RequestState
import com.lfvp.publickeyappdemo.models.UserInfo
import com.lfvp.publickeyappdemo.screens.EmptyContent
import com.lfvp.publickeyappdemo.screens.LoadingView

@Composable
fun ChatListScreen(navigateToChat: (user: UserInfo) -> Unit,onBackClick: () -> Unit, users: RequestState<List<UserInfo>>) {
    Scaffold(topBar = { ListTopAppBar(onBackClick) }) {


        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            if (users is RequestState.Success) {

                if (users.data.isEmpty()) {
                    EmptyContent("Not users found")
                }else{
                    LazyColumn(modifier = Modifier.padding(start = 0.dp, end = 0.dp)) {

                        items(items=users.data, key = {user->user.id!!}){it->
                            UserCard(it) {
                                navigateToChat(it)
                            }
                        }
                    }
                }


            } else if (users is RequestState.Loading) {
                LoadingView()
            }

        }


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ListTopAppBar(
    onBackClick: () -> Unit
) {
    CenterAlignedTopAppBar(title = {
        Text(text = "Chat", color = Color.White)

    }/*, navigationIcon = {
        IconButton(onClick = {  }) {

            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
        }
    }  */,colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
            ))


}

@Composable
fun UserCard(user: UserInfo, onChatClick: () -> Unit) {

    Card(modifier = Modifier
        .fillMaxWidth().padding(top = 16.dp, bottom = 16.dp, start = 32.dp, end = 32.dp)
        .height(72.dp), onClick = {
        onChatClick()
    }, elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

        shape = CardDefaults.outlinedShape

    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text(text = user.email, modifier = Modifier.padding(start = 16.dp), fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }

    }

}

@Preview
@Composable
fun TopAppBarPreview() {
    ListTopAppBar({})
}

@Preview
@Composable
fun UserCardPreview() {
    UserCard(UserInfo(id = "hf8", email = "luis@gmail.com", publicKey = "123"), {})
}


@Composable
@Preview
private fun ChatListPreview(){

    ChatListScreen({
    },{}, RequestState.Success(
        (1..10).map {
            UserInfo(id=it.toString(), email = "user$it@gmail.com", publicKey = "2")
        }
    ))
}



