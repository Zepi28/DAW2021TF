package com.daw.model.request

import java.util.*

data class IssueRequest (
        var id: Int = 0,
        var i_name: String = "",
        var i_description: String = "",
        var creationDate: Date = Date(2021),
        var closeDate: Date = Date(2021)
)