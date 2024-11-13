package com.lfvp.publickeyappdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.lfvp.publickeyappdemo.data.KeyRepository
import com.lfvp.publickeyappdemo.screens.SetupNavigation
import com.lfvp.publickeyappdemo.ui.theme.PublicKeyAppDemoTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    @Inject
    lateinit var keyStoreRepository: KeyRepository
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val mainViewmodel: MainViewmodel by viewModels ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{
            mainViewmodel.showingSplash
        }

        enableEdgeToEdge()

        setContent {
            PublicKeyAppDemoTheme {

                if(!mainViewmodel.showingSplash){
                    val isLoggedIn = remember {
                        mainViewmodel.isAuthenticated
                    }

                    navController= rememberNavController()
                    SetupNavigation(navController,isLoggedIn ?:false)
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PublicKeyAppDemoTheme {
    }
}