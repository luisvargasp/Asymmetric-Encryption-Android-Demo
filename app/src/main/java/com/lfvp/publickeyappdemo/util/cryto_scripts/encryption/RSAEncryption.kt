package com.lfvp.publickeyappdemo.util.cryto_scripts.encryption

import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import javax.crypto.Cipher

object RSAEncryption {



    private const val KEY_SIZE=1024
    private const val TRANSFORMATION="RSA/ECB/PKCS1Padding"
    private const val SIGNATURE_ALGORITHM="SHA256withRSA"
    fun generateKeyPairBase64():Pair<String,String>{
        val keyPairGenerator= KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(KEY_SIZE)
        val keyPair=keyPairGenerator.generateKeyPair()
        val publicKey=Base64.getEncoder().encodeToString(keyPair.public.encoded)
        val privateKey=Base64.getEncoder().encodeToString(keyPair.private.encoded)
        return Pair(publicKey,privateKey)

    }
    fun generateKeyPair():KeyPair{
        val keyPairGenerator= KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(KEY_SIZE)
        val keyPair=keyPairGenerator.generateKeyPair()
        return keyPair

    }
    fun encryptData(plainText:String,publicKeyBase64:String):String{
        val publicKey= getPublicKeyFromBase64(publicKeyBase64)
        val cipher=Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE,publicKey)
        val cipherText=cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(cipherText)

    }
    fun encryptData(plainText:String,publicKey:Key):String{
        val cipher=Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE,publicKey)
        val cipherText=cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(cipherText)

    }
    fun decryptData(cipherText:String,privateKeyBase64:String):String{
        val privateKey= getPrivateKeyFromBase64(privateKeyBase64)
        val cipher=Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE,privateKey)
        val decryptedBytes=cipher.doFinal(Base64.getDecoder().decode(cipherText))
        return String(decryptedBytes,StandardCharsets.UTF_8)

    }
    fun decryptData(cipherText:String,privateKey: Key):String{
        val cipher=Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE,privateKey)
        val decryptedBytes=cipher.doFinal(Base64.getDecoder().decode(cipherText))
        return String(decryptedBytes,StandardCharsets.UTF_8)
    }

    private fun getPublicKeyFromBase64(base64:String):PublicKey{
        val keyBytes=Base64.getDecoder().decode(base64)
        val keySpec=X509EncodedKeySpec(keyBytes)
        val keyFactory=KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }
    private fun getPrivateKeyFromBase64(base64:String):PrivateKey{
        val keyBytes=Base64.getDecoder().decode(base64)
        val keySpec=PKCS8EncodedKeySpec(keyBytes)
        val keyFactory=KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec)
    }

    fun signData(data:String,privateKey:PrivateKey):String{
        val signature= Signature.getInstance(SIGNATURE_ALGORITHM)
        signature.initSign(privateKey)
        signature.update(data.toByteArray(StandardCharsets.UTF_8))
        val digitalSignature=signature.sign()
        return Base64.getEncoder().encodeToString(digitalSignature)

    }
    fun verify(data: String,signatureBase64:String,publicKey: PublicKey):Boolean{
        val signature= Signature.getInstance(SIGNATURE_ALGORITHM)
        signature.initVerify(publicKey)
        signature.update(data.toByteArray(StandardCharsets.UTF_8))

        val digitalSignature= Base64.getDecoder().decode(signatureBase64)


        return signature.verify(digitalSignature)

    }
}



fun main(){
    val keyPair= RSAEncryption.generateKeyPair()

    val data="Hello"
    val cipherText= RSAEncryption.encryptData(data, keyPair.private)
    println(cipherText)
    val plainText= RSAEncryption.decryptData(cipherText, keyPair.public)
    println(plainText)

    println()

    val signature=RSAEncryption.signData(data,keyPair.private)


    //receiver

    println(RSAEncryption.verify("Hello",signature,keyPair.public))




}