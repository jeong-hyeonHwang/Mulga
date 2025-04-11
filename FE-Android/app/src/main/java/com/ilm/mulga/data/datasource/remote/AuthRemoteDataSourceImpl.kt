package com.ilm.mulga.data.datasource.remote

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthRemoteDataSourceImpl (
    private val firebaseAuth: FirebaseAuth
) : AuthRemoteDataSource {

    override suspend fun signInWithCredential(credential: AuthCredential): FirebaseUser? {
        return suspendCoroutine { continuation ->
            firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener { authResult ->
                    continuation.resume(authResult.user)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }
}