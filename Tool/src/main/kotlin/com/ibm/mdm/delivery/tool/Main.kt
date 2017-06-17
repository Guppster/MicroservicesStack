package com.ibm.mdm.delivery.tool

import khttp.post
import com.beust.klaxon.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.json.JSONObject
import spark.Request
import spark.Spark.*

fun main(args: Array<String>)
{
    exception(Exception::class.java) { e, req, res -> e.printStackTrace() }

    //Run time variables
    val COREURL = "http://hotellnx114.torolab.ibm.com:8080"
    val TOOLNAME = "DeliveryTool"
    val VERSION = "0.0.1"
    val URL = "myurlisthis.com"

    val propertyList = listOf<String>("secondsToWait")


    post(COREURL + "/services/register", data = JSONObject(mapOf("Name" to TOOLNAME, "Version" to VERSION, "Url" to URL, "Properties" to propertyList)))

    val runController = runController()

    path("")
    {
        post("/run", "application/json")
        { req, res ->
            val runID = getRunID(req.body())
            val body = req.body()

            val secondsToWait = 15;

            //secondsToWait = get(COREURL + "entry/" + req.qp("id") + "platform", data = json.get("id"))

            //Run the main program in the background
            async(CommonPool)
            {
                runController.executeRun(secondsToWait)
            }

            "Run Started for ID: " + runID
        }

        get("/status")
        { req, res ->
                JSONObject(mapOf("Status" to runController.getStatus()))
        }
    }
}

fun getRunID(body: String): String
{
    val parser: Parser = Parser()
    val json: JsonObject = parser.parse(StringBuilder(body)) as JsonObject
    return json.get("id") as String
}

fun loadProperties()
{

}

fun Request.qp(key: String): String = this.queryParams(key) //adds .qp alias for .queryParams on Request object
