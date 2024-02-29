package com.daw.common.exceptions

import java.lang.RuntimeException

class ObjectNotFoundException(message: String) : RuntimeException(message)
class DuplicatedKeyException(message: String) : RuntimeException(message)
class IllegalCharacterException(message: String) : RuntimeException(message)