package cloud.assignment.service;

import cloud.assignment.model.User;
import cloud.assignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.ServiceUnavailableException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class userDetailsService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<?> DataAccessException(ServiceUnavailableException dae){
        System.err.println(dae.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(username+" Entered userDetailsService");
        try {
            Optional<User> accountOptional = userRepository.findByUsername(username);

            if (accountOptional.isPresent()) {
                User user = accountOptional.get();
                System.out.println(user.getPassword() + " " + user.getUsername() + " options account is present");
                List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

                return new org.springframework.security.core.userdetails.User(
                        user.getUsername(), user.getPassword(), authorities
                );
            } else {
                throw new UsernameNotFoundException("Account not found with email: " + username);
            }
        }
        catch(Exception e){

        }
        throw new UsernameNotFoundException("");
    }

}
