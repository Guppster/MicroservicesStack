package com.ibm.mdm.delivery.tool

import khttp.post
import com.beust.klaxon.*
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

    val propertyList = listOf<String>("washost", "wasport", "mdmversion", "dbhost", "dbport", "platform")

    val parser: Parser = Parser()

    post(COREURL + "/services/register", data = JSONObject(mapOf("Name" to TOOLNAME, "Version" to VERSION, "Url" to URL, "Properties" to propertyList)))

    val runController = runController()

    path("")
    {
        post("/run", "application/json")
        { req, res ->
            val body = req.body()
            val json: JsonObject = parser.parse(StringBuilder(body)) as JsonObject

            //val response = get(COREURL + "entry/" + req.qp("id") + "platform", data = json.get("id"))
            //runController.executeRun()

            "Run Started for ID: " + json.get("id")
        }

        get("/status")
        { req, res ->
            "Ready"
        }

    }
}


fun Request.qp(key: String): String = this.queryParams(key) //adds .qp alias for .queryParams on Request object
