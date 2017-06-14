package com.ibm.mdm.delivery.mps.property

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.util.concurrent.atomic.AtomicInteger

class PropertyController
{
    //Initialize some random properties
    val properties = hashMapOf(
            0 to PropertyMapping(name = "PTEF -- MDM115BaseLine", property = hashMapOf("property1" to "value1"), id = 0),
            1 to PropertyMapping(name = "PTEF -- MDM116", property = hashMapOf("property1" to "value1"), id = 1)
    )

    var lastId: AtomicInteger = AtomicInteger(properties.size - 1)

    fun new(name: String, property: String)
    {
        val id = lastId.incrementAndGet()

        properties.put(id, PropertyMapping(name = name, property = jsonToPropertyMap(property), id = id))
    }

    fun findById(id: Int): PropertyMapping?
    {
        return properties[id]
    }

    fun findByName(name: String): PropertyMapping?
    {
        return properties.values.find { it.name == name } // == is equivalent to java .equals() (referential equality is checked by ===)
    }

    fun update(id: Int, name: String, property: String)
    {
        properties.put(id, PropertyMapping(name = name, property = jsonToPropertyMap(property), id = id))
    }

    fun update(name: String, property: String)
    {
        val id = findByName(name)!!.id
        properties.put(id , PropertyMapping(name = name, property = jsonToPropertyMap(property), id = id))
    }

    fun delete(id: Int) {
        properties.remove(id)
    }

    //Converts a JSON string to a HashMap<String, String>
    fun jsonToPropertyMap(json: String): Map<String, String>
    {
        val mapper = ObjectMapper().registerKotlinModule()

        val result: HashMap<String, String> = mapper.readValue(json)

        return result
    }

}