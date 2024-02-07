package cloud.assignment.security;
import cloud.assignment.service.userDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private userDetailsService UserDetailsservice;


    @Autowired
    private BCryptPasswordEncoder pwdEncoder;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(new AntPathRequestMatcher("/v1/user")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/v1/user/self")).authenticated()
                        .requestMatchers(new AntPathRequestMatcher("/healthz")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
                        .anyRequest().permitAll()
                )
                .csrf((csrf) -> csrf.disable())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(UserDetailsservice)
                .passwordEncoder(pwdEncoder);

    }
}