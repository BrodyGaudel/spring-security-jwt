package com.brodygaudel.securityservice;

import com.brodygaudel.securityservice.entity.Role;
import com.brodygaudel.securityservice.entity.User;
import com.brodygaudel.securityservice.repository.RoleRepository;
import com.brodygaudel.securityservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootApplication
public class SecurityServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){
        return args -> {
            List<Role> roles = roleRepository.findAll();
            if(roles.isEmpty()){
                log.info("******************* init roles ***********************************");
                roleRepository.save(new Role(null, "USER"));
                roleRepository.save(new Role(null, "ADMIN"));
                roleRepository.save(new Role(null, "SUPER_ADMIN"));
                log.info("********************* roles initialized **************************");
            }
            List<User> users = userRepository.findAll();
            if(users.isEmpty()){
                log.info("******************** START USER INITIALIZATION *************************");
                String password = UUID.randomUUID().toString();
                User user = User.builder()
                        .enabled(true)
                        .email("admin@spring.io")
                        .username("admin")
                        .password(passwordEncoder.encode(password))
                        .creation(LocalDateTime.now())
                        .build();
                User userSaved = userRepository.save(user);
                List<Role> roleList = roleRepository.findAll();
                userSaved.setRoles(roleList);
                userRepository.save(userSaved);
                log.info("USERNAME = USERNAME");
                log.info("PASSWORD = "+password);
                log.info("******************** You must change the username and password after your first authentication *************************");
            }
        };
    }


}
