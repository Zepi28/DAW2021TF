package com.daw.repository

import com.daw.model.Project
import com.daw.model.request.ProjectRequest

interface ProjectJDBIRepository {

    /** Project */
    fun selectProjects() : List<Project>
    fun selectProject(name: String) : ProjectRequest
    fun insertProject(projectName: String, projectDescription: String, userName: String)
    fun deleteProject(name: String) : Int
    fun deleteAllProject()
    fun updateProject(name: String, description: String) : Project


}