package com.lfvp.publickeyappdemo.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lfvp.publickeyappdemo.models.RequestState
import com.lfvp.publickeyappdemo.models.Message
import com.lfvp.publickeyappdemo.screens.EmptyContent
import com.lfvp.publickeyappdemo.screens.LoadingView
import com.lfvp.publickeyappdemo.util.singleClick

@Composable
fun ChatScreen(messageValue:String, onSendMessage:()->Unit, onMessageChange: (String) -> Unit, title:String, onBackClick: () -> Unit, messages: RequestState<List<Message>>, isSendingMessage:Boolean) {
    Scaffold(topBar = { ChatTopAppBar(title=title, onBackClick = onBackClick) }) {


        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {

            Row(modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(enabled = !isSendingMessage,
                    modifier = Modifier.weight(1f).padding(end = 8.dp),value = messageValue, onValueChange = { message->
                        onMessageChange(message)


                }, label = { Text(text = "Message") }, textStyle = MaterialTheme.typography.labelMedium.copy(color = Color.Black),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    , placeholder = {Text(text = "Write your message") }
                )

                IconButton(modifier = Modifier
                    .size(48.dp)
                    .padding(start = 8.dp)
                    .alpha(if(messageValue.trim().isNotBlank()) 1.0f else 0.5f)
                    .background(MaterialTheme.colorScheme.primary), onClick = singleClick{
                        if(!isSendingMessage){
                            onSendMessage()
                        }

                }, enabled = messageValue.trim().isNotBlank()) {
                    if(isSendingMessage){
                        CircularProgressIndicator( modifier = Modifier.size(36.dp), color = MaterialTheme.colorScheme.onPrimary)
                    }else{
                        Icon(imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = null, tint =MaterialTheme.colorScheme.onPrimary )

                    }

                }
            }

            if (messages is RequestState.Success) {

                if (messages.data.isEmpty()) {
                    EmptyContent("No received messages found")
                }else{
                    LazyColumn(modifier = Modifier.padding(start = 0.dp, end = 0.dp)) {

                        items(items=messages.data, key = {message->message.id!!}){
                            MessageCard((it))
                        }
                    }
                }


            } else if (messages is RequestState.Loading) {
                LoadingView()
            }

        }


    }

}




@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun ChatTopAppBar(
    onBackClick: () -> Unit,
    title:String
) {
    CenterAlignedTopAppBar(title = {
        Text(text = title, color = Color.White)

    }, navigationIcon = {
        IconButton(onClick = {
            onBackClick()
        }) {

            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
        }
    }  ,colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary
    ))

}
@Composable
fun MessageCard(message: Message) {

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp, bottom = 16.dp, start = 32.dp, end = 32.dp)
        .height(72.dp), onClick = {

    }, elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),

        shape = CardDefaults.outlinedShape

    ) {

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text(text =message.message, modifier = Modifier.padding(start = 16.dp), fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }

    }

}

@Composable
@Preview
fun ChatTopAppBarPreview(){
    ChatTopAppBar({},"User x")
}

@Composable
@Preview
fun MessageCardPreview(){
    MessageCard(Message(message = "Hello my friend"))
}
@Composable
@Preview
private fun ChatPreview(){

    ChatScreen("Hello Mr",{},{},"USERX",{}, RequestState.Success(
        (1..10).map {
            Message(id=it.toString(), message = "Greeting $it")
        }
    ),false)
}
