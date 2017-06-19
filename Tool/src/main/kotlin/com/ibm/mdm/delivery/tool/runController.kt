package com.ibm.mdm.delivery.tool

import com.beust.klaxon.JSON
import org.json.JSONObject
import java.lang.Thread.sleep

class runController
{
    var running = false;

    suspend fun executeRun(secondsToWait: Int): JSONObject
    {
        var output : JSONObject = JSONObject();

        output.append("log", "Started!")

        output.append("log", "Waiting $secondsToWait seconds...")

        running = true

        for (n in 0 until secondsToWait)
        {
            sleep(1000)

            output.append("log", "One second elapsed!")
        }

        running = false

        return output
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
