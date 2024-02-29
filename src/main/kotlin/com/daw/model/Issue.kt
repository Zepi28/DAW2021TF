package com.daw.model

import java.util.*

class Issue {

    var id: Int = 0
    var i_name: String = ""
    var i_description: String = ""
    var p_name: String = ""
    var creationDate: Date = Date(2021)
    var closeDate: Date = Date(2021)

    init {
        this.id = id
        this.i_name = i_name
        this.i_description = i_description
        this.creationDate = creationDate
        this.closeDate = closeDate
    }

}