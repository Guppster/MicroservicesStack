package com.ibm.mdm.delivery.tool

import org.json.JSONObject
import java.lang.Thread.sleep

class runController
{
    var running = false;

    suspend fun executeRun(secondsToWait: Int): JSONObject
    {
        var output : String = ""

        output = output.plus("Started!")

        output = output.plus("Waiting $secondsToWait seconds...")

        running = true

        for (n in 0 until secondsToWait)
        {
            sleep(1000)

            output = output.plus("One second elapsed!")
        }

        running = false

        return JSONObject(mapOf("Log" to output))
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
