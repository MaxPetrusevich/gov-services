import io.jsonwebtoken.io.DecodingException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
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
    }
}
