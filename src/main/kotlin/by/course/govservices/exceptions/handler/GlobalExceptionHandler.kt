package by.course.govservices.exceptions.handler

import by.course.govservices.exceptions.BidNotFoundException
import by.course.govservices.exceptions.UserNotFoundException
import io.jsonwebtoken.io.DecodingException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DecodingException::class)
    fun handleDecodingException(ex: DecodingException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body("Decoding error: ${ex.message}")
    }
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any>> {
        val errors = ex.bindingResult.fieldErrors
            .associate { it.field to it.defaultMessage }
        return ResponseEntity.badRequest().body(mapOf("errors" to errors))
    }    @ExceptionHandler(BidNotFoundException::class)
    fun handleBidNotFoundException(ex: BidNotFoundException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun handleGeneralException(ex: Exception): ResponseEntity<String> {
        return ResponseEntity("Произошла непредвиденная ошибка", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}