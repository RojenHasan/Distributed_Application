package be.ucll.da.ownerservice.web;

import be.ucll.da.ownerservice.api.model.ApiError;
import be.ucll.da.ownerservice.domain.OwnerException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({OwnerException.class})
    public ResponseEntity<ApiError> handleNoFieldOfCarTypeException() {
        ApiError error = new ApiError();
        error.setCode("12");
        error.setMessage("error");

        return ResponseEntity.badRequest().body(error);
    }
}
