package com.lfvp.publickeyappdemo.util.cryto_scripts.hashing

import java.math.BigInteger
import java.security.MessageDigest


object Hashing {


    fun md5(message:String):String
    {
        val messageDigest=MessageDigest.getInstance("MD5")
        var hexHash=""

       val bytes=  messageDigest.digest(message.toByteArray())
        hexHash=BigInteger(1,bytes).toString(16)
        while (hexHash.length<32){
            hexHash= "0$hexHash"
        }
        return hexHash
    }

    fun sha256(message:String):String
    {
        val messageDigest=MessageDigest.getInstance("SHA-256")
        var hexHash=""

        val bytes=  messageDigest.digest(message.toByteArray())
        hexHash=BigInteger(1,bytes).toString(16)
        while (hexHash.length<64){
            hexHash= "0$hexHash"
        }
        return hexHash
    }

}

fun main(){
    val message="Password"
    println(Hashing.md5(message))//128 bits output
    println(Hashing.sha256(message))//256 bits output 128

    println()
    println()
    println(BigInteger.valueOf(2).pow(64)
        .divide(BigInteger.valueOf(200_000_000))// hashes per second rate
        .divide(BigInteger.valueOf(1_000_000_000))// # of machines
        .divide(BigInteger.valueOf(3600*24*365))// to years
      //  .divide(BigInteger.valueOf(14_000_000_000))// universe lifespan
    )






}









/*

    println(BigInteger.valueOf(2).pow(256)
        .divide(BigInteger.valueOf(50_000_000))// hashes per second rate
        .divide(BigInteger.valueOf(1_000_000_000))// # of machines
        .divide(BigInteger.valueOf(3600*24*365))// to years
        .divide(BigInteger.valueOf(14_000_000_000))// universe lifespan
    )



    */
