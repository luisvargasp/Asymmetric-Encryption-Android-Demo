package com.lfvp.publickeyappdemo.screens

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.lfvp.publickeyappdemo.screens.auth.AuthScreen
import com.lfvp.publickeyappdemo.screens.auth.AuthViewmodel
import com.lfvp.publickeyappdemo.screens.chat.ChatScreen
import com.lfvp.publickeyappdemo.screens.chat.ChatViewmodel
import com.lfvp.publickeyappdemo.screens.chatlist.ChatListScreen
import com.lfvp.publickeyappdemo.screens.chatlist.ChatListViewmodel

@Composable
fun SetupNavigation(
    navController: NavHostController,isAuthenticated:Boolean
) {

    NavHost(
        navController = navController,
        startDestination = if (isAuthenticated) Screen.Home else Screen.Auth, enterTransition = {
            EnterTransition.None
        }, exitTransition = {ExitTransition.None}
    ) {

        composable<Screen.Auth> {

            val authViewmodel: AuthViewmodel = hiltViewModel()
            val keyboardController = LocalSoftwareKeyboardController.current

            val context = LocalContext.current
            val authSuccess = authViewmodel.authSuccess
            val showError = authViewmodel.showError
            LaunchedEffect(authSuccess) {

                authSuccess?.let { value ->
                    if (value) {
                        Log.d("TrackLaunched", "NavigatingUp")
                        navController.navigate(Screen.ChatList) {
                            popUpTo(Screen.Auth) { inclusive = true }
                        }
                        authViewmodel.resetOneTimeEvents()
                    }

                }
            }
            LaunchedEffect(showError) {

                showError?.let { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    authViewmodel.resetOneTimeEvents()

                }
            }


            Box(modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            keyboardController?.hide()
                        }
                    )
                }) {

                AuthScreen(isLogin = authViewmodel.isLogin,
                    onAuthChange = {
                        authViewmodel.toggleAuth()
                        keyboardController?.hide()

                    },
                    emailValue = authViewmodel.email, passwordValue = authViewmodel.password,
                    onEmailChange = { email ->
                        authViewmodel.updateEmail(email)
                    }, onPasswordChange = { password ->
                        authViewmodel.updatePassword(password)
                    }, onButtonClick = {
                        authViewmodel.performAction()
                        keyboardController?.hide()

                    })
                if (authViewmodel.isLoading) {
                    LoadingView()

                }
            }
        }

        navigation<Screen.Home>(startDestination = Screen.ChatList) {


            composable<Screen.ChatList> {
                val chatListViewmodel: ChatListViewmodel = hiltViewModel()


                val context = LocalContext.current
                val users by chatListViewmodel.users.collectAsStateWithLifecycle()


                ChatListScreen({ user ->
                    navController.navigate(Screen.Chat(user.id!!, user.email, user.publicKey))
                }, {
                    if (!navController.popBackStack()) {

                        (context as Activity).moveTaskToBack(true)
                    }

                }, users)

            }
            composable<Screen.Chat> {
                val chatViewmodel: ChatViewmodel = hiltViewModel()
                val context = LocalContext.current
                val showError = chatViewmodel.showError


                val receivedMessages by chatViewmodel.receivedMessages.collectAsStateWithLifecycle()
                LaunchedEffect(showError) {
                    showError?.let { errorMessage ->
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        chatViewmodel.resetOneTimeEvents()

                    }
                }

                ChatScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onMessageChange = { message ->
                        chatViewmodel.updateMessage(message)

                    },
                    onSendMessage = {
                        chatViewmodel.sendMessage()

                    },
                    isSendingMessage = chatViewmodel.sendingMessageLoading,
                    title = chatViewmodel.userToChat.email,
                    messages = receivedMessages,
                    messageValue =
                    chatViewmodel.message

                )

            }

        }

    }

}



