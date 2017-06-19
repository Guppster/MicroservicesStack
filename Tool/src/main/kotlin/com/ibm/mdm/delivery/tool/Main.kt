package com.ibm.mdm.delivery.tool

import khttp.post
import com.beust.klaxon.*
import khttp.get
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import org.json.JSONObject
import spark.Request
import spark.Spark.*
import java.net.MalformedURLException
import kotlin.system.exitProcess

//Constants
val VERSION = "0.0.1"
val propertyList = listOf<String>("secondsToWait")
val TOOLNAME = "DeliveryTool"

//Run time variables
var COREURL = "MDMCore.w3ibm.bluemix.com"
var PORT = 4567
var URL= "localhost"

// CORE: "http://hotellnx114.torolab.ibm.com:8080"
// ME:   "http://hotellnx114.torolab.ibm.com:59325"

fun main(args: Array<String>)
{
    //Parse commandline arguments
    parseArgs(args)

    //Specify the port for this service
    port(PORT)

    exception(Exception::class.java) { e, req, res -> e.printStackTrace() }

    //Initialize the tool
    val runController = runController()

    //The root path
    path("")
    {
        //The run path
        post("/run", "application/json")
        { req, res ->

            //Extract the run ID from the body of the request
            val runID = getRunID(req.body())

            //Fetch the properties from Core with the runID
            val properties = loadProperties(runID)

            //Print out the properties to make sure we got them
            print(properties)

            //Run the main program and handle results in the background
            async(CommonPool)
            {
                //Execute and collect results
                val result = runController.executeRun(properties.get("secondsToWait") as Int)

                //Tell Core that we're done and send results
                post(COREURL + "/run/finish", data = JSONObject(mapOf("id" to runID, "results" to result)))
            }

            //Send back a OK status with a message indicating run started
            res.status(200);
            "Run Started for ID: " + runID
        }

        //The status path
        get("/status")
        { req, res ->
            JSONObject(mapOf("Status" to runController.getStatus()))
        }
    }

    try
    {
        //Register the service with Core. We are ready for work
        post(COREURL + "/services/register", data = JSONObject(mapOf("Name" to TOOLNAME, "Version" to VERSION, "Url" to URL, "Properties" to propertyList)))
    }catch(e: MalformedURLException)
    {
        println("Error: Invalid core URL or port")
        printUsage()
        exitProcess(1)
    }
}

/**
 * Parse command line arguments for COREURL, SERVICEURL and PORT
 */
fun parseArgs(args: Array<String>)
{
    if(args.size != 3)
        printUsage();

    COREURL = args[0]
    URL = args[1]
    PORT = args[2].toInt()
}

/**
 * Tells people how to start Tool service
 */
fun printUsage()
{
    println("Usage: ... [CORE-URL:CORE-PORT] [SERVICE-URL] [SERVICE-PORT]")
    println("Example: MDM-Core.w3ibm.bluemix.com localhost 4567")
    exitProcess(1)
}

/**
 * Takes in a JSON String and parses the Id field out of it
 */
fun getRunID(body: String): Int
{
    val parser: Parser = Parser()
    val json: JsonObject = parser.parse(StringBuilder(body)) as JsonObject
    return json.get("Id") as Int
}

/**
 * Fetches the properties for a specific run id.
 * Returns json representation of the properties
 */
fun loadProperties(id: Int): JSONObject
{
    return get(COREURL + "/run/fetch/$id").jsonObject
}

fun Request.qp(key: String): String = this.queryParams(key) //adds .qp alias for .queryParams on Request object
