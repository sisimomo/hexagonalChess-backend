package com.github.sisimomo.hexagonalchess.backend.configuration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.annotation.JsonValue;
import com.github.sisimomo.hexagonalchess.backend.commons.dto.response.ErrorResponse;
import com.github.sisimomo.hexagonalchess.backend.commons.dto.response.FieldError;
import com.github.sisimomo.hexagonalchess.backend.commons.dto.response.GlobalError;
import com.github.sisimomo.hexagonalchess.backend.commons.exception.UncheckedException;

import io.swagger.v3.oas.annotations.media.Schema;

@ControllerAdvice
@Service
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {
    Class<?> clazz = e.getRequiredType();
    String message;
    if (clazz != null && clazz.isEnum()) {
      message = "The parameter '%s' must have a value among : %s";
      Optional<Method> oMethod =
          Stream.of(clazz.getDeclaredMethods()).filter(method -> method.isAnnotationPresent(JsonValue.class)).findAny();
      if (oMethod.isPresent()) {
        List<String> enumValues = new ArrayList<>();
        for (Object enumValue : clazz.getEnumConstants()) {
          try {
            enumValues.add((String) oMethod.get().invoke(enumValue));
          } catch (IllegalAccessException | InvocationTargetException ignored) {
          }
        }
        message = String.format(message, e.getName(), String.join(", ", enumValues));
      } else {
        message = String.format(message, e.getName(), StringUtils.join(clazz.getEnumConstants(), ", "));
      }
    } else {
      message = "The parameter '%s' must be of type '%s'";
      if (clazz != null && clazz.isAnnotationPresent(Schema.class)) {
        message = String.format(message, e.getName(), clazz.getAnnotationsByType(Schema.class)[0].name());
      } else {
        message = String.format(message, e.getName(), clazz.getTypeName());
      }
    }
    return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {UncheckedException.class})
  @ResponseBody
  protected ResponseEntity<ErrorResponse> handleUncheckedServiceException(UncheckedException ex, WebRequest request) {
    logger.debug(ex);
    return new ResponseEntity<>(new ErrorResponse(ex.getFormattedUserErrorMessage(), ex.getError().getErrorId()),
        ex.getError().getResponseCode());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatusCode status, WebRequest request) {
    StringBuilder sb = new StringBuilder();

    ex.getGlobalErrors().forEach(e -> sb.append("Global error in ").append(e.getObjectName()).append(". Error: ")
        .append(e.getDefaultMessage()).append("<br>"));

    ex.getFieldErrors()
        .forEach(e -> sb.append("Field validation error in ").append(e.getObjectName()).append(". Field: ")
            .append(e.getField()).append(". Value: ").append(e.getRejectedValue()).append(". Error: ")
            .append(e.getDefaultMessage()).append("<br>"));

    sb.delete(sb.length() - 4, sb.length());

    List<GlobalError> globalErrors = ex.getFieldErrors().stream()
        .map(e -> GlobalError.builder().objectName(e.getObjectName()).errorCode(e.getCode()).build()).toList();

    List<FieldError> fieldErrors = ex.getFieldErrors().stream().map(
        e -> FieldError.builder().objectName(e.getObjectName()).fieldName(e.getField()).errorCode(e.getCode()).build())
        .toList();

    logger.warn(sb.toString(), ex);

    return new ResponseEntity<>(new ErrorResponse(sb.toString(), fieldErrors, globalErrors), status);
  }

  @ExceptionHandler({AuthenticationException.class})
  @ResponseBody
  public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
    return new ResponseEntity<>(new ErrorResponse("Full authentication is required to access this resource", "TBG1X1"),
        HttpStatus.UNAUTHORIZED);
  }

}
