package com.daw.model.request

data class ProjectRequest(
    var p_name: String = "",
    var p_description: String = "",
    var username: String = "",
    var email: String = ""
)