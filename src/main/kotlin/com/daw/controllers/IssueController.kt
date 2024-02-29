package com.daw.controllers

import com.daw.*
import com.daw.dto.*
import com.daw.repository.IssueRepository
import com.daw.model.request.IssueRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@CrossOrigin("*")
@RestController
@RequestMapping("$PROJECTS_PATH/{projectName}$ISSUE_PATH")
class IssueController(private val service: IssueRepository) {

    @GetMapping
    fun getIssues(@PathVariable projectName : String) =
        IssuesOutputModel(service.selectIssues(projectName), projectName).toSirenObject(URI("$PROJECTS_PATH/$projectName$ISSUE_PATH"))


    @PutMapping(CREATE_PATH)
    fun insertIssue(@PathVariable projectName : String, @RequestBody request: IssueRequest) : SirenEntity<SingleIssueOutputModel> {
        val issues = service.insertIssue(request.i_name, projectName, request.i_description)
        return SingleIssueOutputModel(issues).toSirenObject(URI("$PROJECTS_PATH/$projectName$ISSUE_PATH/${issues.id}"), listOf(deleteIssueAction(issues.id, projectName), updateIssueAction(issues.id, projectName)))
    }

    @GetMapping("/{id}")
    fun getIssue(@PathVariable projectName: String, @PathVariable id : Int) : SirenEntity<SingleIssueOutputModel> {
        val response = service.selectIssue(projectName,id)
        return SingleIssueOutputModel(response)
            .toSirenObject(URI("$PROJECTS_PATH/$projectName$ISSUE_PATH/$id"))
    }

    @PutMapping("/{issueId}")
    fun putIssue(@PathVariable projectName : String, @PathVariable issueId : Int, @RequestBody request : IssueRequest) : SirenEntity<SingleIssueOutputModel> {
        val issue = service.updateIssue(projectName, issueId, request.i_name, request.i_description, request.closeDate )
        return SingleIssueOutputModel(issue).toSirenObject(URI("$PROJECTS_PATH/$projectName$ISSUE_PATH/${issueId}"), listOf(deleteIssueAction(issue.id, projectName), getIssueAction(issue.id, projectName)))
    }

    @DeleteMapping("/{id}")
    fun deleteIssue(@PathVariable projectName : String, @PathVariable id : Int) : ResponseEntity<SirenEntity<Void>> {
        service.deleteIssue(projectName, id)
        return ResponseEntity.noContent().build()
    }
}