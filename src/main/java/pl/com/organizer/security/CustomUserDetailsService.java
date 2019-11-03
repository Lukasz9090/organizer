package pl.com.organizer.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.com.organizer.exception.UnconfirmedAccountException;
import pl.com.organizer.exception.UserNotFoundException;
import pl.com.organizer.model.Role;
import pl.com.organizer.model.User;
import pl.com.organizer.repository.UserRepository;

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
        User user = getUserByUsername(username);

        if (!user.isActive()){
            throw new UnconfirmedAccountException("You have to confirm your email address before continuing");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                convertAuthorities(user.getRole())
        );
    }

    private User getUserByUsername(String username){
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not exist!"));
    }

    private Set<GrantedAuthority> convertAuthorities(Set <Role> userRoles){
        Set<GrantedAuthority> authorities = new HashSet<>();
       for (Role ur : userRoles){
           authorities.add(new SimpleGrantedAuthority(ur.getRole().getDescription()));
       }
       return authorities;
    }
}
