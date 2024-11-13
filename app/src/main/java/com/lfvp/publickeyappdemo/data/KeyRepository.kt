package com.lfvp.publickeyappdemo.data

interface KeyRepository {

     fun existKey(keyAlias:String):Boolean
     fun createKey(keyAlias:String)
     fun getPublicKey(keyAlias:String):String
     fun decryptData(cipherText:String,keyAlias: String):String
     fun encryptData(plainText:String,keyAlias: String):String
     fun encryptDataFromPublicKey(plainText:String,publicKey: String):String
}