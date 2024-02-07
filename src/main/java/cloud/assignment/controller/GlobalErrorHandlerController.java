package cloud.assignment.controller;

import cloud.assignment.model.User;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;
import javax.naming.ServiceUnavailableException;

@ControllerAdvice
public class GlobalErrorHandlerController {
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<User> InvalidInputException(InvalidInputException ie){
        System.err.println(ie.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<User>MethodArgumentNotValidException(MethodArgumentNotValidException me){
        System.err.println(me.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<User>NullPointerException(NullPointerException np){
        System.err.println(np.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<User>HttpMessageNotRedable(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<User>NoHandlerFoundException(NoHandlerFoundException nh){
        System.err.println(nh.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<?>DataAccessException(ServiceUnavailableException dae){
        System.err.println(dae.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
    }
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<?> handleUnauthorizedException(HttpClientErrorException.Unauthorized ex) {
        System.err.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    @ExceptionHandler(HttpClientErrorException.UnsupportedMediaType.class)
    public ResponseEntity<?> handleUnsupportedMediaException(HttpClientErrorException.UnsupportedMediaType umt) {
        System.err.println(umt.getMessage());
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(null);
    }
}