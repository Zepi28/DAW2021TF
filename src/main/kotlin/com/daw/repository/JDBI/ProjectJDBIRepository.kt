package com.daw.repository.JDBI

import com.daw.model.request.ProjectRequest
import com.daw.model.Issue
import com.daw.model.Project

interface ProjectJDBIRepository {

    /** Project */
    fun selectProjects() : List<Project>
    fun selectProject(name: String) : ProjectRequest
    fun insertProject(projectName: String, projectDescription: String, userName: String)
    fun deleteProject(name: String) : Int
    fun deleteAllProject()
    fun updateProject(name: String, description: String) : Project
    fun getProjectIssues(projectName: String) : List<Issue>
}