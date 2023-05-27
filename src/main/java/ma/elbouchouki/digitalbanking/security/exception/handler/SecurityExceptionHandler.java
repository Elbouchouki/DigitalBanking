package ma.elbouchouki.digitalbanking.security.exception.handler;

import lombok.extern.slf4j.Slf4j;
import ma.elbouchouki.digitalbanking.security.config.SecurityConstant;
import ma.elbouchouki.digitalbanking.security.exception.InvalidCredentialsException;
import ma.elbouchouki.digitalbanking.security.exception.InvalidTokenException;
import ma.elbouchouki.digitalbanking.security.exception.handler.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;
import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class SecurityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionResponse> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .code(HttpStatus.UNAUTHORIZED.value())
                                .status(HttpStatus.UNAUTHORIZED.name())
                                .message(getMessage(
                                        SecurityConstant.SecurityExceptionMessage.INVALID_CREDENTIALS,
                                        null))
                                .build()
                );
    }

    @ExceptionHandler(InvalidTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionResponse> handleInvalidTokenException(InvalidTokenException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        ExceptionResponse.builder()
                                .code(HttpStatus.UNAUTHORIZED.value())
                                .status(HttpStatus.UNAUTHORIZED.name())
                                .message(getMessage(
                                        SecurityConstant.SecurityExceptionMessage.INVALID_TOKEN,
                                        null))
                                .build()
                );
    }

    private String getMessage(String key, Object[] args) {
        return Objects.requireNonNull(getMessageSource())
                .getMessage(key, args, Locale.getDefault());
    }

}
