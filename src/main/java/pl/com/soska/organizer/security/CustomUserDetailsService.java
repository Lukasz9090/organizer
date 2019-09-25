package pl.com.soska.organizer.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.com.soska.organizer.exception.UserNotFoundException;
import pl.com.soska.organizer.model.Role;
import pl.com.soska.organizer.model.User;
import pl.com.soska.organizer.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not exist!"));

        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        convertAuthorities(user.getRole())
                );
        return userDetails;
    }

    private Set<GrantedAuthority> convertAuthorities(Set <Role> userRoles){
        Set<GrantedAuthority> authorities = new HashSet<>();
       for (Role ur : userRoles){
           authorities.add(new SimpleGrantedAuthority(ur.getRole().getDescription()));
       }
       return authorities;
    }
}
