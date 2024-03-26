package cloud.assignment.service;
import cloud.assignment.model.User;
import cloud.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import java.util.Date;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@Service
public class userservice {
    @Autowired
    private UserRepository userRepository;
    public User createuser(User user){
        user.setAccount_created(new Date());
        user.setAccount_updated(new Date());
        return userRepository.save(user);
    }
    public Optional<User> findByUsername(String username) {
        System.out.println(username);
        return userRepository.findByUsername(username);
    } 
}
