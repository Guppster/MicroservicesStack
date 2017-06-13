package com.ibm.mdm.delivery.mps.property

class EntryController
{
    //Create an interface to manipulate properties
    val propertyController = PropertyController()

    fun get(id: Int, key: String): String?
    {
        return propertyController.findById(id)?.property?.get(key);
    }

    fun get(name: String, key: String): String?
    {
        return propertyController.findByName(name)?.property?.get(key);
    }
}
