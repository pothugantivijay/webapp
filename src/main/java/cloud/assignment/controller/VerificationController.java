package cloud.assignment.controller;

import cloud.assignment.model.TokenEntity;
import cloud.assignment.model.User;
import cloud.assignment.repository.TokenRepository;
import cloud.assignment.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;

@RestController
public class VerificationController {

    @Autowired
    private TokenRepository tokenRepository;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyuser(HttpServletRequest req, @RequestParam("token") String tok) {

        try {
            Optional<TokenEntity> t = tokenRepository.findByLink(tok);
            if (t.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            TokenEntity tk = t.get();
            Timestamp ts = tk.getExptime();

            long exp_time = ts.getTime();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            long curr_time = now.getTime();

            if (exp_time - curr_time > 0) {

                tk.setVerified(true);
                tokenRepository.save(tk);
                return ResponseEntity.status(HttpStatus.OK).body("User Verified !!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification Request timed out. Try again !!");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
        }
    }
}
