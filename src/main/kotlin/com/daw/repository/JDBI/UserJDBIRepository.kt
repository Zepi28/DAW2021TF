package com.daw.repository.JDBI

interface UserJDBIRepository {
    fun isAUser(userName : String) : Boolean
    fun createUser(userName : String)
}