package cloud.assignment.controller;

import cloud.assignment.model.TokenEntity;
import cloud.assignment.model.User;
import cloud.assignment.repository.TokenRepository;
import cloud.assignment.repository.UserRepository;
import cloud.assignment.service.connection;
import cloud.assignment.service.userservice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

//import com.google.api.gax.core.FixedCredentialsProvider;
//import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.gson.Gson;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

//import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/v1/user")
public class UserController{
    @Autowired
    private userservice Userservice;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    connection Connection;
    @Autowired
    private TokenRepository tokenRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @PostMapping
    public ResponseEntity<?> createuser(@RequestBody User user){
        try {
            logger.info("info hit");
            logger.debug("debug hit");
            logger.warn("warn hit");
            logger.error("error hit");
            if(!Connection.isconnectionok()){
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).cacheControl(CacheControl.noCache()).build();
            }
            if (Userservice.findByUsername(user.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).body(null);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            pubsubMessage(user);
            User savedUser = Userservice.createuser(user);

            // Excluding the password
            Map<String, Object> userResponse = new HashMap<>();
            userResponse.put("username", savedUser.getUsername());
            userResponse.put("id", savedUser.getID());
            userResponse.put("first_name", savedUser.getFirst_name());
            userResponse.put("last_name", savedUser.getLast_name());
            userResponse.put("account_created", savedUser.getAccount_created());
            userResponse.put("account_updated", savedUser.getAccount_updated());
            //pubsubMessage(savedUser);

            System.out.println("THE EMAIL ADDRESS HAS BEEN CREATED");
            return ResponseEntity.status(HttpStatus.CREATED).cacheControl(CacheControl.noCache()).body(userResponse);
        } catch(Exception e){
            // It's a good idea to log the exception
            System.out.println("INVALID USER DETAILS");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).body(null);
        }
    }


    @RequestMapping(method = {RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.GET, RequestMethod.PATCH})
    public ResponseEntity<?>user(@RequestBody User user){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).body(null);
    }
    @GetMapping("/self")
    public ResponseEntity<?> getuser(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!Connection.isconnectionok()){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).cacheControl(CacheControl.noCache()).body(null);
        }
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<User> userOptional = userRepository.findByUsername(userDetails.getUsername());

            if(userOptional==null){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).cacheControl(CacheControl.noCache()).body(null);
            }
            Optional<TokenEntity> token = tokenRepository.findById(userDetails.getUsername());
            if(token.isEmpty()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).body(null);
            }
            if(!token.get().isVerified()){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).cacheControl(CacheControl.noCache()).body("USER NOT VERIFIED");
            }

            try {
                User ua = userOptional.get();
                Map<String, Object> userResponse = new HashMap<>();
                userResponse.put("username", ua.getUsername());
                userResponse.put("id", ua.getID());
                userResponse.put("firstname", ua.getFirst_name());
                userResponse.put("lastname", ua.getLast_name());
                userResponse.put("account_created", ua.getAccount_created());
                userResponse.put("account_updated", ua.getAccount_updated());
                System.out.println("USER DETAILS FOUND");

                return ResponseEntity.status(HttpStatus.OK).cacheControl(CacheControl.noCache()).body(userResponse);
            } catch (Exception e) {
                System.out.println("USER DETAILS NOT FOUND");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).cacheControl(CacheControl.noCache()).build();
            }
        } else {
            System.out.println("USER DETAILS NOT FOUND");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).cacheControl(CacheControl.noCache()).build();
        }
    }
    //put mapping is given here
    @SuppressWarnings("unused")
    @PutMapping("/self")
        public ResponseEntity<?> updateuser(@RequestBody User updateInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isUpdated = false;
        if(!Connection.isconnectionok()){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).cacheControl(CacheControl.noCache()).body(null);
        }
        if (!authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).cacheControl(CacheControl.noCache()).build();
        }
        if (updateInfo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).body(null);
        }
        try{
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<User> userOptional = userRepository.findByUsername(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).cacheControl(CacheControl.noCache()).build();
        }
        if(userOptional==null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).cacheControl(CacheControl.noCache()).body(null);
        }
        Optional<TokenEntity> token = tokenRepository.findById(userDetails.getUsername());
        if(token.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).body(null);
        }

        if(!token.get().isVerified()){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).cacheControl(CacheControl.noCache()).body("User not verified!");
        }

        User user = userOptional.get();
            if(updateInfo.getFirst_name() == null || updateInfo.getLast_name() == null || updateInfo.getPassword() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).body(null);
            }
        if (updateInfo.getUsername() != null && !Objects.equals(updateInfo.getUsername(), user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).body(null);
        }
        if (updateInfo.getFirst_name() != null) {
            user.setFirst_name(updateInfo.getFirst_name());
            isUpdated = true;
        }
        if (updateInfo.getLast_name() != null) {
            user.setLast_name(updateInfo.getLast_name());
            isUpdated = true;
        }
        if (updateInfo.getPassword() != null) {
            String password = passwordEncoder.encode(updateInfo.getPassword());
            user.setPassword(password);
            isUpdated = true;
        }
        user.setAccount_updated(user.getAccount_updated());
        userRepository.save(user);
        }
        catch(Exception e){
            if (!isUpdated) {
                System.out.println("CANNOT UPDATE USER DETAILS");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).cacheControl(CacheControl.noCache()).body(null);
            }
        }
        return ResponseEntity.noContent().cacheControl(CacheControl.noCache()).build();
    }
    private void pubsubMessage(User user) {
        String projectId = "devproject-414915";
        String topicId = "verify_email";
        Gson gson = new Gson();
        String jsonData = gson.toJson(user);

        ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);
        Publisher publisher = null;

        try {
            publisher = Publisher.newBuilder(topicName).build();
            ByteString data = ByteString.copyFromUtf8(jsonData);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
            publisher.publish(pubsubMessage).get();
            System.out.println(data);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (publisher != null) {
                publisher.shutdown();
            }
        }
    }
}


