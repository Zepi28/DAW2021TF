package com.daw.dto

import com.daw.*
import com.daw.model.Issue
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI
import java.util.*

fun updateIssueAction(issueId : Int, projectName: String) = SirenAction(
    name = "update-current-issue",
    title = "Update Current Issue",
    href = URI("$PROJECTS_PATH/$projectName/$ISSUE_PATH/$issueId"),
    method = HttpMethod.PUT,
    type = MediaType.APPLICATION_JSON,
    fields = listOf(SirenAction.Field("i_name", "String"), SirenAction.Field("i_description", "String"))
)

fun deleteIssueAction(issueId : Int, projectName: String) = SirenAction(
    name = "delete-current-issue",
    title = "Delete Current Issue",
    href = URI("$PROJECTS_PATH/$projectName/$ISSUE_PATH/$issueId"),
    method = HttpMethod.DELETE,
    type = MediaType.APPLICATION_JSON,
    fields = listOf(SirenAction.Field("i_id", "String"))
)

fun getIssueAction(issueId : Int, projectName : String) = SirenAction(
    name = "get-current-issue",
    title = "Get Current Issue",
    href = URI("$PROJECTS_PATH/$projectName/$ISSUE_PATH/$issueId"),
    method = HttpMethod.GET,
    type = MediaType.APPLICATION_JSON,
    fields = listOf(SirenAction.Field("i_id", "String"))
)

class IssueOutputModel (
    val id: Int,
    val i_name: String,
    val p_name: String,
    val i_description: String,
    val creationDate: Date,
    val closeDate: Date?
)


class SingleIssueOutputModel(val issue: Issue) {
    fun toSirenObject(selfUri: URI, actions: List<SirenAction>? = null) = SirenEntity(
        properties = this,
        clazz = listOf("Issues"),
        links = listOf(SirenLink(rel = listOf("self"), href = selfUri)),
        actions = actions
    )
}

class IssuesOutputModel(private val issue: List<Issue>, private val projectName: String) {
    fun toSirenObject(selfUri: URI, actions: List<SirenAction>? = null) = SirenEntity<IssueOutputModel>(
        properties = null,
        entities = issue.map {
            EmbeddedLink(rel = listOf("issues"),
                href = URI("$PROJECTS_PATH/$projectName$ISSUE_PATH/${it.id}"))
        },
        clazz = listOf("Issues"),
        links = listOf(SirenLink(rel = listOf("self"), href = selfUri)),
        actions = actions
    )
}
