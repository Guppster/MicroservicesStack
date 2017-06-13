package com.ibm.mdm.delivery.mps

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

    var entryController = EntryController()

    //Enable YAML parsing
    val mapper = ObjectMapper(YAMLFactory())

    //Enable Kotlin support
    mapper.registerModule(KotlinModule())

    path("/properties")
    {
        get("")
        { req, res ->
            mapper.writeValueAsString(propertyController.properties)
        }

        get("/:id")
        { req, res ->
            mapper.writeValueAsString(propertyController.findById(req.params("id").toInt()))
        }

        get("/:id/:key/")
        { req, res ->
            mapper.writeValueAsString(
                    entryController.get(
                    id = req.params("id").toInt(),
                    key = req.params("key")
                    )
            )
        }

        get("/name/:name")
        { req, res ->
            mapper.writeValueAsString(propertyController.findByName(req.params("name")))
        }

        get("/name/:name/:key")
        { req, res ->
            mapper.writeValueAsString(
                    entryController.get(
                            name = req.params("id"),
                            key = req.params("key")
                    )
            )
        }

        post("/new")
        { req, res ->
            propertyController.new(name = req.qp("name"), property = req.qp("property"))
            res.status(201)
            "OK"
        }

        patch("/update/:id")
        { req, res ->
            propertyController.update(
                    id = req.params("id").toInt(),
                    name = req.qp("name"),
                    property = req.qp("property")
            )
            "OK"
        }

        patch("/update/:name")
        { req, res ->
            propertyController.update(
                    name = req.params("name"),
                    id = req.qp("id").toInt(),
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

    propertyController.properties.forEach(::println)
}

fun Request.qp(key: String): String = this.queryParams(key) //adds .qp alias for .queryParams on Request object
