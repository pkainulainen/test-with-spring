package com.testspringmaster.kotlin.example

class PersonRepository {

    /**
     * Finds the requested person from the database by
     * using the person id as a search criteria.
     * @param   id  The id of the requested person.
     * @return  The information of the found person.
     * @throws  NotFoundException   if no person is found with the given id.
     */
    fun findById(id: Long): Person {
        when(id) {
            1L -> return Person(id = 1L, name = "John Doe")
            2L -> return Person(id = 2L, name = "Jane Doe")
            else -> throw NotFoundException("No person found with id: #$id")
        }
    }
}