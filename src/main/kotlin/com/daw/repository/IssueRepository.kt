package com.daw.repository;

import com.daw.common.exceptions.ObjectNotFoundException
import com.daw.configs.DBConfiguration;
import com.daw.model.Issue
import com.daw.repository.JDBI.IssueJDBIRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component;
import java.lang.RuntimeException
import java.time.LocalDateTime
import java.util.*

@Component
class IssueRepository(config : DBConfiguration) : IssueJDBIRepository {

    val jdbi: Jdbi = Jdbi.create(config.driverManagerDataSource())

    override fun selectIssues(p_name: String): List<Issue> {
        return jdbi.withHandle<List<Issue>, RuntimeException> {
            it.createQuery("SELECT * FROM ISSUE WHERE p_name = ? ")
                    .bind(0, p_name)
                    .mapToBean(Issue::class.java)
                    .list()
        }
    }

    fun getLatestIssue() : Issue{
        return jdbi.withHandle<Issue, RuntimeException> {
            it.createQuery("SELECT * FROM ISSUE ORDER by id DESC;")
                .mapToBean(Issue::class.java)
                .list().first()
        }
    }

    override fun selectIssue(p_name: String, id: Int): Issue {
        // TODO:
        /**
        SELECT i.i_name, i.p_name, i.i_description, i.creationdate, c.description, c.dt FROM ISSUE i
        INNER JOIN COMMENT c
        ON i.id = c.issue_id
        AND i.id=:i_id AND i.p_name=:p_name
         */
        val response = jdbi.withHandle<List<Issue>, RuntimeException> {
            it.createQuery("SELECT * FROM ISSUE WHERE id = :id AND p_name = :p_name")
                    .bind("p_name", p_name)
                    .bind("id",id)
                    .mapToBean(Issue::class.java)
                    .list()
        }
        if(response.isEmpty())
            throw ObjectNotFoundException("Couldn't find any Issue with id $id from Project $p_name")
        return response.first()
    }

    override fun insertIssue(i_name : String, p_name : String, i_description : String) : Issue {
        jdbi.useHandle<RuntimeException> {
            it.createUpdate("INSERT INTO ISSUE (i_name, p_name, i_description, creationDate) " +
                "VALUES (:i_name, :p_name, :i_description, :creationDate)")
                    .bind("i_name", i_name)
                    .bind("p_name",p_name)
                    .bind("i_description", i_description)
                    .bind("creationDate", LocalDateTime.now())
                    .execute()
        }

        return getLatestIssue()
    }

    override fun deleteIssue(p_name: String, id: Int): Int {
        val response =  jdbi.withHandle<Int, ObjectNotFoundException> {
            it.createUpdate("DELETE FROM ISSUE WHERE p_name = :p_name AND id = :id")
                    .bind("id",id)
                    .bind("p_name", p_name)
                    .execute()
        }

        if(response == 0)
            throw ObjectNotFoundException("Couldn't find any Issue with id $id from Project $p_name")
        return response
    }

    override fun updateIssue(p_name: String, id: Int, name: String, description: String, closeDate: Date): Issue {
        jdbi.useHandle<RuntimeException> {
            it.createUpdate("UPDATE ISSUE SET i_description = :i_description, i_name = :i_name, closeDate = :closeDate WHERE p_name = :p_name AND id = :id")
                    .bind("id",id)
                    .bind("p_name",p_name)
                    .bind("i_description", description)
                    .bind("i_name", name)
                    .bind("closeDate",closeDate)
                    .execute()
        }
        return selectIssue(p_name,id)
    }
}