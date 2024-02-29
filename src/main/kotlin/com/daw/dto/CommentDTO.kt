package com.daw.dto

import java.util.*

data class CommentDTO (
    var id: Int,
    var description: String,
    var dt: Date,
    var author: UserDTO
)


