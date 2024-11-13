@file:Suppress("UNREACHABLE_CODE")

package com.lfvp.publickeyappdemo.util.cryto_scripts.encryption

import com.lfvp.publickeyappdemo.util.cryto_scripts.encryption.AES.decryptData
import com.lfvp.publickeyappdemo.util.cryto_scripts.encryption.AES.encryptData
import com.lfvp.publickeyappdemo.util.cryto_scripts.encryption.AES.generateKey
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

object AES {

    const val KEY_SIZE=256 // 128,192,256
    const val TRANSFORMATION="AES"



    fun generateKey(): SecretKey {
        val keyGenerator= KeyGenerator.getInstance("AES")
        keyGenerator.init(KEY_SIZE)
        return keyGenerator.generateKey()

    }


    fun encryptData(plainText:String,secretKey: SecretKey):String{

        val cipher=Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE,secretKey)
        val cipherText=cipher.doFinal(plainText.toByteArray(StandardCharsets.UTF_8))
        return Base64.getEncoder().encodeToString(cipherText)


    }

    fun decryptData(cipherText:String,secretKey: SecretKey):String{
        val cipher=Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE,secretKey)

        val decryptedBytes=cipher.doFinal(Base64.getDecoder().decode(cipherText))
        return String(decryptedBytes,StandardCharsets.UTF_8)

    }


    fun encryptData(bytes:ByteArray,secretKey: SecretKey):ByteArray{

        val cipher=Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE,secretKey)
        val encryptedData=cipher.doFinal(bytes)
        return encryptedData
    }
    fun decryptData(bytes: ByteArray,secretKey: SecretKey):ByteArray{
        val cipher=Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE,secretKey)

        val decryptedBytes=cipher.doFinal(bytes)
        return decryptedBytes

    }
}

fun main(){

    val message="This is a message to be encrypted"


    val secretKey=generateKey()

    //println(Base64.getEncoder().encodeToString(secretKey.encoded)  )
    val encrypted= encryptData(message,secretKey)

    println(encrypted)
    println(decryptData(encrypted,secretKey))







    val originalFile = File("/Users/fervp/Documents/image.png")
    val encryptedBytes= encryptData(originalFile.readBytes(),secretKey)

    val encryptedFile = FileOutputStream("/Users/fervp/Documents/image-encrypted.png")
    encryptedFile.write(encryptedBytes)
    encryptedFile.close()


    val decryptedBytes= decryptData(File("/Users/fervp/Documents/image-encrypted.png").readBytes(),secretKey)



    val decryptedFile = FileOutputStream("/Users/fervp/Documents/image-decrypted.png")
    decryptedFile.write(decryptedBytes)
    decryptedFile.close()




}