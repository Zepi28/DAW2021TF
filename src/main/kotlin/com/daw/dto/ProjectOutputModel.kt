package com.daw.dto

import com.daw.*
import com.daw.model.Project
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI

fun updateProjectAction(projectName : String) = SirenAction(
    name = "update-current-project",
    title = "Update Current Project",
    href = URI("$PROJECTS_PATH/$projectName"),
    method = HttpMethod.PUT,
    type = MediaType.APPLICATION_JSON,
    fields = listOf(SirenAction.Field("p_description", "String"))
)

fun deleteProjectAction(projectName : String) = SirenAction(
    name = "delete-current-project",
    title = "Delete Current Project",
    href = URI("$PROJECTS_PATH/$projectName"),
    method = HttpMethod.DELETE,
    type = MediaType.APPLICATION_JSON,
    fields = listOf()
)

fun getProjectAction(projectName : String) = SirenAction(
    name = "get-current-project",
    title = "Get Current Project",
    href = URI("$PROJECTS_PATH/$projectName"),
    method = HttpMethod.GET,
    type = MediaType.APPLICATION_JSON,
    fields = listOf(SirenAction.Field("p_description", "String"))
)

class ProjectOutputModel (
    val name: String,
    val description: String?,
    val users : List<UserDTO>?,
    val issues : List<IssueDTO>?
)

class ProjectsOutputModel(private val projects: List<Project>) {
    fun toSirenObject(selfUri: URI, actions: List<SirenAction>? = null) = SirenEntity<ProjectOutputModel>(
        properties = null,
        entities = projects.map {
            EmbeddedLink(rel = listOf("item"),
                href = URI("$PROJECTS_PATH/${it.p_name}"))
        },
        clazz = listOf("Project"),
        links = listOf(SirenLink(rel = listOf("self"), href = selfUri)),
        actions = actions
    )
}

class SingleProjectOutputModel(val project: ProjectOutputModel) {
    fun toSirenObject(selfUri: URI, actions: List<SirenAction>? = null) = SirenEntity(
        properties = this,
        clazz = listOf("Project"),
        links = listOf(SirenLink(rel = listOf("self"), href = selfUri)),
        actions = actions
    )
}
