package com.daw.repository

import com.daw.configs.DBConfiguration
import com.daw.model.User
import com.daw.repository.JDBI.UserJDBIRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class UserRepository(config : DBConfiguration) : UserJDBIRepository {

    val jdbi: Jdbi = Jdbi.create(config.driverManagerDataSource())
    override fun isAUser(userName: String): Boolean {

        val response = jdbi.withHandle<List<User>, RuntimeException> {
            it.createQuery("SELECT * FROM GIT_USER WHERE username= ?")
                .bind(0, userName)
                .mapToBean(User::class.java)
                .list()
        }
        return response.isNotEmpty()
    }

    override fun createUser(userName: String) {

        jdbi.useHandle<RuntimeException> {
            it.createUpdate("INSERT INTO GIT_USER VALUES (:username, :password, :email)")
                .bind("username", userName)
                .bind("password", "NotAGoodPW")
                .bind("email", "$userName@alunos.isel.pt")
                .execute()
        }
    }


}