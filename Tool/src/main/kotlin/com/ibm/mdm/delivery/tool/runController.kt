package com.ibm.mdm.delivery.tool

import java.lang.Thread.sleep

class runController
{
    var secondsToWaitMonitored = 0
    fun executeRun(secondsToWait: Int)
    {
        this.secondsToWaitMonitored = secondsToWait

        println("Started!")
        println("Waiting $secondsToWait seconds...")

        while(secondsToWaitMonitored != 0)
        {
            sleep(1000)
            secondsToWaitMonitored -= 1
            println("One second elapsed!")
        }
    }

    fun getStatus(): String
    {
        if (secondsToWaitMonitored > 0)
        {
            return "Working"
        }
        else
        {
            return "Ready"
        }
    }
}
