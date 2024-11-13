package com.lfvp.publickeyappdemo.data

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import dagger.hilt.android.scopes.ViewModelScoped
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

const val ANDROID_KEYSTORE = "AndroidKeyStore"
const val TRANSFORMATION = "RSA/ECB/PKCS1Padding"
@ViewModelScoped
class AndroidKeyStoreRepository:KeyRepository {
    private val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
        load(null)
    }
    override fun existKey(keyAlias: String): Boolean {
       return keyStore.containsAlias(keyAlias)
    }

    override fun createKey(keyAlias: String) {
        clearAndroidKeyStore()

        val keyPairGenerator = KeyPairGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_RSA,
            ANDROID_KEYSTORE
        )

        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
            .setDigests(KeyProperties.DIGEST_SHA256)
            .setUserAuthenticationRequired(false)
            .build()

        keyPairGenerator.initialize(keyGenParameterSpec)
        keyPairGenerator.generateKeyPair()
    }

    override fun getPublicKey(keyAlias: String):String {
        val privateKeyEntry = keyStore?.getEntry(keyAlias, null) as? KeyStore.PrivateKeyEntry
        val encodedPublicKey = privateKeyEntry?.certificate?.publicKey?.encoded
        return Base64.getEncoder().encodeToString(encodedPublicKey)
    }

    override fun decryptData(cipherText: String, keyAlias: String):String {
        try {
            val cipher = Cipher.getInstance(TRANSFORMATION)
            val privateKeyEntry = keyStore.getEntry(keyAlias, null) as? KeyStore.PrivateKeyEntry
            cipher.init(Cipher.DECRYPT_MODE, privateKeyEntry?.privateKey)
            val decryptedData = cipher.doFinal(Base64.getDecoder().decode(cipherText))
            return String(decryptedData, StandardCharsets.UTF_8)
        }catch (e:Exception){
            return e.message ?: "Error while decrypting"
        }

    }

    override fun encryptData(plainText: String, keyAlias: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val publicKey = keyStore.getCertificate(keyAlias).publicKey
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val cipherText = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(cipherText)

    }
    override fun encryptDataFromPublicKey(plainText: String, publicKey: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val publicKeyBytes = Base64.getDecoder().decode(publicKey)
        val keySpec=X509EncodedKeySpec(publicKeyBytes)
        val keyFactory=KeyFactory.getInstance("RSA")
        val pKey=keyFactory.generatePublic(keySpec)
        cipher.init(Cipher.ENCRYPT_MODE, pKey)
        val cipherText = cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(cipherText)

    }

    private fun clearAndroidKeyStore(){
        keyStore.aliases().asSequence().forEach { it->
            keyStore.deleteEntry(it)
        }
    }
}