package com.lfvp.publickeyappdemo.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lfvp.publickeyappdemo.models.RequestState
import com.lfvp.publickeyappdemo.models.Message
import com.lfvp.publickeyappdemo.models.UserInfo
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@ViewModelScoped
class FirebaseDatabaseRepository @Inject constructor(
    val authRepository: AuthRepository,
    val firebaseDatabase: FirebaseDatabase,
    val keyRepository: KeyRepository
) : RemoteRepository {
    override suspend fun createUserInDB(userInfo: UserInfo): Boolean {

        return suspendCancellableCoroutine<Boolean> { next ->

            firebaseDatabase.getReference("users").child(authRepository.getUserId()!!)
                .setValue(userInfo)
                .addOnSuccessListener {
                    next.resume(true)

                }.addOnFailureListener { e ->
                    next.resumeWithException(e)
                }

        }

    }

    override suspend fun updatePublicKey(publicKey: String): Boolean {
        return suspendCancellableCoroutine<Boolean> { next ->

            firebaseDatabase.getReference("users").child(authRepository.getUserId()).child("publicKey")
                .setValue(publicKey)
                .addOnSuccessListener {
                    next.resume(true)

                }.addOnFailureListener { e ->
                    next.resumeWithException(e)
                }

        }
    }

    override fun getUsers(): Flow<RequestState<List<UserInfo>>> {
        return callbackFlow {
            val reference = firebaseDatabase.getReference("users")
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = mutableListOf<UserInfo>()
                    try {
                        if (snapshot.exists()) {
                            snapshot.children.map { child ->
                                child.getValue(UserInfo::class.java)?.copy(id = child.key)

                            }.filter { userInfo -> userInfo?.id != authRepository.getUserId()}
                                .forEach { it ->
                                    it?.let { user ->
                                        users.add(user)
                                    }
                                }
                        }
                        trySend(RequestState.Success(users))

                    } catch (e: Exception) {
                        trySend(RequestState.Error(e))

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(RequestState.Error(error.toException()))
                }

            }
            reference.addValueEventListener(listener)
            awaitClose {
                reference.removeEventListener(listener)
            }

        }

    }

    override suspend fun sendMessage(addresseeUser: UserInfo, message: String): Boolean {

        return suspendCancellableCoroutine<Boolean> { next ->

            firebaseDatabase.getReference("messages").child(addresseeUser.id!!)
                .child(authRepository.getUserId())
                .push()
                .setValue(
                    Message(
                        message = keyRepository.encryptDataFromPublicKey(
                            plainText = message,
                            publicKey = addresseeUser.publicKey
                        )
                    )
                )
                .addOnSuccessListener {
                    next.resume(true)

                }.addOnFailureListener { e ->
                    next.resumeWithException(e)
                }

        }

    }

    override fun getReceivedMessages(userId: String): Flow<RequestState<List<Message>>> {
        return callbackFlow {
            val reference = firebaseDatabase.getReference("messages").child(authRepository.getUserId())
                .child(userId)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val messages = mutableListOf<Message>()
                    try {
                        if (snapshot.exists()) {
                            snapshot.children.map { child ->
                                child.getValue(Message::class.java)?.let {
                                    val msg = it.copy(
                                        id = child.key,
                                        message = keyRepository.decryptData(
                                            it.message,
                                            authRepository.getUserId()
                                        )
                                    )
                                    messages.add(msg)
                                }

                            }
                        }
                        trySend(RequestState.Success(messages))

                    } catch (e: Exception) {
                        trySend(RequestState.Error(e))

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(RequestState.Error(error.toException()))
                }

            }
            reference.addValueEventListener(listener)
            awaitClose {
                reference.removeEventListener(listener)
            }

        }
    }

    override fun getAddresseeUserInfo(userId: String): Flow<RequestState<UserInfo>> {
        return callbackFlow {
            val reference = firebaseDatabase.getReference("users").child(userId)
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    try {
                        if (snapshot.exists()) {
                            val user =
                                snapshot.getValue(UserInfo::class.java)?.copy(id = snapshot.key)
                            trySend(RequestState.Success(user!!))
                        }

                    } catch (e: Exception) {
                        trySend(RequestState.Error(e))

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(RequestState.Error(error.toException()))
                }

            }
            reference.addValueEventListener(listener)
            awaitClose {
                reference.removeEventListener(listener)
            }

        }

    }
}