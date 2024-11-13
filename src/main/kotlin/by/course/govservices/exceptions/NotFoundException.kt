
package by.course.govservices.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)  // HTTP статус 404
class NotFoundException(message: String) : RuntimeException(message)
