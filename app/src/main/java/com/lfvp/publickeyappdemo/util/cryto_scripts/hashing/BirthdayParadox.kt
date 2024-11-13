package com.lfvp.publickeyappdemo.util.cryto_scripts.hashing

import com.lfvp.publickeyappdemo.util.cryto_scripts.hashing.BirthdayParadox.calculateProbability
import java.math.BigInteger


//Hashing

object BirthdayParadox {


    fun calculateProbability(space:Int,n:Int):Double{
        var value=1.0

        for(i  in 0  until n){
            value=value*(space-i)/space

        }
        return 1-value
    }
}


fun main(){
    println("Probability : ${calculateProbability(365,46)}")




}










/*

  val secureRandom=SecureRandom()


    val resultList= mutableListOf<Boolean>()


    repeat(100000){

        val list= mutableListOf<Int>()

        repeat(23){
            val birthday=(secureRandom.nextInt(1,366))
            list.add(birthday)

        }

        resultList.add(list.size!=list.toSet().size)


    }
    println(resultList.count{ it }.toFloat()/resultList.size)
 */




