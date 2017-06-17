package com.ibm.mdm.delivery.tool

import java.lang.Thread.sleep

class runController
{
    var running = false;

    suspend fun executeRun(secondsToWait: Int)
    {
        println("Started!")
        println("Waiting $secondsToWait seconds...")

        running = true

        for (n in 0 until secondsToWait)
        {
            sleep(1000)
            println("One second elapsed!")
        }

        running = false
    }

    fun getStatus(): String
    {
        if (running)
        {
            return "Working"
        }

        return "Ready"
    }
}
