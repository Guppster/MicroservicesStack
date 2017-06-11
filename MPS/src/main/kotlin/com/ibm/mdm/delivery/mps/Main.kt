package com.ibm.mdm.delivery.mps

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ibm.mdm.delivery.mps.property.PropertyController
import spark.Request
import spark.Spark.*

fun main(args: Array<String>)
{
    exception(Exception::class.java) { e, req, res -> e.printStackTrace() }

    val propertyController = PropertyController()

    path("/properties")
    {
        get("")
        { req, res ->
            jacksonObjectMapper().writeValueAsString(propertyController.properties)
        }

        get("/:id")
        { req, res ->
            propertyController.findById(req.params("id").toInt())
        }

        get("/name/:name")
        { req, res ->
            propertyController.findByName(req.params("name"))
        }

        post("/save")
        { req, res ->
            propertyController.save(name = req.qp("name"), property = req.qp("property"))
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

        delete("/delete/:id")
        { req, res ->
            propertyController.delete(req.params("id").toInt())
            "OK"
        }

    }

    propertyController.properties.forEach(::println)
}

fun Request.qp(key: String): String = this.queryParams(key) //adds .qp alias for .queryParams on Request object
