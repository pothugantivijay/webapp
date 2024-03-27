package cloud.assignment.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;

import java.util.Date;
import cloud.assignment.repository.TokenRepository;

@RestController
public class VerificationController {
        private static final Logger logger = LoggerFactory.getLogger(VerificationController.class);

    @Autowired
    private TokenRepository tokenRepository;

    @GetMapping("/verify")
    public String verifyToken(@RequestParam String token) {
        try {
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
        } catch (Exception e) {
            logger.error("error hit"+ e.getMessage());
            //logger.error(e.getStackTrace());
            return "An error occurred while verifying the token";
        }
    }
}
