package com.ibm.mdm.delivery.tool

import java.lang.Thread.sleep

class runController
{
    fun executeRun()
    {
        val secondsToWait = 3
        println("Started!")
        println("Waiting $secondsToWait seconds...")
        for (n in 0 until secondsToWait) {
            sleep(1000)
            println("One second elapsed!")
        }
    }
}

