package com.daw.repository.JDBI;

import com.daw.model.Issue;

import java.util.Date;

interface IssueJDBIRepository {

    /** Issue */
    fun selectIssues(p_name : String) : List<Issue>
    fun selectIssue(p_name : String,id: Int) : Issue
    fun insertIssue(i_name : String, p_name : String, i_description : String) : Issue
    fun deleteIssue(p_name : String,id: Int) : Int
    fun updateIssue(p_name : String,id : Int, name: String, description: String, closeDate :Date) : Issue
}