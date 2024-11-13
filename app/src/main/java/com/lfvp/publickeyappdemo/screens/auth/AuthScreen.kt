package com.lfvp.publickeyappdemo.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lfvp.publickeyappdemo.util.singleClick

@Composable
fun AuthScreen(
    isLogin:Boolean,
    onAuthChange:()->Unit,
    emailValue:String,
    passwordValue:String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onButtonClick:()->Unit

) {



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer( modifier = Modifier.fillMaxHeight(0.15f))
    

        Text(
            text = if(isLogin)"Enter your account" else "Create your account",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(36.dp))

        OutlinedTextField(value = emailValue, onValueChange = {email->
            onEmailChange(email)

        }, label = { Text(text = "Email") }, textStyle = MaterialTheme.typography.labelMedium.copy(color = Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            , placeholder = {Text(text = "Email") }
        )
        Spacer(modifier = Modifier.height(36.dp))

        OutlinedTextField(value = passwordValue, onValueChange = {password->
            onPasswordChange(password)

        }, label = { Text(text = "Password") }, visualTransformation = PasswordVisualTransformation(),textStyle = MaterialTheme.typography.labelMedium.copy(color = Color.Black),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            , placeholder = {Text(text = "Password") }
            )

        Spacer(modifier = Modifier.height(72.dp))


        Button(onClick = singleClick{onButtonClick()}, modifier = Modifier.fillMaxWidth(0.4f)) {
            Text(if (isLogin)"Log in" else "Sign up")
        }
        TextButton(onClick = singleClick{
            onAuthChange()
        }) {
            Text(if(isLogin) "Create an account" else "Log in")
        }



    }


}

@Preview
@Composable
fun AuthPreview(

) {
    AuthScreen(isLogin = true, onAuthChange = {}, emailValue = "example@gmail.com", passwordValue = "123456", onPasswordChange = {}, onEmailChange = {}, onButtonClick = {})
}


