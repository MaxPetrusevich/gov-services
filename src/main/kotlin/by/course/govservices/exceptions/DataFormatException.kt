package by.course.govservices.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)  // HTTP статус 400 - некорректный запрос
class DataFormatException(message: String) : RuntimeException(message)
