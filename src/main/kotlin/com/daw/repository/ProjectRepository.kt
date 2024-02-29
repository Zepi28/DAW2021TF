package com.daw.repository

import com.daw.model.request.ProjectRequest
import com.daw.common.exceptions.DuplicatedKeyException
import com.daw.common.exceptions.ObjectNotFoundException
import com.daw.configs.DBConfiguration
import com.daw.model.Issue
import com.daw.model.Project
import com.daw.repository.JDBI.ProjectJDBIRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class ProjectRepository(config: DBConfiguration) : ProjectJDBIRepository {

    val jdbi: Jdbi = Jdbi.create(config.driverManagerDataSource())
    val user: UserRepository = UserRepository(config)

    /**
     * Apontamentos:
     *
     * Use useHandle when you need when you don't need to return a value
     * Use withHandle when you need to return a value
     */
    override fun selectProjects() : List<Project> {

        val response = jdbi.withHandle<List<Project>, RuntimeException> {
            it.createQuery("SELECT * FROM PROJECT")
                .mapToBean(Project::class.java)
                .list()
        }

        if(response.isEmpty())
            throw ObjectNotFoundException("Couldn't find any projects")

        return response
    }

    /**
     * @name Name of the project you want to selected
     *
     * @return Project selected
     */
    override fun selectProject(name: String) : ProjectRequest {
        val response = jdbi.withHandle<List<ProjectRequest>, RuntimeException> {
            it.select("SELECT p.*, u.username, u.email \n" +
                    "FROM PROJECT p\n" +
                    "INNER JOIN PROJECT_USER pu ON p.p_name=pu.p_name\n" +
                    "INNER JOIN GIT_USER u ON u.username=pu.username \n" +
                    "WHERE p.p_name=? ")
                .bind(0, name)
                .mapToBean(ProjectRequest::class.java)
                .list()
        }
        
        if(response.isEmpty())
            throw ObjectNotFoundException("Couldn't find any project named $name")
        return response[0]
    }


    fun findByProjectName(name: String) : Boolean {
        val response = jdbi.withHandle<List<Project>, RuntimeException> {
            it.createQuery("SELECT * FROM PROJECT WHERE p_name= ?")
                .bind(0, name)
                .mapToBean(Project::class.java)
                .list()
        }
        return response.isNotEmpty()
    }

    /**
     * @name Name of the project you want to insert
     * @description Description that you want to insert
     *
     */
    override fun insertProject(projectName: String, projectDescription: String, userName: String) {
        // Check for duplicate key
        if(findByProjectName(projectName))
            throw DuplicatedKeyException("Project named $projectName already exists")

        // Check if User exists
        if(!user.isAUser(userName)){
            user.createUser(userName) // Create User
        }

        // Creat Project
        jdbi.useHandle<RuntimeException> {
            it.createUpdate("INSERT INTO PROJECT VALUES (:name, :description)")
                .bind("name", projectName)
                .bind("description", projectDescription)
                .execute()
        }

        // Create relation between User and Project
        jdbi.useHandle<RuntimeException> {
            it.createUpdate("INSERT INTO PROJECT_USER VALUES (:username, :p_name)")
                .bind("username", userName)
                .bind("p_name", projectName)
                .execute()
        }
    }

    /**
     * @name Name of the project you want to delete
     *
     * @return Integer of project deleted
     */
    override fun deleteProject(name: String) : Int {
        val response = jdbi.withHandle<Int, RuntimeException> {
            it.createUpdate("DELETE FROM PROJECT WHERE p_name = :name")
                .bind("name", name)
                .execute()
        }
        if (response == 0)
            throw ObjectNotFoundException("Couldn't find any project named $name")
        return response
    }

    override fun deleteAllProject() {
        return jdbi.useHandle<RuntimeException> {
            it.createUpdate("DELETE * FROM PROJECT")
                .execute()
        }
    }

    /**
     * @name Name of the project you want to update
     * @description Description that you want to update
     *
     * @return Project updated
     */
    override fun updateProject(name: String, description: String) : Project {
        if(!findByProjectName(name))
            throw ObjectNotFoundException("Couldn't find any project named $name")

        jdbi.useHandle<RuntimeException> {
            it.createUpdate("UPDATE PROJECT SET p_description = :description WHERE p_name = :name")
                .bind("name", name)
                .bind("description", description)
                .execute()
        }
        return Project()
    }

    override fun getProjectIssues(projectName: String): List<Issue> {
        return jdbi.withHandle<List<Issue>, RuntimeException> {
            it.createQuery("SELECT * FROM ISSUE WHERE p_name = ? ")
                .bind(0, projectName)
                .mapToBean(Issue::class.java)
                .list()
        }
    }
}

