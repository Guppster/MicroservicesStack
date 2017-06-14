package com.ibm.mdm.delivery.mps

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.ibm.mdm.delivery.mps.property.EntryController
import com.ibm.mdm.delivery.mps.property.PropertyController
import spark.Request
import spark.Spark.*

fun main(args: Array<String>)
{
    exception(Exception::class.java) { e, req, res -> e.printStackTrace() }

    //Create an interface to manipulate properties
    val propertyController = PropertyController()

    var entryController = EntryController(propertyController)

    //Enable YAML parsing
    val mapper = ObjectMapper(JsonFactory())

    //Enable Kotlin support
    mapper.registerModule(KotlinModule())

    path("/properties")
    {
        get("")
        { req, res ->
            mapper.writeValueAsString(propertyController.properties)
        }

        get("/id/:id")
        { req, res ->
            mapper.writeValueAsString(propertyController.findById(req.params("id").toInt()))
        }


        get("/name/:name")
        { req, res ->
            mapper.writeValueAsString(propertyController.findByName(req.params("name")))
        }

        post("/new")
        { req, res ->
            propertyController.new(name = req.qp("name"), property = req.qp("property"))
            res.status(201)
            "OK"
        }

        patch("/update/id/:id")
        { req, res ->
            propertyController.update(
                    id = req.params("id").toInt(),
                    name = req.qp("name"),
                    property = req.qp("property")
            )
            "OK"
        }

        patch("/update/name/:name")
        { req, res ->
            propertyController.update(
                    name = req.params("name"),
                    property = req.qp("property")
            )
            "OK"
        }

        delete("/delete/:id")
        { req, res ->
            propertyController.delete(req.params("id").toInt())
            "OK"
        }
    }

    path("/entry")
    {
        get("/id/:id/:key")
        { req, res ->
            mapper.writeValueAsString(
                    entryController.get(
                            id = req.params("id").toInt(),
                            key = req.params("key")
                    )
            )
        }

        get("/name/:name/:key")
        { req, res ->
            mapper.writeValueAsString(
                    entryController.get(
                            name = req.params("name"),
                            key = req.params("key")
                    )
            )
        }

    }

    propertyController.properties.forEach(::println)
}

fun Request.qp(key: String): String = this.queryParams(key) //adds .qp alias for .queryParams on Request object
