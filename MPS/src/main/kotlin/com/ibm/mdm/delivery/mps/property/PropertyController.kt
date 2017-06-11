package com.ibm.mdm.delivery.mps.property

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.util.concurrent.atomic.AtomicInteger

class PropertyController
{
    //Initialize some random properties
    val properties = hashMapOf(
            0 to PropertyMapping(name = "MDM115", property = "- this: is some \n- yaml: \n- [that, we] \n- {will: be, parsing: in-code}", id = 0),
            1 to PropertyMapping(name = "MDM116", property = "- this: is some \n- yaml: \n- [that, we] \n- {will: be, parsing: in-code}", id = 1)
    )

    var lastId: AtomicInteger = AtomicInteger(properties.size - 1)

    fun save(name: String, property: String)
    {
        val id = lastId.incrementAndGet()

        properties.put(id, PropertyMapping(name = name, property = property, id = id))
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
        properties.put(id, PropertyMapping(name = name, property = property, id = id))
    }

    fun delete(id: Int) {
        properties.remove(id)
    }
}