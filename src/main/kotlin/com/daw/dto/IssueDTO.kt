package com.daw.dto

import java.util.*

data class IssueDTO(
    var id : Int,
    var i_name : String,
    var i_description : String,
    var creationDate : Date,
    var comments: List<CommentDTO>?
)


