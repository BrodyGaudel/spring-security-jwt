package com.brodygaudel.securityservice.security;

import com.brodygaudel.securityservice.entity.User;
import com.brodygaudel.securityservice.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom implementation of the Spring Security UserDetailsService interface.
 * This service is responsible for loading user details by username and converting
 * the retrieved user information into a UserDetails object for authentication.
 *
 * @since 2024
 * @author Brody Gaudel MOUNANGA BOUKA
 * @version 1.0
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs a new MyUserDetailsService with the specified UserRepository.
     *
     * @param userRepository The UserRepository used for retrieving user information.
     */
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by the provided username.
     *
     * @param username The username of the user to load.
     * @return UserDetails object representing the loaded user information.
     * @throws UsernameNotFoundException If the user with the given username is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        user.getRoles().forEach(
                role -> {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getName());
                    grantedAuthorities.add(grantedAuthority);
                }
        );
        return new org.springframework.security.core.userdetails.
                User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
