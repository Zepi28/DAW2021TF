package com.daw.controllers

import com.daw.*
import com.daw.common.authorization.BASIC_SCHEME
import com.daw.dto.*
import com.daw.repository.JDBI.ProjectJDBIRepository
import com.daw.model.request.ProjectRequest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.Base64Utils
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletRequest

@CrossOrigin("*")
@RestController
@RequestMapping(PROJECTS_PATH,  produces = [SIREN_MEDIA_TYPE, MediaType.APPLICATION_JSON_VALUE])
class ProjectController(private val service: ProjectJDBIRepository) {

    @PostMapping("/new")
    fun insertProject(@RequestBody request: ProjectRequest, req: HttpServletRequest) : SirenEntity<SingleProjectOutputModel> {

        /** Get user **/
        val userCredentials = req.getHeader("authorization").drop(BASIC_SCHEME.length + 1).trim()
        val userId = String(Base64Utils.decodeFromString(userCredentials)).split(':')[0]

        service.insertProject(request.p_name, request.p_description, userId)
        return SingleProjectOutputModel(ProjectOutputModel(request.p_name, request.p_description, null, null)).toSirenObject(
            URI("$PROJECTS_PATH/${request.p_name}"))
    }

    @GetMapping
    fun getProjects(req : HttpServletRequest) : SirenEntity<ProjectOutputModel> {
        print("GET PROJECTS CALLED")
        return ProjectsOutputModel(service.selectProjects()).toSirenObject(URI(PROJECTS_PATH))
    }

    @GetMapping("/{name}")
    fun getProject(@PathVariable name: String) : SirenEntity<SingleProjectOutputModel> {
        val response = service.selectProject(name)
        val users : MutableList<UserDTO> = mutableListOf()
        val issues : List<IssueDTO> = service.getProjectIssues(name).map { i -> IssueDTO(i.id, i.i_name, i.i_description, i.creationDate, null) }

        users.add(UserDTO(response.username, response.email))

        return SingleProjectOutputModel(ProjectOutputModel(response.p_name, response.p_description, users, issues))
            .toSirenObject(URI("$PROJECTS_PATH/$name"), listOf(updateProjectAction(name), deleteProjectAction(name)))
    }

    @PutMapping("/{name}")
    fun updateProject(@PathVariable name : String, @RequestBody request : ProjectRequest) : SirenEntity<SingleProjectOutputModel> {
        val response = service.updateProject(name, request.p_description)
        val users : MutableList<UserDTO> = mutableListOf()
        val issues : MutableList<IssueDTO> = mutableListOf()
        return SingleProjectOutputModel(ProjectOutputModel(response.p_name, response.p_description, users, issues))
            .toSirenObject(URI("$PROJECTS_PATH/$name"), listOf(getProjectAction(name), deleteProjectAction(name)))
    }

    @DeleteMapping("/{name}")
    fun deleteProject(@PathVariable name : String) : ResponseEntity<SirenEntity<Void>> {
        service.deleteProject(name)
        return ResponseEntity.noContent().build()
    }
}
