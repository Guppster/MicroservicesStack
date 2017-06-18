package com.ibm.mdm.delivery.tool

import khttp.post
import com.beust.klaxon.*
import khttp.get
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.json.JSONObject
import spark.Request
import spark.Spark.*

//Run time variables
val COREURL = "http://hotellnx114.torolab.ibm.com:8080"
val TOOLNAME = "DeliveryTool"
val VERSION = "0.0.1"
val URL = "http://hotellnx114.torolab.ibm.com:59325"
val propertyList = listOf<String>("secondsToWait")

fun main(args: Array<String>)
{
    port(59325)
    exception(Exception::class.java) { e, req, res -> e.printStackTrace() }

    post(COREURL + "/services/register", data = JSONObject(mapOf("Name" to TOOLNAME, "Version" to VERSION, "Url" to URL, "Properties" to propertyList)))

    val runController = runController()

    path("")
    {
        post("/run", "application/json")
        { req, res ->
            val runID = getRunID(req.body())

            val properties = loadProperties(runID)

            print(properties)

            //Run the main program in the background
            async(CommonPool)
            {
                runController.executeRun(properties.get("secondsToWait") as Int)
            }

            res.status(200);
            "Run Started for ID: " + runID
        }

        get("/status")
        { req, res ->
            JSONObject(mapOf("Status" to runController.getStatus()))
        }
    }
}

fun getRunID(body: String): Int
{
    val parser: Parser = Parser()
    val json: JsonObject = parser.parse(StringBuilder(body)) as JsonObject
    return json.get("Id") as Int
}

fun loadProperties(id: Int): JSONObject
{
    return get(COREURL + "/fetch/$id").jsonObject
}

fun Request.qp(key: String): String = this.queryParams(key) //adds .qp alias for .queryParams on Request object
