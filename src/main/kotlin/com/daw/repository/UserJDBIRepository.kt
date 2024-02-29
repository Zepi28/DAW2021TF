package com.daw.repository

interface UserJDBIRepository {
    fun isAUser(userName : String) : Boolean
    fun createUser(userName : String)
}