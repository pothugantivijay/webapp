package cloud.assignment.service;
import cloud.assignment.model.User;
import cloud.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class userservice {
    @Autowired
    private UserRepository userRepository;
    public User createuser(User user){
        user.setAccount_created(Instant.now());
        user.setAccount_updated(Instant.now());
        return userRepository.save(user);
    }
    public Optional<User> findByUsername(String username) {
        System.out.println(username);
        return userRepository.findByUsername(username);
    }

}
