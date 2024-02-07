package cloud.assignment.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cloud.assignment.service.connection;

@RestController
@RequestMapping("/healthz")
public class Healthchecker{
    @Autowired
    connection connection;
    public ResponseEntity<Void> cache(HttpStatus status){
        HttpHeaders header = new HttpHeaders();
        return ResponseEntity.status(status).headers(header).build();
    }
    @GetMapping
    public ResponseEntity<Void> check(HttpServletRequest request) {
        if(request.getContentLength()>0 || request.getQueryString()!=null) {
            return cache(HttpStatus.BAD_REQUEST);
//        } else if (request.getHeaderNames()!=null) {
//            return ResponseEntity.badRequest().headers(headers).build();
//        }
        }else if (connection.isconnectionok()) {
            return cache(HttpStatus.OK);
        } else if (!connection.isconnectionok()) {
            return cache(HttpStatus.SERVICE_UNAVAILABLE);
        }
        return ResponseEntity.ok().build();
    }
    @PutMapping
    public ResponseEntity<Void> checkput(){
        return cache(HttpStatus.METHOD_NOT_ALLOWED);
    }
    @PostMapping
    public ResponseEntity<Void> checkpost(){
        return cache(HttpStatus.METHOD_NOT_ALLOWED);
    }
    @DeleteMapping
    public ResponseEntity<Void> checkdel(){
        return cache(HttpStatus.METHOD_NOT_ALLOWED);
    }
    @PatchMapping
    public ResponseEntity<Void>checkpatch(){
        return cache(HttpStatus.METHOD_NOT_ALLOWED);
    }
}
