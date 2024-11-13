package com.lfvp.publickeyappdemo.data

import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@ViewModelScoped
class FirebaseAuthRepository @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {
    override  fun isAuthenticated(): Boolean {
        return auth.currentUser!=null
    }

    override fun getUserId(): String {
       return auth.currentUser?.uid!!
    }

    override suspend fun login(email: String, password: String): Boolean {

        return suspendCancellableCoroutine { next ->
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener() { task ->

                if (task.isSuccessful) {
                    next.resume(true)

                }
                else{
                    task.exception?.let {
                        exception ->
                        next.resumeWithException(exception)
                    }?: next.resume(false)
                }

            }


        }
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return suspendCancellableCoroutine { next ->
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {  task ->

                if (task.isSuccessful) {
                    next.resume(true)

                }
                else{
                    task.exception?.let {
                            exception ->
                        next.resumeWithException(exception)
                    }?: next.resume(false)
                }

            }

        }


    }


}