package cloud.assignment.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import cloud.assignment.repository.*;;

@RestController
public class VerificationController {

    @Autowired
    private TokenRepository tokenRepository;

    @GetMapping("/verify")
    public String verifyToken(@RequestParam String token) {
        return tokenRepository.findByTokenAndVerifiedIsFalse(token)
                .map(tokenEntity -> {
                    if (tokenEntity.getExpiration().after(new Date())) {
                        tokenEntity.setVerified(true);
                        tokenRepository.save(tokenEntity);
                        return "Verified successfully";
                    } else {
                        return "Verification link expired";
                    }
                })
                .orElse("Invalid token");
    }
}
